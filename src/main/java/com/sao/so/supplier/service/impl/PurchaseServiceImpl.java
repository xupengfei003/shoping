package com.sao.so.supplier.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sao.so.supplier.config.Constant;
import com.sao.so.supplier.dao.AccountDao;
import com.sao.so.supplier.dao.PurchaseDao;
import com.sao.so.supplier.dao.PurchaseItemDao;
import com.sao.so.supplier.domain.AccountUser;
import com.sao.so.supplier.domain.Purchase;
import com.sao.so.supplier.domain.PurchaseItem;
import com.sao.so.supplier.pojo.input.AccountPurchaseInput;
import com.sao.so.supplier.pojo.input.PurchaseInput;
import com.sao.so.supplier.pojo.input.PurchaseSelectInput;
import com.sao.so.supplier.pojo.output.*;
import com.sao.so.supplier.pojo.vo.AccountPurchaseVo;
import com.sao.so.supplier.pojo.vo.PurchaseItemVo;
import com.sao.so.supplier.pojo.vo.PurchasesVo;
import com.sao.so.supplier.service.CommodityService;
import com.sao.so.supplier.service.PurchaseService;
import com.sao.so.supplier.util.NumberGenerate;
import com.sao.so.supplier.util.StringUtil;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

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
    private AccountDao  accountDao;

    /**
     * 保存订单信息
     * @param purchase 订单对象
     * @return
     */
    @Override
    @Transactional
    public Map<String,Object> savePurchase(PurchaseInput purchase) throws Exception{
        Map<String,Object> output = new HashMap<>();
        /*
            1.根据商户进行拆单
                a.根据商品查出所有商户信息。
                b.循环商户ID生成订单
            2.循环订单详情信息 生成批量插入订单详情数据，统计订单总价
            3.生成订单数据
         */
        List<PurchaseItemVo> listPurchaseItem = purchase.getListPurchaseItem();//订单详情
        //生成订单编号
        String orderId = NumberGenerate.generateUuid();
        Set<Long> set = new HashSet<>();
        for (PurchaseItemVo purchaseItem : listPurchaseItem) {
            //a.根据商品查出所有商户信息。
            Long goodsId = purchaseItem.getGoodsId();//商品ID
            AccountUser accountUser = purchaseDao.findAccountById(goodsId);
            set.add(accountUser.getUserId());
        }
        //b.循环商户ID生成订单
        List<Purchase> listPurchase = new ArrayList<>();
        for (Long struId : set){
            List<PurchaseItem> listItem = new ArrayList<>();
            BigDecimal totalMoney = new BigDecimal(0);//订单总价计算
            for (PurchaseItemVo purchaseItem : listPurchaseItem) {
                //根据商品ID查询商户ID
                Long goodsId = purchaseItem.getGoodsId();//商品ID
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
            //3.生成订单数据
            Long userId = purchase.getUserId();//买家ID
            String orderReceiverName = purchase.getOrderReceiverName();//收货人姓名
            String orderReceiverMobile = purchase.getOrderReceiverMobile();//收货人电话
            String orderAddress = purchase.getOrderAddress();//收货人地址
            Integer orderPaymentMethod = purchase.getOrderPaymentMethod();//支付方式
            Purchase purchaseDate = new Purchase();
            purchaseDate.setOrderId(orderId);//订单ID
            purchaseDate.setStoreId(struId);//商户ID
            purchaseDate.setUserId(userId);//买家ID
            purchaseDate.setOrderReceiverName(orderReceiverName);//收货人姓名
            purchaseDate.setOrderReceiverMobile(orderReceiverMobile);//收货人电话
            purchaseDate.setOrderAddress(orderAddress);//收货人地址
            purchaseDate.setOrderPaymentMethod(orderPaymentMethod);//支付方式
            purchaseDate.setOrderPrice(totalMoney);//订单总金额
            purchaseDate.setOrderCreateTime(System.currentTimeMillis());//下单时间
            purchaseDate.setOrderStatus(Constant.OrderStatusConfig.RECEIVED);//订单状态 1待付款2代发货3已发货4已收货5已拒收6已退款
            listPurchase.add(purchaseDate);
             /*
            1.将订单信息保存至订单表
            2.将订单详情保存只订单详情
            3.订单生成成功（返回订单ID，金额）
         */
            int result = purchaseDao.savePurchase(listPurchase);
            int resultSum = purchaseItemDao.savePurchaseItem(listItem);
        }
        output.put("status", Constant.CodeConfig.CODE_SUCCESS);
        return output;
    }

    /**
     * 根据订单ID获取订单详情.
     * @param orderId orderId
     * @return PurchaseOutput
     * @throws Exception
     */
    @Override
    public PurchaseInfoOutput findById(BigInteger orderId) throws Exception {
        PurchaseInfoOutput purchaseInfoOutput = new PurchaseInfoOutput();
        Purchase purchase = purchaseDao.findById(Long.parseLong(orderId.toString()));
        if(purchase != null) {
            //PurchaseInfoOutput 添加订单信息
            purchaseInfoOutput.setOrderId(Long.parseLong(purchase.getOrderId()));
            purchaseInfoOutput.setOrderReceiverName(purchase.getOrderReceiverName());
            purchaseInfoOutput.setOrderReceiverMobile(purchase.getOrderReceiverMobile());
            purchaseInfoOutput.setOrderCreateTime(purchase.getOrderCreateTime());
            purchaseInfoOutput.setOrderPaymentMethod(purchase.getOrderPaymentMethod());
            purchaseInfoOutput.setOrderPaymentNum(purchase.getOrderPaymentNum());
            purchaseInfoOutput.setOrderPaymentTime(purchase.getOrderPaymentTime());
            purchaseInfoOutput.setOrderPrice(purchase.getOrderPrice());
            purchaseInfoOutput.setOrderStatus(purchase.getOrderStatus().shortValue());
            purchaseInfoOutput.setOrderShipMethod(purchase.getOrderShipMethod());
            purchaseInfoOutput.setOrderShipmentNumber(purchase.getOrderShipmentNumber());
            purchaseInfoOutput.setLogisticsCompany(purchase.getLogisticsCompany());
            purchaseInfoOutput.setDistributorName(purchase.getDistributorName());
            purchaseInfoOutput.setDistributorMobile(purchase.getDistributorMobile());
            purchaseInfoOutput.setDrawbackTime(purchase.getDrawbackTime());


            //PurchaseInfoOutput 添加订单明细列表
            List<PurchaseItemVo> purchaseItemVoList = purchaseItemDao.getOrderDetailByOId(Long.parseLong(purchase.getOrderId().toString()));
            if(purchaseItemVoList != null && purchaseItemVoList.size() > 0) {
                purchaseInfoOutput.setPurchaseItemVoList(purchaseItemVoList);
            }
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
    public PurchaseSelectOutput searchOrders(Integer pageNum, Integer rows, PurchaseSelectInput purchaseSelectInput) {
        PageHelper.startPage(pageNum, rows);

       /* //1.转换起始时间格式，数据库下单时间比较
        Date beginTime = purchaseSelectInput.getBeginTime();
        if (beginTime != null) {
            Long beginDate = beginTime.getTime();
            purchaseSelectInput.setBeginDate(beginDate);
        }
        purchaseSelectInput.setBeginDate(null);

        //2.转换截至时间格式，数据库下单时间比较
        Date endTime = purchaseSelectInput.getEndTime();
        if (endTime != null) {
            Long endDate = endTime.getTime();
            purchaseSelectInput.setEndDate(endDate);
        }
        purchaseSelectInput.setEndDate(null);

        //3.转换时间格式，数据库支付时间比较
        Date orderPaymentTime = purchaseSelectInput.getOrderPaymentTime();
        if (orderPaymentTime != null) {
            Long orderPaymentDate = orderPaymentTime.getTime();
            purchaseSelectInput.setOrderPaymentDate(orderPaymentDate);
        }
        purchaseSelectInput.setOrderPaymentDate(null);*/

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
    public boolean updateOrder(BigInteger orderId, Integer orderStatus,Integer receiveMethod,String name,String number) {
        if(orderStatus==Constant.OrderStatusConfig.REFUNDED){
            Date drawbackTime = new Date();
            Long drawbackDate = drawbackTime.getTime();
            boolean flag = purchaseDao.updateOrderAtr(orderId,drawbackDate ,null,null,null);
        }else if(orderStatus==Constant.OrderStatusConfig.ISSUE_SHIP){
            purchaseDao.updateOrderAtr(orderId,null,receiveMethod,name,number);
        }
        return purchaseDao.updateOrder(orderId, orderStatus);
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
     * POI批量导出订单列表
     * @param request request
     * @param response response
     * @param orderIds orderIds
     * @param pageNum pageNum
     * @param pageSize pageSize
     */
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response,String orderIds, Integer pageNum, Integer pageSize) throws Exception{

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

        //判断是否是批量导出
        List<String> orderIdList = null;
        if(orderIds != null && orderIds.length() > 0){
            orderIdList = Arrays.asList(orderIds.split(","));
        } else if(pageNum != null && pageSize != null){ //判断是否是分页查询列表
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Purchase> orderList = purchaseDao.getOrderListByIds(orderIdList);

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
     * 根据商家编号查找所有相关订单记录(分页)
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @param storeId 商家编号
     * @return 相关记录的集合
     */
    @Override
    public RecordToPurchaseOutput searchPurchases(Integer pageNum, Integer pageSize, AccountPurchaseInput input, Long storeId) {
        /**
         * 1.判断参数是否为空，是否合法
         *   ①.判断当前页码和每页显示条数是否为空，为空赋予默认值，否则继续执行
         *   ②.判断商户编号storeId 是否存在，存在继续执行，否则结束业务，返回错误代码
         * 2.访问持久化层，获取数据集合
         *   ①.使用分页插件设置分页
         *   ②.访问持久化层方法获取数据
         * 3.将获取到的数据集合中的泛型转化成vo层的类对象
         *   ①.将获取到的数据集合使用循环方法依次放入另一个集合中
         * 4.将转化后的数据集合封装到PageInfo对象中
         *   ①.封装PageInfo时要封装：当前页码，每页显示条数，总页码,数据集合
         * 5.将PageInfo对象封装到output出参类中返回
         */
        RecordToPurchaseOutput output = new RecordToPurchaseOutput();//返回对象
        //1.1、判断当前页码和每页显示条数是否为空
        if (null==pageNum){
            pageNum = 1;
        }
        if (null==pageSize){
            pageSize = 10;
        }

        //2 分页
        PageHelper.startPage(pageNum,pageSize);

        //访问持久化层，获取数据
        List<Purchase> purchaseList = purchaseDao.findPageByStoreId(input,storeId);

        if(null != purchaseList && !purchaseList.isEmpty()){
            PageInfo<Purchase> pageInfo = new PageInfo<>(purchaseList);

            //3.将获得的list集合中的对象全部转化成vo层的对象
            List<AccountPurchaseVo> purchaseVos = this.inversion(purchaseList);

            //构造返回的PageInfo信息
            PageInfo<AccountPurchaseVo> pageInfoVo = new PageInfo<>();
            pageInfoVo.setPageNum(pageNum);
            pageInfoVo.setPageSize(pageSize);
            pageInfoVo.setTotal(pageInfo.getTotal());
            pageInfoVo.setPages(pageInfo.getPages());
            pageInfoVo.setSize(pageInfo.getSize());
            pageInfoVo.setList(purchaseVos);

            //4.将转化后的数据集合封装到PageInfo对象中,封装成功信息和code
            output.setCode(Constant.CodeConfig.CODE_SUCCESS);
            output.setMessage(Constant.MessageConfig.MSG_SUCCESS);
            output.setPageInfo(pageInfoVo);
        } else {
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

}