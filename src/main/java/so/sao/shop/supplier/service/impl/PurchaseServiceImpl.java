package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.config.StorageConfig;
import so.sao.shop.supplier.config.azure.AzureBlobService;
import so.sao.shop.supplier.dao.*;
import so.sao.shop.supplier.domain.*;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.*;
import so.sao.shop.supplier.pojo.output.CommodityOutput;
import so.sao.shop.supplier.pojo.output.OrderRefuseReasonOutput;
import so.sao.shop.supplier.pojo.output.PurchaseInfoOutput;
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
    @Transactional
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
                Account accountUser = purchaseDao.findAccountById(goodsId);
                if (null != accountUser) {
                    set.add(accountUser.getAccountId());
                }
            }
        }
        boolean flag = false;
        //b.循环商户ID生成订单
        List<Purchase> listPurchase = new ArrayList<>();
        List<PurchaseItem> listItem = new ArrayList<>();
        List<Notification> notificationList = new ArrayList<>();
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
                    item.setGoodsNumber(goodsNumber.intValue());//商品数量
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
            listPurchase.add(purchaseDate);
            /*
               1.将订单信息保存至订单表
               2.将订单详情保存只订单详情
               3.订单生成成功（返回订单ID，金额）
            */
            //给该供应商增加一条消息数据
            Notification notification = createNotification(sId, orderId, Constant.OrderStatusConfig.PAYMENT);
            notificationList.add(notification);
        }
        if (null != listPurchase && listPurchase.size() > 0 && null != listItem && listItem.size() > 0) {
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
     * @return PurchaseOutput
     * @throws Exception
     */
    @Override
    public PurchaseInfoOutput findById(String orderId) throws Exception {
        PurchaseInfoOutput purchaseInfoOutput = new PurchaseInfoOutput();
        PurchaseInfoVo purchaseInfoVo = new PurchaseInfoVo();
        Purchase purchase = purchaseDao.findById(orderId);
        if (purchase != null) {
            //PurchaseInfoOutput 添加订单信息
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
            //PurchaseInfoOutput 添加订单明细列表
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
            purchaseInfoOutput.setPurchaseInfoVo(purchaseInfoVo);
        }
        return purchaseInfoOutput;
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

        List<Purchase> purchaseList = new ArrayList<>();
        List<Purchase> orderList;
        List<Integer> pageNumList;

        if (pageNum != null && pageNum.length() > 0 && pageSize != null) { //获取区间页列表
            //java8新特性 逗号分隔字符串转List<Integer>
            pageNumList = Arrays.asList(pageNum.split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
            if (pageNumList.size() > 1) {
                Collections.sort(pageNumList);
                for (int i = pageNumList.get(0); i <= pageNumList.get(1); i++) {
                    PageHelper.startPage(i, pageSize);
                    orderList = purchaseDao.getOrderListByIds(purchaseSelectInput, accountId);
                    purchaseList.addAll(orderList);
                }
            } else { //获取当前页列表
                PageTool.startPage(pageNumList.get(0), pageSize);
                purchaseList = purchaseDao.getOrderListByIds(purchaseSelectInput, accountId);
            }

        } else { //查询全部列表
            purchaseList = purchaseDao.getOrderListByIds(purchaseSelectInput, accountId);
        }

        //向单元格里填充数据
        HSSFCell cellTemp = null;
        for (int i = 0; i < purchaseList.size(); i++) {
            Purchase purchase = purchaseList.get(i);
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
     * 根据支付ID获取订单状态列表
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
     * 根据订单查询订单打印页面信息
     * 1.查询订单信息
     * 2.查询商品条目
     * 3.将订单信息和商品条目封装到output对象
     * 4.在实体类属性的get方法中格式化数据
     *
     * @param orderId 订单编号
     * @return 封装结果 PurchaseItemPrintOutput
     */
    @Override
    public PurchaseItemPrintOutput getPrintItems(String orderId) {
        // 1.查询出订单信息（客户、客户电话、收货地址、下单时间、合计）
        PurchasePrintVo purchasePrintVo = purchaseDao.findPrintOrderInfo(orderId);

        // 2.查询订单的商品条目
        List<PurchaseItemPrintVo> purchaseItemPrintVos = purchaseItemDao.findPrintItems(orderId);

        if (null == purchasePrintVo) {
            return null;
        }

        // 3.封装返回对象
        PurchaseItemPrintOutput output = new PurchaseItemPrintOutput(); //返回对象
        output = BeanMapper.map(purchasePrintVo, output.getClass()); // 将purchasePrintVo复制到output

        // 添加商品条目
        output.setPurchaseItemPrintVos(purchaseItemPrintVos);

        return output;
    }

    /**
     * 根据订单编号生成收货二维码
     * 如果订单已经存在二维码返回false，生成二维码失败返回false
     * <p>
     * 1.验证订单并判断订单id是否存在关联的二维码
     * 1.1．存在返回false(一个订单仅对应一个二维码)
     * 1.2．不存在执行步骤2
     * 2.生成二维码图片
     * 2.1．拼接二维码内容
     * 2.2. 生成二维码图片
     * 2.3.将二维码图片上传到云端
     * 2.4.将二维码信息保存到数据库
     * 2.5.删除本地图片
     *
     * @param orderId 订单编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createReceivingQrcode(String orderId) throws Exception {
        // 1.验证订单并判断订单id是否存在关联的二维码
        Purchase purchase = purchaseDao.findById(orderId);
        // 1.1.订单不存在
        if (null == purchase) {
            throw new Exception("订单不存在");
        }

        List<Qrcode> qrcodeList = qrcodeDao.findQrcodeByOrderId(orderId);
        // 1.2.该订单已经存在二维码
        if (null != qrcodeList && qrcodeList.size() > 0) {
            throw new Exception("该订单已经存在二维码");
        }


        // 2.生成二维码图片
        // 2.1拼接二维码内容
        String qrcodeId = NumberGenerate.generateId();
        String content = receiveUrl + "?orderId=" + orderId;

        // 2.2生成二维码图片
        String path = "/qrcode";
        String name = System.currentTimeMillis() + ".png";
        boolean b = FileUtil.createDir(path);
        String img = path + "/" + name; // 二维码相对地址
        // 调用二维码工具类CodeUtil生成图片
        boolean flag = qrcodeService.createQrCode(new FileOutputStream(new File(img)), content, 230, "png");
        if (!flag) { // 失败
            throw new Exception("生成二维码图片失败");
        }

        // 2.3将二维码图片上传到云端
        FileUtil fileUtil = new FileUtil();
        List files = new ArrayList();
        files.add(name);
        List<Result> uploadResult = azureBlobService.uploadFilesComm(path, files);

        if (uploadResult.size() != 1) {
            throw new Exception("将二维码图片上传到云端失败");
        }
        Result result = uploadResult.get(0);
        if (null == result || result.getCode() != Constant.CodeConfig.CODE_SUCCESS) {
            throw new Exception("将二维码图片上传到云端失败");
        }
        List<CommBlobUpload> blobUploadEntities = (List<CommBlobUpload>) result.getData();
        if (blobUploadEntities.size() != 1) {
            throw new Exception("将二维码图片上传到云端失败");
        }
        CommBlobUpload blobUpload = blobUploadEntities.get(0);
        String url = blobUpload.getUrl(); // 云端的二维码地址

        // 2.4将二维码信息保存到数据库
        Qrcode qrcode = new Qrcode();
        qrcode.setQrcodeId(qrcodeId); // 二维码id
        qrcode.setForeignKey(orderId); // 二维码关联的外键
        qrcode.setContent(content); // 二维码内容
        qrcode.setStatus(0); // 二维码状态：0-未失效 1-失效
        qrcode.setCreatedAt(new Date()); // 创建时间
        qrcode.setUrl(url); // 二维码图片地址
        qrcode.setWidth(230d); // 二维码宽度
        qrcode.setHeight(230d); // 二维码高度

        qrcodeDao.save(qrcode);

        // 2.5删除本地图片
        File file = new File(img);
        if (file.exists() && file.isFile()) {
            System.gc();
            file.delete();
        }
    }

    /**
     * 根据支付id，批量生成订单对应的收货二维码
     *
     * @param payId 支付id
     * @throws Exception 异常
     */
    @Transactional(rollbackFor = Exception.class)
    public void createReceivingQrcodeByPayId(String payId) throws Exception {
        List<Purchase> purchaseList = purchaseDao.findByPayId(payId); // 根据支付id查询订单列表
        // 根据支付id查询对应订单是否已经存在二维码
        int count = qrcodeDao.getQrcodesByPayId(payId);
        if (count > 0) { // 订单存在二维码
            throw new Exception("一个订单只能生成一个二维码");
        }
        List<String> qrcodePics = new ArrayList<>(); // 存储二维码图片
        List<Qrcode> qrcodeList = new ArrayList<>(); // 存储二维码实体
        String path = "/qrcode"; // 生成二维码的本地路径
        Boolean flag = true; // 批量生成二维码-成功
        if (null != purchaseList && purchaseList.size() > 0) {
            for (int i = 0; i < purchaseList.size(); i++) {
                String orderId = purchaseList.get(i).getOrderId(); // 订单编号
                String qrcodeId = NumberGenerate.generateId(); // 二维码id
                String content = receiveUrl + "?orderId=" + orderId; // 二维码内容
                String name = System.currentTimeMillis() + ".png"; // 二维码临时名称
                boolean b = FileUtil.createDir(path); // 二维码临时路径
                String img = path + "/" + name; // 二维码相对地址
                // 生成二维码图片
                flag = qrcodeService.createQrCode(new FileOutputStream(new File(img)), content, 230, "png");
                if (!flag) { // 失败
                    throw new Exception("生成二维码失败");
                }
                qrcodePics.add(name);
                Qrcode qrcode = new Qrcode();
                qrcode.setQrcodeId(qrcodeId); // 二维码id
                qrcode.setForeignKey(orderId); // 二维码关联的外键
                qrcode.setContent(content); // 二维码内容
                qrcode.setStatus(0); // 二维码状态：0-未失效 1-失效
                qrcode.setCreatedAt(new Date()); // 创建时间
//                qrcode.setUrl(url); // 二维码图片地址 // 上传到云端后返回图片url
                qrcode.setWidth(230d); // 二维码宽度
                qrcode.setHeight(230d); // 二维码高度

                qrcodeList.add(qrcode);
            }
        }


        // 批量上传到云端
        FileUtil fileUtil = new FileUtil();
        List<Result> uploadResult = azureBlobService.uploadFilesComm(path, qrcodePics);

        if (uploadResult.size() == 1) {
            Result result = uploadResult.get(0);
            flag = Constant.CodeConfig.CODE_SUCCESS.equals(result.getCode());
            if (!flag) { // 上传失败
                throw new Exception("上传失败");
            }
            List<CommBlobUpload> blobUploadEntities = (List<CommBlobUpload>) result.getData();
            if (blobUploadEntities.size() == qrcodeList.size()) {
                for (int i = 0; i < qrcodeList.size(); i++) {
                    CommBlobUpload blobUpload = blobUploadEntities.get(i);
                    qrcodeList.get(i).setUrl(blobUpload.getUrl()); // 云端的二维码地址
                }
            }
        }

        // 批量插入数据库
        qrcodeDao.saveQrcodes(qrcodeList);
    }

    /**
     * 扫描收货二维码
     * <p>
     * 1.验证是否可以扫码收货
     * 2.将订单状态改为已收货
     * 3.将二维码状态改为失效，并记录失效时间
     *
     * @param orderId 订单编号
     * @return Map类型  成功flag返回true;
     * 失败flag返回false,message返回失败信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map sweepReceiving(String orderId) throws Exception {
        // 1.验证是否可以扫码收货
        PurchasePrintVo purchasePrintVo = purchaseDao.findPrintOrderInfo(orderId); // 查询订单和二维码信息
        Map map = new HashMap<>(); // 返回结果
        if (null == purchasePrintVo || null == purchasePrintVo.getQrcodeStatus()
                || purchasePrintVo.getQrcodeStatus().equals(1)) { // 订单为null或二维码状态失效（1）
            map.put("flag", false);
            map.put("message", "订单不存在或二维码已经失效");
            return map;
        }
        // 2.将订单状态改为已收货
        purchaseDao.updateOrder(orderId, Constant.OrderStatusConfig.RECEIVED, new Date());
        // 3.将二维码状态改为失效，并记录失效时间
        Date date = new Date();
        qrcodeDao.updateStatus(orderId, 1, date, date);
        map.put("flag", true);
        //TODO 订单拒收成功给该供应商推送一条消息
        pushNotification(orderId, Constant.OrderStatusConfig.RECEIVED);
        return map;
    }

    /**
     * 添加拒收货信息
     * <p>
     * 将拒收理由及相关图片保存
     *
     * @param refuseOrderInput 封装了订单编号，拒收理由，拒收图片
     * @return Map 封装结果 键flag的值为true表示成功，false表示失败，message的值表示文字描述
     */
    @Override
    public void refuseOrder(RefuseOrderInput refuseOrderInput) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", refuseOrderInput.getOrderId());//订单编号
        map.put("refuseReason", refuseOrderInput.getRefuseReason());//拒收理由
        map.put("refuseImgUrlA", refuseOrderInput.getRefuseImgUrlA());//拒收图片URL A
        map.put("refuseImgUrlB", refuseOrderInput.getRefuseImgUrlB());//拒收图片URL B
        map.put("refuseImgUrlC", refuseOrderInput.getRefuseImgUrlC());//拒收图片URL C
        map.put("orderRefuseTime", new Date());//拒收时间
        map.put("updatedAt", new Date());//更新时间
        map.put("orderStatus", Constant.OrderStatusConfig.REJECT);//订单状态 5 拒收
        purchaseDao.insertRefuseMessage(map);
        //TODO 订单拒收成功给该供应商推送一条消息
        pushNotification(refuseOrderInput.getOrderId(), Constant.OrderStatusConfig.REJECT);
    }

    /**
     * 根据订单ID获取该订单的拒收原因
     *
     * @param orderId 订单ID
     * @return map 封装了所有订单拒收原因信息
     */
    @Override
    public Map<String, Object> searchRefuseReasonByOrderId(String orderId) throws Exception {
        OrderRefuseReasonOutput orderRefuseReasonOutput = purchaseDao.findRefuseReasonByOrderId(orderId);
        Map<String, Object> map = new HashMap<>();
        map.put("refuseReason", Constant.MessageConfig.MSG_NO_DATA);
        map.put("refuseImgUrl", null);
        List<String> urlList = new ArrayList<>();
        if (null != orderRefuseReasonOutput) {
            urlList.add(orderRefuseReasonOutput.getRefuseImgUrlA());
            urlList.add(orderRefuseReasonOutput.getRefuseImgUrlB());
            urlList.add(orderRefuseReasonOutput.getRefuseImgUrlC());
            map.put("refuseReason", orderRefuseReasonOutput.getRefuseReason());
            map.put("refuseImgUrl", urlList);
        }
        return map;
    }

    /**
     * 封装出参的方法
     *
     * @param code   code码
     * @param result 出参类
     * @param info   分页工具
     * @return 出参
     */
    private Result getOutPut(Integer code, Result<PageInfo> result, PageInfo info) {
        result.setCode(code);
        result.setData(info);
        switch (code) {
            case 1:
                result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
                break;
            case 0:
                result.setMessage(Constant.MessageConfig.MSG_FAILURE);
                break;
            case 4:
                result.setMessage(Constant.MessageConfig.MSG_NOT_FOUND_RESULT);
                break;
            case 7:
                result.setMessage(Constant.MessageConfig.DateNOTLate);
                break;
            case 8:
                result.setMessage(Constant.MessageConfig.MoneyNOTLate);
                break;
            case 102:
                result.setMessage(Constant.NoExistMessageConfig.NOSTORE);
                break;
            default:
                result.setCode(Constant.CodeConfig.CODE_FAILURE);
                result.setMessage(Constant.MessageConfig.MSG_FAILURE);
        }
        return result;
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
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", cancelReasonInput.getOrderId());//订单编号
        map.put("cancelReason", cancelReasonInput.getCancelReason());//取消订单理由
        map.put("updatedAt", new Date());//更新时间
        map.put("orderStatus", Constant.OrderStatusConfig.CANCEL_ORDER);//订单状态 7 取消
        purchaseDao.insertCancelMessage(map);
        //TODO 订单取消成功给该供应商推送一条消息
        pushNotification(cancelReasonInput.getOrderId(), Constant.OrderStatusConfig.CANCEL_ORDER);
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
     * 根据订单编号调用退款接口退款并修改订单状态
     *
     * @param orderId 订单编号
     * @return 返回Map：flag：true|false,message:信息
     * @throws Exception 异常
     */
    @Transactional(rollbackFor = Exception.class)
    public Map refundByOrderId(String orderId) throws Exception {
        // TODO: 2017/8/31 调用退款接口实现真正的退款,退款失败返回失败信息
        System.out.println("调用退款接口实现真正的退款----------------------成功");

        // 根据订单状态验证是否可以退款
        Integer orderStatus = purchaseDao.getOrderStatus(orderId); // 订单状态

        // 仅已取消（7）和已拒收（5）状态的订单可以退款，其他状态不可以退款
        boolean canRefund = Constant.OrderStatusConfig.CANCEL_ORDER.equals(orderStatus)
                || Constant.OrderStatusConfig.REJECT.equals(orderStatus); // 是否可以退款
        Map result = new HashMap();
        if (!canRefund) { // 已取消或已拒收
            result.put("flag", false);
            result.put("message", "仅已取消和已拒收状态的订单可以退款，其他状态不可以退款");
            return result;
        }

        // 修改订单状态为退款，修改退款时间为当前时间
        Map params = new HashMap();
        params.put("orderId", orderId);
        params.put("orderStatus", Constant.OrderStatusConfig.REFUNDED); // 已退款
        Date now = new Date();
        params.put("drawbackTime", now); // 退款时间
        params.put("updatedAt", now); // 更新时间
        int count = purchaseDao.refundByOrderId(params);
        if (count == 0) {
            result.put("flag", false);
            result.put("message", "退款失败");
            return result;
        }

        result.put("flag", true);
        result.put("message", "退款成功");

        // TODO 订单退款成功给该供应商推送一条消息
        pushNotification(orderId, Constant.OrderStatusConfig.REFUNDED);

        return result;
    }
}