package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.AccountDao;
import so.sao.shop.supplier.dao.PurchaseDao;
import so.sao.shop.supplier.dao.PurchaseItemDao;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.domain.Purchase;
import so.sao.shop.supplier.domain.PurchaseItem;
import so.sao.shop.supplier.pojo.input.AccountPurchaseInput;
import so.sao.shop.supplier.pojo.input.PurchaseInput;
import so.sao.shop.supplier.pojo.input.PurchaseSelectInput;
import so.sao.shop.supplier.pojo.output.*;
import so.sao.shop.supplier.pojo.vo.AccountPurchaseVo;
import so.sao.shop.supplier.pojo.vo.PurchaseInfoVo;
import so.sao.shop.supplier.pojo.vo.PurchaseItemVo;
import so.sao.shop.supplier.pojo.vo.PurchasesVo;
import so.sao.shop.supplier.service.CommodityService;
import so.sao.shop.supplier.service.PurchaseService;
import so.sao.shop.supplier.util.NumberGenerate;
import so.sao.shop.supplier.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    /**
     * 保存订单信息
     * @param purchase 订单对象
     * @return
     */
    @Override
    @Transactional
    public Map<String,Object> savePurchase(PurchaseInput purchase) throws Exception{
        Map<String,Object> output = new HashMap<>();
        output.put("status", Constant.CodeConfig.CODE_FAILURE);
        /*
            1.根据商户进行拆单
                a.根据商品查出所有商户信息。
                b.循环商户ID生成订单
            2.循环订单详情信息 生成批量插入订单详情数据，统计订单总价
            3.生成订单数据
         */
        List<PurchaseItemVo> listPurchaseItem = purchase.getListPurchaseItem();//订单详情
        Set<Long> set = new HashSet<>();
        for (PurchaseItemVo purchaseItem : listPurchaseItem) {
            //a.根据商品查出所有商户信息。
            Long goodsId = purchaseItem.getGoodsId();//商品ID
            if(null != goodsId){
                Account accountUser = purchaseDao.findAccountById(goodsId);
                if(null != accountUser){
                    set.add(accountUser.getAccountId());
                }
            }
        }
        boolean flag = false;
        int counter = 1;
        //b.循环商户ID生成订单
        for (Long sId : set){
            List<Purchase> listPurchase = new ArrayList<>();
            //生成订单编号
            String orderId = NumberGenerate.generateUuid();
            List<PurchaseItem> listItem = new ArrayList<>();
            BigDecimal totalMoney = new BigDecimal(0);//订单总价计算
            for (PurchaseItemVo purchaseItem : listPurchaseItem) {
                //根据商品ID查询商户ID
                Long goodsId = purchaseItem.getGoodsId();//商品ID
                Account accountUser = purchaseDao.findAccountById(goodsId);
                //判断当前商品是否属于该商户
                if(null != accountUser && sId.equals(accountUser.getAccountId())){
                    BigDecimal goodsNumber = new BigDecimal(purchaseItem.getGoodsNumber());//商品数量
                    String goodsAttribute = purchaseItem.getGoodsAttribute();//商品属性
                    //查询商品信息
                    CommodityOutput commOutput = commodityService.getCommodity(goodsId);
                    //2.生成批量插入订单详情数据
                    PurchaseItem item = new PurchaseItem();
                    item.setGoodsAttribute(goodsAttribute);//商品属性
                    item.setGoodsId(goodsId);//商品ID
                    item.setGoodsNumber(goodsNumber.intValue());//商品数量
                    Double unitPrice = commOutput.getUnitPrice();//商品售价
                    item.setGoodsUnitPrice(new BigDecimal(unitPrice));
                    totalMoney = totalMoney.add(goodsNumber.multiply(new BigDecimal(unitPrice)));//订单总价计算
                    item.setGoodsTatolPrice(goodsNumber.multiply(new BigDecimal(unitPrice)));//单个商品总价
                    item.setGoodsImage(commOutput.getMinImg());//商品图片
                    item.setGoodsName(commOutput.getName());//商品名称
                    item.setBrandName(commOutput.getBrand());//品牌
                    item.setOrderId(orderId);//订单ID
                    listItem.add(item);
                }
            }
            //3.生成订单数据
            Long userId = purchase.getUserId();//买家ID
            String orderReceiverName = purchase.getOrderReceiverName();//收货人姓名
            String orderReceiverMobile = purchase.getOrderReceiverMobile();//收货人电话
            String orderAddress = purchase.getOrderAddress();//收货人地址
            Integer orderPaymentMethod = purchase.getOrderPaymentMethod();//支付方式
            Purchase purchaseDate = new Purchase();
            purchaseDate.setOrderId(orderId);//订单ID
            purchaseDate.setStoreId(sId);//商户ID
            purchaseDate.setUserId(userId);//买家ID
            purchaseDate.setOrderReceiverName(orderReceiverName);//收货人姓名
            purchaseDate.setOrderReceiverMobile(orderReceiverMobile);//收货人电话
            purchaseDate.setOrderAddress(orderAddress);//收货人地址
            purchaseDate.setOrderPaymentMethod(orderPaymentMethod);//支付方式
            purchaseDate.setOrderPrice(totalMoney);//订单总金额
            purchaseDate.setOrderCreateTime(new Date());//下单时间
            purchaseDate.setOrderStatus(Constant.OrderStatusConfig.PAYMENT);//订单状态 1待付款2代发货3已发货4已收货5已拒收6已退款
            purchaseDate.setUpdatedAt(new Date());//更新时间
            listPurchase.add(purchaseDate);
             /*
            1.将订单信息保存至订单表
            2.将订单详情保存只订单详情
            3.订单生成成功（返回订单ID，金额）
         */
            int result = purchaseDao.savePurchase(listPurchase);
            int resultSum = purchaseItemDao.savePurchaseItem(listItem);
            if(result > 0 && resultSum > 0 && counter == set.size()){
                flag = true;
            }
            counter ++;
        }
        if (flag) {
            output.put("status", Constant.CodeConfig.CODE_SUCCESS);
        }
        return output;
    }

    /**
     * 根据订单ID获取订单详情.
     * @param orderId orderId
     * @return PurchaseOutput
     * @throws Exception
     */
    @Override
    public PurchaseInfoOutput findById(String orderId) throws Exception {
        PurchaseInfoOutput purchaseInfoOutput = new PurchaseInfoOutput();
        PurchaseInfoVo purchaseInfoVo = new PurchaseInfoVo();
        Purchase purchase = purchaseDao.findById(orderId);
        if(purchase != null) {
            //PurchaseInfoOutput 添加订单信息
            purchaseInfoVo.setOrderId(purchase.getOrderId());
            purchaseInfoVo.setOrderReceiverName(purchase.getOrderReceiverName());
            purchaseInfoVo.setOrderReceiverMobile(purchase.getOrderReceiverMobile());
            purchaseInfoVo.setOrderCreateTime(purchase.getOrderCreateTime());
            purchaseInfoVo.setOrderPaymentMethod(purchase.getOrderPaymentMethod());
            purchaseInfoVo.setOrderPaymentNum(purchase.getOrderPaymentNum());
            purchaseInfoVo.setOrderPaymentTime(purchase.getOrderPaymentTime());
            purchaseInfoVo.setOrderPrice(purchase.getOrderPrice());
            purchaseInfoVo.setOrderStatus(purchase.getOrderStatus().shortValue());
            purchaseInfoVo.setOrderShipMethod(purchase.getOrderShipMethod());
            purchaseInfoVo.setOrderShipmentNumber(purchase.getOrderShipmentNumber());
            purchaseInfoVo.setLogisticsCompany(purchase.getLogisticsCompany());
            purchaseInfoVo.setDistributorName(purchase.getDistributorName());
            purchaseInfoVo.setDistributorMobile(purchase.getDistributorMobile());
            purchaseInfoVo.setDrawbackTime(purchase.getDrawbackTime());

            //PurchaseInfoOutput 添加订单明细列表
            List<PurchaseItemVo> purchaseItemVoList = purchaseItemDao.getOrderDetailByOId(purchase.getOrderId());

            if(purchaseItemVoList != null && purchaseItemVoList.size() > 0) {
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
    public PurchaseSelectOutput searchOrders(Integer pageNum, Integer rows, PurchaseSelectInput purchaseSelectInput) throws Exception{
        PageHelper.startPage(pageNum, rows);
        List<PurchasesVo> orderList = purchaseDao.findPage(purchaseSelectInput);
        if (orderList.size() > 0) {
            PurchaseSelectOutput purchaseSelectOutput = new PurchaseSelectOutput();
            purchaseSelectOutput.setPageInfo(new PageInfo<>(orderList));
            return purchaseSelectOutput;
        } else {
            PurchaseSelectOutput purchaseSelectOutput = new PurchaseSelectOutput();
            purchaseSelectOutput.setPageInfo(new PageInfo<>());
            return purchaseSelectOutput;
        }
    }

    /**
     * 更改订单状态
     *
     * @param orderId
     * @param orderStatus
     * @return boolean
     */
    @Override
    @Transactional
    public boolean updateOrder(String orderId, Integer orderStatus,Integer receiveMethod,String name,String number) {
        if(orderStatus==Constant.OrderStatusConfig.REFUNDED){
            Date drawbackDate = new Date();
            purchaseDao.updateOrderAtr(orderId,drawbackDate ,null,null,null);
        }else if(orderStatus==Constant.OrderStatusConfig.ISSUE_SHIP){
            purchaseDao.updateOrderAtr(orderId,null,receiveMethod,name,number);
        }
        Date updateDate = new Date();
        return purchaseDao.updateOrder(orderId, orderStatus,updateDate);
    }

    /**
     * 批量删除订单
     *
     * @param orderIds
     * @return
     */
    @Override
    public boolean deletePurchase(String orderIds) {
        String[] orderIdArr = orderIds.split(",");
        return purchaseDao.deleteByOrderId(orderIdArr);
    }

    /**
     * POI导出(当前页/所选页/全部)订单列表
     * @param request request
     * @param response response
     * @param pageNum pageNum
     * @param accountId accountId
     * @param pageSize pageSize
     */
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, String pageNum, Integer pageSize, Long accountId) throws Exception{

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

        if(pageNum != null && pageNum.length() > 0 && pageSize != null){ //获取区间页列表
            //java8新特性 逗号分隔字符串转List<Integer>
            pageNumList = Arrays.asList(pageNum.split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
            if(pageNumList.size() > 1){
                Collections.sort(pageNumList);
                for(int i = pageNumList.get(0); i<=pageNumList.get(1); i++){
                    PageHelper.startPage(i, pageSize);
                    orderList = purchaseDao.getOrderListByIds(accountId);
                    purchaseList.addAll(orderList);
                }
            } else { //获取当前页列表
                PageHelper.startPage(pageNumList.get(0), pageSize);
                purchaseList = purchaseDao.getOrderListByIds(accountId);
            }

        } else { //查询全部列表
            purchaseList = purchaseDao.getOrderListByIds(accountId);
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
            if(status == Constant.OrderStatusConfig.PAYMENT){
                orderStatus = Constant.OrderMessageConfig.PAYMENT;
            } else if(status == Constant.OrderStatusConfig.PENDING_SHIP) {
                orderStatus = Constant.OrderMessageConfig.PENDING_SHIP;
            } else if(status == Constant.OrderStatusConfig.ISSUE_SHIP) {
                orderStatus = Constant.OrderMessageConfig.ISSUE_SHIP;
            } else if(status == Constant.OrderStatusConfig.RECEIVED) {
                orderStatus = Constant.OrderMessageConfig.RECEIVED;
            } else if(status == Constant.OrderStatusConfig.REJECT) {
                orderStatus = Constant.OrderMessageConfig.REJECT;
            } else if(status == Constant.OrderStatusConfig.REFUNDED) {
                orderStatus = Constant.OrderMessageConfig.REFUNDED;
            } else if(status == Constant.OrderStatusConfig.COMPLETE) {
                orderStatus = Constant.OrderMessageConfig.COMPLETE;
            }
            cellTemp = row.createCell(3);
            cellTemp.setCellValue(orderStatus);
            cellTemp.setCellStyle(style);
            cellTemp = row.createCell(4);
            cellTemp.setCellValue(purchase.getOrderPrice()+"￥");
            cellTemp.setCellStyle(style);
            cellTemp = row.createCell(5);
            cellTemp.setCellValue(purchase.getOrderCreateTime() == null ? "" : StringUtil.fomateData(purchase.getOrderCreateTime(),"yyyy-MM-dd HH:mm:ss"));
            cellTemp.setCellStyle(style);
            Integer paymentMethod = purchase.getOrderPaymentMethod();
            String payment = "";
            if(paymentMethod == Constant.PaymentStatusConfig.ALIPAY){
                payment = Constant.PaymentMsgConfig.ALIPAY;
            } else if(paymentMethod == Constant.PaymentStatusConfig.WECHAT){
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
     * @param storeId
     * @return SumIncome
     */
    @Override
    public SumIncome findOrderStatus(Long storeId) {
        SumIncome sumIncome =null;
        //查询此商户ID有没有在数据库中，count=0则没有
        int count  = purchaseDao.countByStoreId(storeId);
        if (count==0){
            sumIncome =new SumIncome();
            sumIncome.setIncome(BigDecimal.valueOf(count));
            sumIncome.setCode(Constant.NoExistCodeConfig.NOSTORE);
            sumIncome.setMessage(Constant.NoExistMessageConfig.NOSTORE);
            return sumIncome;
        }
        //商户ID存在，且不为空时将计算的总金额赋值给历史总金额字段
        Double account = purchaseDao.findOrderStatus(storeId);
        if(account != null) {

            accountDao.updateUserPrice(BigDecimal.valueOf(account),storeId);
            sumIncome =new SumIncome();
            sumIncome.setIncome(BigDecimal.valueOf(account));
            sumIncome.setCode(Constant.CodeConfig.CODE_SUCCESS);
            sumIncome.setMessage(Constant.MessageConfig.MSG_SUCCESS);
        }else {
            sumIncome = new SumIncome();
            sumIncome.setIncome(BigDecimal.valueOf(0));
            sumIncome.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
            sumIncome.setMessage(Constant.MessageConfig.MSG_SUCCESS);
        }
        return  sumIncome;
    }

    /**
     * 根据商家编号及查询条件（起始-结束时间；起始-结束金额范围）查找所有相关订单记录(分页)
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @param input 查询条件封装类
     * @param storeId 商家编号
     * @return output出参
     */
    @Override
    public RecordToPurchaseOutput searchPurchases(Integer pageNum, Integer pageSize, AccountPurchaseInput input, Long storeId) throws ParseException {
        /**
         * 1、判断参数是否为空
         *     1.1、判断 pageNum当前页码、pageSize是否为空，若为空给默认值
         * 2、设置分页，访问持久化层#findfindPageByStoreId方法查询
         * 3、判断返回结果及是否为空：
         *     3.1、不为空时出参中封装分页信息（当前页码，总页码，总条数，结果集）、“成功”的code码和message
         *          3.1.1、将结果集中的泛型（domain类）转化成vo类
         *     3.2、为空时在出参对象中封装“未查询到结果集”的code码和message
         *
         */
        RecordToPurchaseOutput output = new RecordToPurchaseOutput();//返回对象
        //1.1、判断当前页码和每页显示条数是否为空
        if (null==pageNum){
            pageNum = 1;
        }
        if (null==pageSize){
            pageSize = 10;
        }

        //2.1、分页
        PageHelper.startPage(pageNum,pageSize);
        //判断input是否为空。不为空将字符串的时间转化成Date
        if (null !=input){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (null !=input.getBeginTime()){
                input.setBeginDate(sdf.parse(input.getBeginTime()));
            }
            if (null != input.getEndTime()){
                input.setEndDate(sdf.parse(input.getEndTime()));
            }
        }

        //2.2、访问持久化层，获取数据
        List<Purchase> purchaseList = purchaseDao.findPageByStoreId(input,storeId);

        //3、判断返回结果及是否为空：
        //不为空时
        if(null != purchaseList && !purchaseList.isEmpty()){
            PageInfo<Purchase> pageInfo = new PageInfo<>(purchaseList);

            //3.1.1、将结果集中的泛型（domain类）转化成vo类
            List<AccountPurchaseVo> purchaseVos = this.inversion(purchaseList);

            //3.1.2、出参中封装分页信息（当前页码，总页码，总条数，结果集）、“成功”的code码和message
            PageInfo<AccountPurchaseVo> pageInfoVo = new PageInfo<>();
            pageInfoVo.setPageNum(pageNum);
            pageInfoVo.setPageSize(pageSize);
            pageInfoVo.setTotal(pageInfo.getTotal());
            pageInfoVo.setPages(pageInfo.getPages());
            pageInfoVo.setSize(pageInfo.getSize());
            pageInfoVo.setList(purchaseVos);

            output.setCode(Constant.CodeConfig.CODE_SUCCESS);
            output.setMessage(Constant.MessageConfig.MSG_SUCCESS);
            output.setPageInfo(pageInfoVo);
        } else {//为空时
            // 3.2、在出参对象中封装“未查询到结果集”的code码和message
            output.setCode(Constant.CodeConfig.CODE_NOT_FOUND_RESULT);
            output.setMessage(Constant.MessageConfig.MSG_NOT_FOUND_RESULT);
            output.setPageInfo(null);
        }
        return output;
    }

    /**
     * 转化集合中的泛型
     * @param purchaseList 转化前的集合
     * @return 转化后的集合
     */
    private List<AccountPurchaseVo> inversion(List<Purchase> purchaseList) {
        List<AccountPurchaseVo> purchaseVos = new ArrayList<>();//转化后的集合
        AccountPurchaseVo purchaseVo =null;//转化后的对象
        for (Purchase purchase:purchaseList) {
            //循环转化数据
            purchaseVo = new AccountPurchaseVo();
            purchaseVo.setOrderId(purchase.getOrderId());//订单编号
            purchaseVo.setOrderPaymentMethod(purchase.getOrderPaymentMethod());//支付方式
            purchaseVo.setOrderPrice(purchase.getOrderPrice());//该次订单总价
            purchaseVo.setOrderReceiverMobile(purchase.getOrderReceiverMobile());//收货人联系方式
            purchaseVo.setOrderReceiverName(purchase.getOrderReceiverName());//收货人姓名
            purchaseVo.setOrderStatus(purchase.getOrderStatus());//订单状态
            purchaseVos.add(purchaseVo);//将转化后的数据添加到集合中
        }
        return purchaseVos;
    }

    @Override
    public Integer findOrderStatus(String orderId) {
        return purchaseDao.getOrderStatus(orderId);
    }

}