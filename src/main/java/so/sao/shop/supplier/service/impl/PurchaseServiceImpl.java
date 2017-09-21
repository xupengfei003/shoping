package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.config.StorageConfig;
import so.sao.shop.supplier.config.azure.AzureBlobService;
import so.sao.shop.supplier.dao.*;
import so.sao.shop.supplier.domain.*;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.*;
import so.sao.shop.supplier.pojo.output.CommodityOutput;
import so.sao.shop.supplier.pojo.output.OrderRefuseReasonOutput;
import so.sao.shop.supplier.pojo.output.PurchaseItemPrintOutput;
import so.sao.shop.supplier.pojo.vo.*;
import so.sao.shop.supplier.service.CommodityService;
import so.sao.shop.supplier.service.PurchaseService;
import so.sao.shop.supplier.service.QrcodeService;
import so.sao.shop.supplier.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单信息  service 实现
 * </p>
 *
 * @author 透云-中软-西安项目组
 * @since 2017-07-19
 */
@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Resource
    private PurchaseDao purchaseDao;
    @Resource
    private PurchaseItemDao purchaseItemDao;
    @Resource
    private CommodityService commodityService;
    @Resource
    private SupplierCommodityDao supplierCommodityDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    QrcodeDao qrcodeDao;// 二维码dao
    @Autowired
    QrcodeService qrcodeService;// 二维码service
    @Autowired
    StorageConfig storageConfig;//上传到云端的配置
    @Value("${qrcode.receive.url}")
    private String receiveUrl;  //收货二维码内容中的地址前缀
    @Resource
    private AzureBlobService azureBlobService; // azure blob存储相关
    @Resource
    private NotificationDao notificationDao;

    /**
     * 保存订单信息
     *
     * @param purchase 订单对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> savePurchase(PurchaseInput purchase) throws Exception {
        Map<String, Object> output = new HashMap<>();
        output.put("status", Constant.CodeConfig.CODE_FAILURE);
        /*
            1.根据商户进行拆单
                a.根据商品查出所有商户信息。
                b.循环商户ID生成订单
            2.循环订单详情信息 生成批量插入订单详情数据，统计订单总价
            3.生成订单数据
            4.给供应商推送消息
         */
        List<PurchaseItemVo> listPurchaseItem = purchase.getListPurchaseItem();//订单详情
        Set<Long> set = new HashSet<>();
        for (PurchaseItemVo purchaseItem : listPurchaseItem) {
            //a.根据商品查出所有商户信息。
            Long goodsId = purchaseItem.getGoodsId();//商品ID
            if (null != goodsId) {
                //根据供应商商品ID获取商品信息
                CommodityOutput commodityOutput = supplierCommodityDao.findDetail(goodsId);
                //判断库存是否充足
                if (null != commodityOutput && purchaseItem.getGoodsNumber() > commodityOutput.getInventory()) {
                    //提示信息
                    output.put("message", "商品库存不足");
                    return output;
                }
                Account accountUser = purchaseDao.findAccountById(goodsId);
                if (null != accountUser && 1 != accountUser.getAccountStatus()) {//商家账号为禁用或删除状态，不允许下单
                    output.put("message", "商家账号为禁用或删除状态，不允许下单");
                    return output;
                } else {
                    set.add(accountUser.getAccountId());
                }
            }
        }

        //b.循环商户ID生成订单
        List<Purchase> listPurchase = new ArrayList<>();
        List<PurchaseItem> listItem = new ArrayList<>();
        List<Notification> notificationList = new ArrayList<>();
        Map<Long, BigDecimal> inventoryMap = new HashMap<>();//存储商品编号和购买数量
        //合并支付单号
        String payId = NumberGenerate.generateUuid();
        for (Long sId : set) {
            //生成订单编号
            String orderId = NumberGenerate.generateUuid();
            BigDecimal totalMoney = new BigDecimal(0);//订单总价计算
            BigDecimal orderSettlemePrice = new BigDecimal(0);//结算金额
            for (PurchaseItemVo purchaseItem : listPurchaseItem) {
                //根据商品ID查询商户ID
                Long goodsId = purchaseItem.getGoodsId();//商品ID
                Account accountUser = purchaseDao.findAccountById(goodsId);
                //判断当前商品是否属于该商户
                if (null != accountUser && sId.equals(accountUser.getAccountId())) {
                    BigDecimal goodsNumber = new BigDecimal(purchaseItem.getGoodsNumber());//商品数量
                    String goodsAttribute = purchaseItem.getGoodsAttribute();//商品属性
                    //查询商品信息
                    Result result = commodityService.getCommodity(goodsId);
                    CommodityOutput commOutput = (CommodityOutput) result.getData();
                    //2.生成批量插入订单详情数据
                    PurchaseItem item = new PurchaseItem();
                    item.setGoodsAttribute(goodsAttribute);//商品属性
                    item.setGoodsId(goodsId);//商品ID
                    item.setCode69(commOutput.getCode69()); //TODO 添加商品条码code69
                    item.setGoodsNumber(goodsNumber.intValue());//商品数量
                    inventoryMap.put(commOutput.getId(), goodsNumber.negate());//记录该商品购买数量
                    BigDecimal price = commOutput.getPrice();//市场价
                    BigDecimal unitPrice = commOutput.getUnitPrice();//成本价
                    item.setGoodsUnitPrice(price);
                    totalMoney = totalMoney.add(goodsNumber.multiply(price));//订单实付金额
                    orderSettlemePrice = orderSettlemePrice.add(goodsNumber.multiply(unitPrice));//订单结算金额
                    item.setGoodsTatolPrice(goodsNumber.multiply(price));//单个商品总价
                    item.setGoodsImage(commOutput.getMinImg());//商品图片
                    item.setGoodsName(commOutput.getName());//商品名称
                    item.setBrandName(commOutput.getBrandName());//品牌
                    item.setOrderId(orderId);//订单ID
                    item.setPayId(payId);//合并支付ID
                    listItem.add(item);
                }
            }
            //3.生成订单数据
            Long userId = purchase.getUserId();//门店ID
            String orderReceiverName = purchase.getOrderReceiverName();//收货人姓名
            String orderReceiverMobile = purchase.getOrderReceiverMobile();//收货人电话
            String orderAddress = purchase.getOrderAddress();//收货人地址
            Integer orderPaymentMethod = purchase.getOrderPaymentMethod();//支付方式
            Purchase purchaseDate = new Purchase();
            purchaseDate.setOrderId(orderId);//订单ID
            purchaseDate.setPayId(payId);//合并支付单号
            purchaseDate.setStoreId(sId);//商户ID
            Account account = accountDao.selectById(sId);//根据商户id查询商户信息得到商家名称
            purchaseDate.setStoreName(account.getProviderName());//商家名称
            purchaseDate.setUserId(userId);//门店ID
            purchaseDate.setUserName(purchase.getUserName());//门店名称
            purchaseDate.setOrderReceiverName(orderReceiverName);//收货人姓名
            purchaseDate.setOrderReceiverMobile(orderReceiverMobile);//收货人电话
            purchaseDate.setOrderAddress(orderAddress);//收货人地址
            purchaseDate.setOrderPaymentMethod(orderPaymentMethod);//支付方式
            purchaseDate.setOrderPrice(totalMoney);//订单实付金额
            purchaseDate.setOrderSettlemePrice(orderSettlemePrice);//订单结算金额
            purchaseDate.setOrderCreateTime(new Date());//下单时间
            purchaseDate.setOrderStatus(Constant.OrderStatusConfig.PAYMENT);//订单状态 1待付款2代发货3已发货4已收货5已拒收6已退款
            purchaseDate.setUpdatedAt(new Date());//更新时间
            // TODO 邮费计算
            listPurchase.add(purchaseDate);
            /*
               1.根据商品编号更改库存数量
               2.将订单信息保存至订单表
               3.将订单详情保存只订单详情
               4.订单生成成功（返回订单ID，金额）
            */
            //给该供应商增加一条消息数据
            Notification notification = createNotification(sId, orderId, Constant.OrderStatusConfig.PAYMENT);
            notificationList.add(notification);
        }
        boolean flag = false;
        if (null != listPurchase && listPurchase.size() > 0 && null != listItem && listItem.size() > 0) {
            //根据商品编号更改库存数量
            int count = supplierCommodityDao.updateInventoryByGoodsId(inventoryMap);
            if (count == 0) {
                output.put("message", "商品库存不足");
                return output;
            }
            int result = purchaseDao.savePurchase(listPurchase);
            int resultSum = purchaseItemDao.savePurchaseItem(listItem);
            BigDecimal totalMoney = new BigDecimal(0);//所有订单实付总金额
            if (result > 0 && resultSum > 0) {
                flag = true;
                //TODO 订单生成成功给该供应商推送一条消息
                if (null != notificationList && notificationList.size() > 0) {
                    notificationDao.saveNotifications(notificationList);
                }
                for (Purchase obj : listPurchase) {
                    //计算所有订单总金额
                    totalMoney = totalMoney.add(obj.getOrderPrice());
                }
            } else {//保存订单失败，主动回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//主动回滚
            }
            if (flag) {
                output.put("status", Constant.CodeConfig.CODE_SUCCESS);
                output.put("orderId", payId);
                output.put("totalMoney", totalMoney);
            }
        }
        return output;
    }

    /**
     * 根据订单ID获取订单详情.
     *
     * @param orderId orderId
     * @return PurchaseInfoVo
     * @throws Exception Exception
     */
    @Override
    public PurchaseInfoVo findById(String orderId) throws Exception {
        PurchaseInfoVo purchaseInfoVo = new PurchaseInfoVo();
        Purchase purchase = purchaseDao.findById(orderId);
        if (purchase != null) {
            //PurchaseInfoVo 添加订单信息
            if(!StringUtils.isEmpty(String.valueOf(purchase.getOrderPostage()))){
                purchaseInfoVo.setOrderPostage("包邮");
            } else {
                purchaseInfoVo.setOrderPostage(NumberUtil.number2Thousand(purchase.getOrderPostage()));
            }
            purchaseInfoVo.setOrderId(purchase.getOrderId());
            purchaseInfoVo.setOrderReceiverName(purchase.getOrderReceiverName());
            purchaseInfoVo.setOrderReceiverMobile(purchase.getOrderReceiverMobile());
            purchaseInfoVo.setOrderCreateTime(purchase.getOrderCreateTime());
            purchaseInfoVo.setOrderPaymentMethod(purchase.getOrderPaymentMethod());
            purchaseInfoVo.setOrderPaymentNum(purchase.getOrderPaymentNum());
            purchaseInfoVo.setOrderPaymentTime(purchase.getOrderPaymentTime());
            purchaseInfoVo.setOrderPrice(NumberUtil.number2Thousand(purchase.getOrderPrice()));
            purchaseInfoVo.setOrderStatus(purchase.getOrderStatus().shortValue());
            purchaseInfoVo.setOrderShipMethod(purchase.getOrderShipMethod());
            purchaseInfoVo.setOrderShipmentNumber(purchase.getOrderShipmentNumber());
            purchaseInfoVo.setLogisticsCompany(purchase.getLogisticsCompany());
            purchaseInfoVo.setDistributorName(purchase.getDistributorName());
            purchaseInfoVo.setDistributorMobile(purchase.getDistributorMobile());
            purchaseInfoVo.setDrawbackTime(purchase.getDrawbackTime());
            purchaseInfoVo.setOrderAddress(purchase.getOrderAddress());
            //添加订单明细列表
            List<PurchaseItemVo> purchaseItemVoList = purchaseItemDao.getOrderDetailByOId(purchase.getOrderId());
            //转换金额为千分位
            for (PurchaseItemVo purchaseItemVo : purchaseItemVoList) {
                purchaseItemVo.setGoodsTatolPrice(NumberUtil.number2Thousand(new BigDecimal(purchaseItemVo.getGoodsTatolPrice())));
                purchaseItemVo.setGoodsUnitPrice(NumberUtil.number2Thousand(new BigDecimal(purchaseItemVo.getGoodsUnitPrice())));
            }
            if (purchaseItemVoList != null && purchaseItemVoList.size() > 0) {
                purchaseInfoVo.setPurchaseItemVoList(purchaseItemVoList);
            } else {
                purchaseInfoVo.setPurchaseItemVoList(new ArrayList<>());
            }
        }
        return purchaseInfoVo;
    }

    /**
     * 查询订单列表，并分页
     *
     * @param pageNum
     * @param rows
     * @return List<Order>
     */
    @Override
    public PageInfo<PurchasesVo> searchOrders(Integer pageNum, Integer rows, PurchaseSelectInput purchaseSelectInput) throws Exception {
        PageHelper.startPage(pageNum, rows);
        List<PurchasesVo> orderList = purchaseDao.findPage(purchaseSelectInput);
        //转换金额为千分位
        for (PurchasesVo purchasesVo : orderList) {
            if(StringUtils.isEmpty(purchasesVo.getOrderPostage()) || "0".equals(purchasesVo.getOrderPostage())){
                purchasesVo.setOrderPostage("包邮");
            } else {
                purchasesVo.setOrderPostage(NumberUtil.number2Thousand(new BigDecimal(purchasesVo.getOrderPostage())));
            }
            purchasesVo.setOrderPrice(NumberUtil.number2Thousand(new BigDecimal(purchasesVo.getOrderPrice())));
        }
        return new PageInfo<>(orderList);
    }

    /**
     * 发货接口
     *
     * @param orderId
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deliverGoods(String orderId, Integer receiveMethod, String name, String number) throws Exception {
        purchaseDao.deliverGoods(orderId, Constant.OrderStatusConfig.ISSUE_SHIP, new Date(), receiveMethod, name, number);
        //TODO 更改订单状态成功后向该供应商推送一条消息
        pushNotification(orderId, Constant.OrderStatusConfig.ISSUE_SHIP);
    }

    /**
     * 批量删除订单
     *
     * @param orderIds
     * @return
     */
    @Override
    public void deletePurchase(String orderIds) {
        String[] orderIdArr = orderIds.split(",");
        Date updateDate = new Date();
        purchaseDao.deleteByOrderId(orderIdArr, updateDate);
    }

    /**
     * POI导出(当前页/所选页/全部)订单列表
     *
     * @param request   request
     * @param response  response
     * @param pageNum   pageNum
     * @param accountId accountId
     * @param pageSize  pageSize
     */
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, String pageNum, Integer pageSize, Long accountId, PurchaseSelectInput purchaseSelectInput) throws Exception {

        //创建HSSFWorkbook对象(excel的文档对象)
        HSSFWorkbook wb = new HSSFWorkbook();
        //声明一个单子并命名
        HSSFSheet sheet = wb.createSheet("订单统计");
        for (int i = 0; i < 10; i++) {
            sheet.setColumnWidth(i, 25 * 256);
        }
        // 生成一个样式
        HSSFCellStyle style = wb.createCellStyle();

        //创建第一行（也可以称为表头）
        HSSFRow row = sheet.createRow(0);
        row.setHeightInPoints(30);
        //样式字体居中
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //给表头第一行一次创建单元格
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("订单编号");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("收货人");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("联系电话");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("订单状态");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("订单金额");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("下单时间");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("支付类型");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("支付流水号");
        cell.setCellStyle(style);

        List<Purchase> orderList;
        List<Integer> pageNumList;

        if (pageNum != null && pageNum.length() > 0 && pageSize != null) { //获取区间页列表
            //java8新特性 逗号分隔字符串转List<Integer>
            pageNumList = Arrays.asList(pageNum.split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
            if (pageNumList.size() > 1) {
                Collections.sort(pageNumList);
                orderList = (List<Purchase>) PageTool.getListByPage(purchaseDao.getOrderListByIds(purchaseSelectInput, accountId), pageNumList.get(0), pageNumList.get(1), pageSize);
            } else { //获取当前页列表
                orderList = (List<Purchase>) PageTool.getListByPage(purchaseDao.getOrderListByIds(purchaseSelectInput, accountId), pageNumList.get(0), pageNumList.get(0), pageSize);
            }
        } else { //查询全部列表
            orderList = purchaseDao.getOrderListByIds(purchaseSelectInput, accountId);
        }
        //向单元格里填充数据
        HSSFCell cellTemp = null;
        for (int i = 0; i < orderList.size(); i++) {
            Purchase purchase = orderList.get(i);
            sheet.setColumnWidth(0, 20 * 256);
            row = sheet.createRow(i + 1);
            cellTemp = row.createCell(0);
            cellTemp.setCellValue(purchase.getOrderId());
            cellTemp.setCellStyle(style);
            cellTemp = row.createCell(1);
            cellTemp.setCellValue(purchase.getOrderReceiverName());
            cellTemp.setCellStyle(style);
            cellTemp = row.createCell(2);
            cellTemp.setCellValue(purchase.getOrderReceiverMobile());
            cellTemp.setCellStyle(style);
            Integer status = purchase.getOrderStatus();
            String orderStatus = "";
            if (status == Constant.OrderStatusConfig.PAYMENT) {
                orderStatus = Constant.OrderMessageConfig.PAYMENT;
            } else if (status == Constant.OrderStatusConfig.PENDING_SHIP) {
                orderStatus = Constant.OrderMessageConfig.PENDING_SHIP;
            } else if (status == Constant.OrderStatusConfig.ISSUE_SHIP) {
                orderStatus = Constant.OrderMessageConfig.ISSUE_SHIP;
            } else if (status == Constant.OrderStatusConfig.RECEIVED) {
                orderStatus = Constant.OrderMessageConfig.RECEIVED;
            } else if (status == Constant.OrderStatusConfig.REJECT) {
                orderStatus = Constant.OrderMessageConfig.REJECT;
            } else if (status == Constant.OrderStatusConfig.REFUNDED) {
                orderStatus = Constant.OrderMessageConfig.REFUNDED;
            }
            cellTemp = row.createCell(3);
            cellTemp.setCellValue(orderStatus);
            cellTemp.setCellStyle(style);
            cellTemp = row.createCell(4);
            cellTemp.setCellValue(purchase.getOrderPrice() + "￥");
            cellTemp.setCellStyle(style);
            cellTemp = row.createCell(5);
            cellTemp.setCellValue(purchase.getOrderCreateTime() == null ? "" : StringUtil.fomateData(purchase.getOrderCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            cellTemp.setCellStyle(style);
            Integer paymentMethod = purchase.getOrderPaymentMethod();
            String payment = "";
            if (paymentMethod == Constant.PaymentStatusConfig.ALIPAY) {
                payment = Constant.PaymentMsgConfig.ALIPAY;
            } else if (paymentMethod == Constant.PaymentStatusConfig.WECHAT) {
                payment = Constant.PaymentMsgConfig.WECHAT;
            }
            cellTemp = row.createCell(6);
            cellTemp.setCellValue(payment);
            cellTemp.setCellStyle(style);
            cellTemp = row.createCell(7);
            cellTemp.setCellValue(purchase.getOrderPaymentNum());
            cellTemp.setCellStyle(style);
        }
        //输出Excel文件
        OutputStream output = null;
        try {
            output = response.getOutputStream();
            response.reset();
            String agent = request.getHeader("USER-AGENT").toLowerCase();
            response.setContentType("application/vnd.ms-excel");
            String fileName = "订单统计" + System.currentTimeMillis();
            String codedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            if (agent.contains("firefox")) {
                response.setCharacterEncoding("utf-8");
                response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xls");
            } else {
                response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            }
            wb.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过商户ID查询订单状态，计算订单总金额
     *
     * @param storeId
     * @return
     */
    @Override
    public String findOrderStatus(Long storeId) {
        //商户ID存在，将计算的总金额赋值给历史总金额字段
        BigDecimal income = purchaseDao.findOrderStatus(storeId);
        accountDao.updateUserPrice(income, storeId);//将查询到的总金额赋值给account表中的income(历史总收入)；
        return NumberUtil.number2Thousand(income);
    }

    /**
     * 根据商家编号及查询条件（起始创建订单-结束创建订单时间/起始下单时间-结束下单时间/支付方式;支付流水号/订单编号/收货人名称/收货人联系方式）查找所有相关订单记录(分页)
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示条数
     * @param input    查询条件封装类
     * @param storeId  商家编号
     * @return output 出参
     */
    @Override
    public Result<PageInfo> searchPurchasesHigh(Integer pageNum, Integer pageSize, AccountPurchaseInput input, Long storeId) throws ParseException {
        /**
         * 1.校验条件类
         *      1.1 比较开始时间结束时间是否符合规则，不符合则返回提示信息
         * 2.设置分页
         * 3.访问持久化层，获取数据
         *      3.1 若获取到的结果集不为空：
         *           ① 将结果集中的泛型（domain类）转化成vo类
         *           ② 出参中封装分页信息（当前页码，总页码，总条数，结果集）、“成功”的code码和message
         *      3.2 若获取到的结果集为空时
         *           ①.在出参对象中封装“未查询到结果集”的code码和message
         *
         */
        //1.校验条件类
        if (Ognl.isNotEmpty(input)) {
            //1.1 比较开始时间结束时间是否符合规则，不符合则返回提示信息
            if (DataCompare.compareDate(input.getCreateBeginTime(), input.getCreateEndTime())) {
                return Result.fail(Constant.MessageConfig.DateNOTLate);
            }
            if (DataCompare.compareDate(input.getPayBeginTime(), input.getPayEndTime())) {
                return Result.fail(Constant.MessageConfig.DateNOTLate);
            }
        }
        //2.分页
        PageTool.startPage(pageNum, pageSize);

        //3.访问持久化层，获取数据
        List<Purchase> purchaseList = purchaseDao.findPageByStoreId(input, storeId);

        Result result;//声明出参对象
        //3.1 若获取到的结果集不为空
        if (null != purchaseList && !purchaseList.isEmpty()) {
            PageInfo<Purchase> pageInfo = new PageInfo<>(purchaseList);//分页对象
            //①.将结果集中的泛型（domain类）转化成vo类
            List<AccountPurchaseVo> purchaseVos = this.inversion(purchaseList);
            //②.出参中封装分页信息（当前页码，总页码，总条数，结果集）、“成功”的code码和message
            PageInfo<AccountPurchaseVo> pageInfoVo = new PageInfo<>();
            pageInfoVo.setPageNum(pageInfo.getPageNum());//当前页码
            pageInfoVo.setPageSize(pageInfo.getPageSize());//每页显示条数
            pageInfoVo.setTotal(pageInfo.getTotal());//总条数
            pageInfoVo.setPages(pageInfo.getPages());//总页码
            pageInfoVo.setList(purchaseVos);//显示数据
            pageInfoVo.setSize(pageInfo.getSize());//每页实际数据条数
            result = Result.success(Constant.MessageConfig.MSG_SUCCESS, pageInfoVo);
        } else {//3.2 若获取到的结果集为空时
            // ①.在出参对象中封装“未查询到结果集”的code码和message
            result = Result.success(Constant.MessageConfig.MSG_NO_DATA);
        }
        return result;
    }

    /**
     * 根据商家编号及查询条件（起始创建订单-结束创建订单时间/支付流水号/订单编号/收货人名称）查找所有相关订单记录(分页)
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示条数
     * @param input    查询条件封装类
     * @param storeId  商家编号
     * @return 出参
     */
    @Override
    public Result<PageInfo> searchPurchasesLow(Integer pageNum, Integer pageSize, AccountPurchaseLowInput input, Long storeId) throws ParseException {
        /**
         * 1.校验条件类
         *      1.1 比较开始时间结束时间是否符合规则，不符合则返回提示信息
         * 2.设置分页
         * 3.访问持久化层，获取数据
         *      3.1 若获取到的结果集不为空：
         *           ① 将结果集中的泛型（domain类）转化成vo类
         *           ② 出参中封装分页信息（当前页码，总页码，总条数，结果集）、“成功”的code码和message
         *      3.2 若获取到的结果集为空时
         *           ①.在出参对象中封装“未查询到结果集”的code码和message
         *
         */

        //1.校验条件类
        if (Ognl.isNotEmpty(input)) {
            //比较开始时间和结束时间是否输入正常 若输入不正常返回提示信息
            if (DataCompare.compareDate(input.getCreateBeginTime(), input.getCreateEndTime())) {
                return Result.fail(Constant.MessageConfig.DateNOTLate);
            }
        }

        //2.分页
        PageTool.startPage(pageNum, pageSize);

        //3.访问持久化层，获取数据
        List<Purchase> purchaseList = purchaseDao.findPageByStoreIdLow(input, storeId);

        Result result;//声明出参对象
        // 3.1 若获取到的结果集不为空：
        if (null != purchaseList && !purchaseList.isEmpty()) {
            PageInfo<Purchase> pageInfo = new PageInfo<>(purchaseList);
            //①.将结果集中的泛型（domain类）转化成vo类
            List<AccountPurchaseVo> purchaseVos = this.inversion(purchaseList);
            //②.出参中封装分页信息（当前页码，总页码，总条数，结果集）、“成功”的code码和message
            PageInfo<AccountPurchaseVo> pageInfoVo = new PageInfo<>();
            pageInfoVo.setPageNum(pageInfo.getPageNum());//当前页码
            pageInfoVo.setPageSize(pageInfo.getPageSize());//每页显示条数
            pageInfoVo.setTotal(pageInfo.getTotal());//总条数
            pageInfoVo.setPages(pageInfo.getPages());//总页码
            pageInfoVo.setSize(pageInfo.getSize());//每页实际数据条数
            pageInfoVo.setList(purchaseVos);//显示数据

            result = Result.success(Constant.MessageConfig.MSG_SUCCESS, pageInfoVo);
        } else {//3.2 若获取到的结果集为空时
            result = Result.success(Constant.MessageConfig.MSG_NO_DATA);
        }
        return result;
    }

    /**
     * 根据订单ID获取订单状态
     *
     * @param orderId
     * @return
     */
    @Override
    public Integer findOrderStatus(String orderId) {
        return purchaseDao.getOrderStatus(orderId);
    }

    /**
     * 根据支付ID获取订单ID列表
     *
     * @param payId 支付ID
     * @return
     */
    @Override
    public List<String> findOrderStatusByPayId(String payId) {
        List<Purchase> purchaseList = purchaseDao.findByPayId(payId);
        List<String> orderStatusList = new ArrayList<>();
        if (purchaseList.size() > 0) {
            for (Purchase purchase : purchaseList) {
                orderStatusList.add(purchase.getOrderId());
            }
        }
        return orderStatusList;
    }

    /**
     * 查询配送单信息
     * <p>
     * 根据订单编号查询配送单页面的所有信息。
     * 1.查询订单信息；
     * 2.查询商品条目；
     * 3.将订单信息和商品条目封装到output对象；
     * 4.在实体类属性的get方法中格式化数据。
     *
     * @param orderId 订单编号
     * @return 返回一个PurchaseItemPrintOutput对象
     * @throws Exception 异常
     */
    @Override
    public PurchaseItemPrintOutput getPrintItems(String orderId) throws Exception {
        // 1.查询订单信息（客户、客户电话、收货地址、下单时间、合计）
        PurchasePrintVo purchasePrintVo = purchaseDao.findPrintOrderInfo(orderId);

        if (null == purchasePrintVo) { // 未查询到订单信息
            return null;
        }

        // 2.查询订单的商品条目
        List<PurchaseItemPrintVo> purchaseItemPrintVos = purchaseItemDao.findPrintItems(orderId);

        // 3.封装返回对象
        PurchaseItemPrintOutput output = new PurchaseItemPrintOutput(); // 封装返回对象
        output = BeanMapper.map(purchasePrintVo, output.getClass()); // 将订单信息复制到output
        output.setPurchaseItemPrintVos(purchaseItemPrintVos); // 添加商品条目

        return output;
    }

    /**
     * 生成单个收货二维码
     * <p>
     * 根据订单编号实现二维码的生成，上传，保存到数据库。
     * 1.验证订单,验证订单和二维码的关系；
     * 2.生成二维码图片。
     * 3.将二维码图片上传到云端；
     * 4.将二维码信息保存到数据库；
     * 5.删除本地图片。
     *
     * @param orderId 订单编号
     * @throws Exception 异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createReceivingQrcode(String orderId) throws Exception {
        // 1.验证订单并判断订单编号是否存在关联的二维码
        Purchase purchase = purchaseDao.findById(orderId);
        if (null == purchase) {
            throw new Exception("订单不存在");
        }

        List<Qrcode> qrcodeList = qrcodeDao.findQrcodeByOrderId(orderId); // 查询订单对应的二维码
        if (null != qrcodeList && qrcodeList.size() > 0) {
            throw new Exception("该订单已经存在二维码");
        }

        // 2.生成二维码图片
        Map<String, String> map = generateQrcodeByOrderId(orderId, null); // 失败抛出异常

        // 3.将二维码图片上传到云端
        List files = new ArrayList();
        files.add(map.get("name"));
        List<CommBlobUpload> blobUploadEntities = batchUploadPic(map.get("path"), files, 0);
        if (blobUploadEntities.size() != 1) {
            throw new Exception("上传二维码图片到云端失败");
        }
        CommBlobUpload blobUpload = blobUploadEntities.get(0); // 云端返回的实体

        // 4.将二维码信息保存到数据库
        Qrcode qrcode = new Qrcode(NumberGenerate.generateId(), orderId, blobUpload.getUrl(), map.get("content"));
        qrcodeDao.save(qrcode);

        // 5.删除本地图片
        System.gc();
        FileUtil.deleteDirectory(map.get("path"));
    }

    /**
     * 生成二维码
     * <p>
     * 根据订单编号，生成二维码，返回二维码信息。
     * 1.path不存在，自动生成path：/qrocode/时间戳；
     * 2.自动生成二维码名称：uuid + 时间戳 + .png
     * 3.根据二维码内容、path、名称生成二维码
     * 4.生成失败，删除path，抛出异常
     * 5.生成成功，将路径、名称、内容封装到Map返回
     *
     * @param orderId 订单编号
     * @return 返回Map对象，封装了生成二维码的路径、名称、内容
     * @throws Exception 生成二维码异常
     */
    public Map generateQrcodeByOrderId(String orderId, String path) throws Exception {
        // 生成二维码路径
        if (Ognl.isEmpty(path)) {
            path = "/qrcode/" + System.currentTimeMillis();
            FileUtil.createDir(path);
        }

        // 生成二维码名称
        String name = NumberGenerate.generateId() + System.currentTimeMillis() + ".png";
        String img = path + "/" + name; // 二维码相对地址

        // 配置二维码内容
        String content = configQrcodeContent(orderId);

        // 生成二维码图片并输出到本地img
        boolean flag = qrcodeService.createQrCode(new FileOutputStream(new File(img)), content, 230, "png");
        if (!flag) { // 失败
            System.gc();
            FileUtil.deleteDirectory(path); // 删除本地二维码文件夹
            throw new Exception("生成二维码图片失败");
        }

        Map result = new HashMap(); // 封装返回结果
        result.put("path", path); // 二维码路径
        result.put("name", name); // 二维码名称
        result.put("img", img); // 二维码路径 + 名称
        result.put("content", content); // 二维码内容

        return result;
    }

    /**
     * 配置二维码内容
     * <p>
     * 根据订单编号配置二维码内容。
     *
     * @param orderId 订单编号
     * @return 二维码内容
     */
    public String configQrcodeContent(String orderId) {
        return receiveUrl + "?orderId=" + orderId;
    }

    /**
     * 批量生成收货二维码
     * <p>
     * 根据支付id查询所有订单，批量给每个订单实现生成二维码、上传云端、保存到数据。
     * 如果上传云端失败，则将失败的图片重新上传3次，3次后还失败，不再处理，不抛异常。
     * 1.根据支付id查询对应订单是否已经存在二维码；
     * 2.批量生成二维码并存储二维码信息；
     * 3.批量上传到云端；
     * 4.批量插入数据库。
     *
     * @param payId 支付id
     * @throws Exception 可能是订单异常
     */
    @Transactional(rollbackFor = Exception.class)
    public void createReceivingQrcodeByPayId(String payId) throws Exception {
        List<Purchase> purchaseList = purchaseDao.findByPayId(payId); // 根据支付id查询订单列表
        // 1.根据支付id查询对应订单是否已经存在二维码
        int count = qrcodeDao.getQrcodesByPayId(payId);
        if (count > 0) { // 订单存在二维码
            throw new Exception("一个订单只能生成一个二维码");
        }

        // 2.批量生成二维码并存储二维码信息
        List<String> qrcodePics = new ArrayList<>(); // 存储二维码图片
        List<Qrcode> qrcodeList = new ArrayList<>(); // 存储二维码实体
        String path = "/qrcode/" + System.currentTimeMillis(); // 生成二维码的本地路径
        FileUtil.createDir(path);
        if (null != purchaseList && purchaseList.size() > 0) {
            for (int i = 0; i < purchaseList.size(); i++) {
                String orderId = purchaseList.get(i).getOrderId(); // 订单编号

                // 生成二维码
                Map<String, String> map = generateQrcodeByOrderId(orderId, path); // 失败抛出异常

                // 封装二维码信息
                Qrcode qrcode = new Qrcode(NumberGenerate.generateId(), orderId, "", map.get("content"));
                qrcodeList.add(qrcode); // 存储二维码信息
                qrcodePics.add(map.get("name")); // 存储本地二维码图片名称
            }
        }

        // 3.批量上传到云端
        List<CommBlobUpload> blobUploadEntities = batchUploadPic(path, qrcodePics, 3);

        if (null != blobUploadEntities) {
            for (int i = 0; i < blobUploadEntities.size(); i++) {
                CommBlobUpload blobUpload = blobUploadEntities.get(i);
                String fileName = blobUpload.getFileName();
                qrcodeList.get(qrcodePics.indexOf(fileName)).setUrl(blobUpload.getUrl()); // 云端的二维码地址
            }
        }

        // 4.批量插入数据库
        qrcodeList.removeIf(qrcode -> {
            return Ognl.isEmpty(qrcode.getUrl());
        });
        if (qrcodeList.size() > 0) {
            qrcodeDao.saveQrcodes(qrcodeList);
        }

        // 5.删除本地缓存的二维码图片
        System.gc();
        FileUtil.deleteDirectory(path); // 删除本地二维码文件夹
    }

    /**
     * 批量上传
     * <p>
     * 批量上传到云端，失败后重新尝试n次，n次后仍然失败，不再处理，仅返回所有上传成功的对象集合。
     * 1.调用uploadFilesComm上传文件；
     * 2.分离出上传成功和失败的文件；
     * 3.上传成功的结果保存起来；
     * 4.上传失败的结果，如果尝试次数number大于等于0，重新上传；
     * 5.本次和下次上传成功的结果合并到list集合，返回。
     *
     * @param path   本地路径
     * @param files  上传文件列表
     * @param number 尝试次数
     * @return 返回CommBlobUpload的集合List对象
     */
    public List<CommBlobUpload> batchUploadPic(String path, List<String> files, int number) {
        // 1.调用uploadFilesComm方法上传文件
        List<Result> uploadResult = azureBlobService.uploadFilesComm(path, files); // 上传图片

        // 2.分离出上传成功和失败的文件
        boolean isAllSuccess = false; // 是否全部上传成功
        List uploadFailList = new ArrayList(); // 上传失败文件名集合
        // 3.上传成功的结果保存起来
        List<CommBlobUpload> blobUploadEntities = null; // 本次上传成功实体集合
        for (int i = 0; i < uploadResult.size(); i++) { // 循环返回结果
            Result result = uploadResult.get(i);
            if (Constant.CodeConfig.CODE_SUCCESS.equals(result.getCode())) {
                blobUploadEntities = (List<CommBlobUpload>) result.getData();
                if (blobUploadEntities.size() == files.size()) { // 上传成功的文件数量和上传数量一致，则上传成功
                    isAllSuccess = true;
                    break;
                }
            } else {
                uploadFailList.addAll((List) result.getData()); // 上传失败文件名添加到list
            }
        }

        if (isAllSuccess) { // 全部上传成功
            return blobUploadEntities;
        }

        // 4.上传失败的结果，如果尝试次数number大于等于0，重新上传
        if (number >= 0) { // 再次上传
            List<CommBlobUpload> successList = batchUploadPic(path, uploadFailList, --number); // 失败文件再次上传
            if (null != blobUploadEntities) { // 本次上传部分成功
                blobUploadEntities.addAll(successList); // 本次成功的和下次成功的合并
                return blobUploadEntities;
            } else { // 本次上传全部失败
                return successList;
            }
        } else { // 上传次数超限
            return blobUploadEntities;
        }
    }

    /**
     * 扫描二维码收货
     * <p>
     * 根据订单编号实现收货逻辑。
     * 1.验证是否可以扫码收货；
     * 2.将订单状态改为已收货；
     * 3.将二维码状态改为失效，并记录失效时间；
     * 4.推送收货消息。
     *
     * @param orderId 订单编号
     * @return 返回Map：flag：成功true|失败false,message:信息
     * @throws Exception 异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map sweepReceiving(String orderId) throws Exception {
        // 1.验证是否可以扫码收货
        PurchasePrintVo purchasePrintVo = purchaseDao.findPrintOrderInfo(orderId); // 查询订单和二维码信息
        Map map = new HashMap<>(); // 返回结果
        if (null == purchasePrintVo) {
            map.put("flag", false);
            map.put("message", "订单不存在");
            return map;
        }
        if (null == purchasePrintVo.getQrcodeStatus()
                || purchasePrintVo.getQrcodeStatus().equals(1)) { // 订单为null或二维码状态失效（1）
            map.put("flag", false);
            map.put("message", "二维码不存在或已经失效");
            return map;
        }

        // 2.将订单状态改为已收货
        purchaseDao.updateOrder(orderId, Constant.OrderStatusConfig.RECEIVED, new Date());

        // 3.将二维码状态改为失效，并记录失效时间
        Date date = new Date();
        qrcodeDao.updateStatus(orderId, 1, date, date);

        // 4.推送收货消息
        pushNotification(orderId, Constant.OrderStatusConfig.RECEIVED);

        map.put("flag", true);

        return map;
    }

    /**
     * 添加拒收货信息
     * <p>
     * 将拒收理由及相关图片保存，并修改二维码状态为失效。
     *
     * @param refuseOrderInput 封装了订单编号，拒收理由，拒收图片
     * @return Map 封装结果 键flag的值为true表示成功，false表示失败，message的值表示文字描述
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refuseOrder(RefuseOrderInput refuseOrderInput) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String orderId = refuseOrderInput.getOrderId();
        map.put("orderId", orderId);//订单编号
        map.put("refuseType", refuseOrderInput.getRefuseType());//拒收类型
        map.put("refuseReason", refuseOrderInput.getRefuseReason());//拒收理由
        Date date = new Date();
        map.put("orderRefuseTime", date);//拒收时间
        map.put("updatedAt", date);//更新时间
        map.put("orderStatus", Constant.OrderStatusConfig.REJECT);//订单状态 5 拒收
        purchaseDao.insertRefuseMessage(map);
        List<CommBlobUpload> imgList = new ArrayList<>();
        //1.如果拒收图片不传入，则不进行图片格式转换；
        //2.如果没有拒收图片，则不存入数据库
        if(refuseOrderInput.getRefuseImg().size()>0){
            for(int i = 0;i<refuseOrderInput.getRefuseImg().size(); i++){
                imgList.add(disposeImage(refuseOrderInput.getRefuseImg().get(i)));//拒收图片URL
            }
        }
        if(imgList.size()>0){
            purchaseDao.insertRefuseImg(map,imgList);
        }
        // 验证二维码是否存在，是否失效
        PurchasePrintVo purchasePrintVo = purchaseDao.findPrintOrderInfo(orderId); // 查询订单和二维码信息
        if (null == purchasePrintVo || null == purchasePrintVo.getQrcodeStatus()
                || purchasePrintVo.getQrcodeStatus().equals(1)) { // 订单为null或二维码状态失效（1）
            throw new Exception("二维码不存在或已经失效");
        }
        // 将二维码状态改为失效，并记录失效时间
        qrcodeDao.updateStatus(orderId, 1, date, date); // status失效是1

        // 订单拒收成功给该供应商推送一条消息
        pushNotification(refuseOrderInput.getOrderId(), Constant.OrderStatusConfig.REJECT);
    }

    /**
     * 根据订单ID获取该订单的拒收原因
     *
     * @param orderId 订单ID
     * @return map 封装了所有订单拒收原因信息
     */
    @Override
    public OrderRefuseReasonOutput searchRefuseReasonByOrderId(String orderId) throws Exception {
        OrderRefuseReasonVo orderRefuseReasonVo = purchaseDao.findRefuseReasonByOrderId(orderId);//查出拒收原因信息
        List<OrderRefuseImageVo> orderRefuseImageVoList = new ArrayList<>();
        if(null != orderRefuseReasonVo){
            orderRefuseImageVoList = purchaseDao.findRefuseImageByOrderId(orderId);//查出拒收图片信息
        }
        OrderRefuseReasonOutput orderRefuseReasonOutput = new OrderRefuseReasonOutput();
        orderRefuseReasonOutput.setRefuseReason(Constant.MessageConfig.MSG_NO_DATA);//如果没有数据则返回“暂无数据”
        if (null != orderRefuseReasonVo) {
            orderRefuseReasonOutput = BeanMapper.map(orderRefuseReasonVo,OrderRefuseReasonOutput.class);
        }
        if(orderRefuseImageVoList.size()>0){
            List<String> urlList = new ArrayList<>();
            orderRefuseImageVoList.forEach(orderRefuseImageVo -> {
                urlList.add(orderRefuseImageVo.getRefuseImgUrl());
            });
            orderRefuseReasonOutput.setRefuseImgUrl(urlList);
        }
        return orderRefuseReasonOutput;
    }

    //推送消息通知
    private void pushNotification(String orderId, Integer orderStatus) {
        Purchase purchase = purchaseDao.findById(orderId);
        if (null != purchase) {
            Notification notification = createNotification(purchase.getStoreId(), orderId, orderStatus);
            if (null != notification) {
                notificationDao.insert(notification);
            }
        }
    }

    //创建消息通知
    private Notification createNotification(Long accountId, String orderId, Integer status) {
        Notification notification = new Notification();
        notification.setAccountId(accountId);
        notification.setNotifiType(0);      //消息类型:0订单，1系统
        notification.setNotifiStatus(0);    //消息状态:0未读,1已读
        notification.setOrderId(orderId);
        String notifiDetail = "";
        if (status == Constant.OrderStatusConfig.PAYMENT) {
            notifiDetail = Constant.NotifiConfig.PAYMENT_NOTIFI;
        } else if (status == Constant.OrderStatusConfig.PENDING_SHIP) {
            notifiDetail = Constant.NotifiConfig.PENDING_SHIP_NOTIFI;
        } else if (status == Constant.OrderStatusConfig.ISSUE_SHIP) {
            notifiDetail = Constant.NotifiConfig.ISSUE_SHIP_NOTIFI;
        } else if (status == Constant.OrderStatusConfig.RECEIVED) {
            notifiDetail = Constant.NotifiConfig.RECEIVED_NOTIFI;
        } else if (status == Constant.OrderStatusConfig.REJECT) {
            notifiDetail = Constant.NotifiConfig.REJECT_NOTIFI;
        } else if (status == Constant.OrderStatusConfig.REFUNDED) {
            notifiDetail = Constant.NotifiConfig.REFUNDED_NOTIFI;
        } else if (status == Constant.OrderStatusConfig.CANCEL_ORDER) {
            notifiDetail = Constant.NotifiConfig.CANCEL_ORDER;
        } else if (status == Constant.OrderStatusConfig.PAYMENT_CANCEL_ORDER) {
            notifiDetail = Constant.NotifiConfig.PAYMENT_CANCEL_ORDER;
        }
        notification.setNotifiDetail(notifiDetail + orderId);
        notification.setCreatedAt(new Date());
        return notification;
    }

    /**
     * 转化集合中的泛型
     *
     * @param purchaseList 转化前的集合
     * @return 转化后的集合
     */
    private List<AccountPurchaseVo> inversion(List<Purchase> purchaseList) {
        List<AccountPurchaseVo> purchaseVos = new ArrayList<>();//转化后的集合
        AccountPurchaseVo purchaseVo = null;//转化后的对象
        for (Purchase purchase : purchaseList) {
            //循环转化数据
            purchaseVo = new AccountPurchaseVo();
            purchaseVo.setOrderId(purchase.getOrderId());//订单编号
            purchaseVo.setOrderReceiverName(purchase.getOrderReceiverName());//收货人姓名
            purchaseVo.setOrderReceiverMobile(purchase.getOrderReceiverMobile());//收货人联系方式
            purchaseVo.setOrderStatus(purchase.getOrderStatus());//订单状态
            purchaseVo.setOrderPrice(NumberUtil.number2Thousand(purchase.getOrderPrice()));//该次订单实付金额
            purchaseVo.setOrderSettlemePrice(NumberUtil.number2Thousand(purchase.getOrderSettlemePrice()));//该次订单结算金额
            purchaseVo.setOrderCreateTime(purchase.getOrderCreateTime());//订单产生时间
            purchaseVo.setOrderPaymentTime(purchase.getOrderPaymentTime());//订单支付时间
            purchaseVo.setOrderPaymentMethod(purchase.getOrderPaymentMethod());//支付方式
            purchaseVo.setOrderPaymentNum(purchase.getOrderPaymentNum());//订单流水号
            purchaseVo.setOrderPostage(NumberUtil.number2Thousand(purchase.getOrderPostage()));//运费金额
            purchaseVos.add(purchaseVo);//将转化后的数据添加到集合中
        }
        return purchaseVos;
    }

    /**
     * 添加取消订单信息
     *
     * @param cancelReasonInput 封装了订单编号，取消理由
     * @return 封装结果 键flag的值为true表示成功，false表示失败，message的值表示文字描述
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(CancelReasonInput cancelReasonInput) throws Exception {

        Integer orderStatus = purchaseDao.getOrderStatus(cancelReasonInput.getOrderId());
        Integer inputOrderStatus = Constant.OrderStatusConfig.CANCEL_ORDER;
        if (orderStatus == Constant.OrderStatusConfig.PAYMENT) {
            inputOrderStatus = Constant.OrderStatusConfig.PAYMENT_CANCEL_ORDER;
            List<PurchaseItemVo> purchaseItemVoList = purchaseItemDao.getOrderDetailByOId(cancelReasonInput.getOrderId());
            //更新仓库数量
            Map<BigInteger, BigDecimal> mapInput = new HashMap<>();
            purchaseItemVoList.forEach(purchaseItemVo -> {
                mapInput.put(BigInteger.valueOf(purchaseItemVo.getGoodsId()), BigDecimal.valueOf(purchaseItemVo.getGoodsNumber()));
            });
            int count = supplierCommodityDao.updateInventoryByGoodsId(mapInput);
            if (count == 0) {
                throw new Exception("取消失败（恢复库存失败）");
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", cancelReasonInput.getOrderId());//订单编号
        map.put("cancelType", cancelReasonInput.getCancelType());//取消类型
        map.put("cancelReason", cancelReasonInput.getCancelReason());//取消订单理由
        map.put("updatedAt", new Date());//更新时间
        map.put("orderStatus", inputOrderStatus);//订单状态 7-已付款已取消/8-待付款已取消
        purchaseDao.insertCancelMessage(map);

        //TODO 订单取消成功给该供应商推送一条消息
        pushNotification(cancelReasonInput.getOrderId(), inputOrderStatus);
    }

    /**
     * 根据订单编号查询取消订单原因
     *
     * @param orderId 订单编号
     * @return 取消订单原因
     * @throws Exception
     */
    @Override
    public String searchCancelReasonByOrderId(String orderId) throws Exception {
        String cancelReason = purchaseDao.findCancelReason(orderId);
        return cancelReason;
    }

    /**
     * 根据订单编号实现退款逻辑
     * <p>
     * 根据订单编号验证订单、修改订单状态、调用退款接口、推送退款消息。
     * 1.根据订单状态验证是否可以退款（仅已取消（7）和已拒收（5）状态的订单可以退款，其他状态不可以退款）；
     * 2.修改订单状态为退款，修改退款时间为当前时间；
     * 3.调用退款接口实现真正的退款；
     * 4.修改库存；
     * 5.推送退款消息。
     *
     * @param orderId 订单编号
     * @return 返回Map：flag：成功true|失败false,message:信息
     * @throws Exception 异常
     */
    @Transactional(rollbackFor = Exception.class)
    public Map refundByOrderId(String orderId) throws Exception {
        // 1.根据订单状态验证是否可以退款
        Integer orderStatus = purchaseDao.getOrderStatus(orderId); // 订单状态

        // 仅已取消（7）和已拒收（5）状态的订单可以退款，其他状态不可以退款
        boolean canRefund = null == orderStatus // 订单状态不存在
                || Constant.OrderStatusConfig.CANCEL_ORDER.equals(orderStatus) // 已取消（7）
                || Constant.OrderStatusConfig.REJECT.equals(orderStatus); // 已拒收（5）
        Map result = new HashMap();
        if (!canRefund) { // 已取消或已拒收不可以退款
            result.put("flag", false);
            result.put("message", "仅已取消和已拒收状态的订单可以退款，其他状态不可以退款");
            return result;
        }

        // 2.修改订单状态为退款，修改退款时间为当前时间
        Map params = new HashMap();
        params.put("orderId", orderId);
        params.put("orderStatus", Constant.OrderStatusConfig.REFUNDED); // 已退款
        Date now = new Date();
        params.put("drawbackTime", now); // 退款时间
        params.put("updatedAt", now); // 更新时间
        int count = purchaseDao.refundByOrderId(params); // 修改订单状态为退款，修改退款时间为当前时间
        if (count == 0) {
            result.put("flag", false);
            result.put("message", "退款失败（修改订单状态失败）");
            return result;
        }

        // 3.调用退款接口实现真正的退款
        // TODO: 2017/8/31 调用退款接口实现真正的退款,退款失败返回失败信息
        // 退款失败抛异常，事务回滚
        System.out.println("调用退款接口实现真正的退款----------------------成功");

        // 4.修改库存
        Map<BigInteger, BigDecimal> goodsInfo = new HashMap();
        List<PurchaseItemVo> purchaseItemVos = purchaseItemDao.getOrderDetailByOId(orderId);
        purchaseItemVos.forEach(item -> {
            goodsInfo.put(BigInteger.valueOf(item.getGoodsId()), BigDecimal.valueOf(item.getGoodsNumber()));
        });
        if (goodsInfo.size() == 0) {
            result.put("flag", false);
            result.put("message", "退款失败（退款订单不存在）");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 回滚
            return result;
        }
        count = supplierCommodityDao.updateInventoryByGoodsId(goodsInfo);
        if (count == 0) {
            result.put("flag", false);
            result.put("message", "退款失败（恢复库存失败）");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 回滚
            return result;
        }

        // 5.推送退款消息
        pushNotification(orderId, Constant.OrderStatusConfig.REFUNDED);

        result.put("flag", true);

        return result;
    }

    /**
     * 根据订单状态查询订单ID
     *
     * @param orderStatus 订单状态
     * @return String 订单状态
     * @throws Exception 异常
     */
    @Override
    public List<String> findOrderIdByOrderStatus(Integer orderStatus) throws Exception {
        List<String> orderIdList = purchaseDao.findOrderIdByOrderStatus(orderStatus);
        return orderIdList;
    }

    //图片处理
    public CommBlobUpload disposeImage(String imgStr) throws Exception {
        //base64转图片
        Map<String, String> map = MultipartFileUtil.generateImage(imgStr); // 失败抛出异常
        List<String> files = new ArrayList<>();
        files.add(map.get("name"));
        List<CommBlobUpload> blobUploadEntities = batchUploadPic(map.get("path"), files , 3);
        if (blobUploadEntities.size() != 1) {
            throw new Exception("上传图片到云端失败");
        }
        CommBlobUpload blobUpload = blobUploadEntities.get(0); // 云端返回的实体
        //删除本地图片
        System.gc();
        FileUtil.deleteDirectory(map.get("path"));
        return blobUpload;
    }
}