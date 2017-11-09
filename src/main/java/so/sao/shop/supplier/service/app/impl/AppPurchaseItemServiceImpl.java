package so.sao.shop.supplier.service.app.impl;

import org.springframework.stereotype.Service;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.app.AppPurchaseDao;
import so.sao.shop.supplier.dao.app.AppPurchaseItemDao;
import so.sao.shop.supplier.dao.external.CouponDao;
import so.sao.shop.supplier.domain.ReceiptPurchase;
import so.sao.shop.supplier.domain.external.Coupon;
import so.sao.shop.supplier.pojo.output.AppPurchaseItemOutput;
import so.sao.shop.supplier.pojo.vo.AppPurchaseItemVo;
import so.sao.shop.supplier.pojo.vo.AppPurchaseShipMethodVo;
import so.sao.shop.supplier.pojo.vo.AppPurchasesVo;
import so.sao.shop.supplier.service.app.AppPurchaseItemService;
import so.sao.shop.supplier.util.BeanMapper;
import so.sao.shop.supplier.util.NumberUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by acer on 2017/9/7.
 */
@Service
public class AppPurchaseItemServiceImpl implements AppPurchaseItemService {
    @Resource
    private AppPurchaseItemDao appPurchaseItemDao;
    @Resource
    private AppPurchaseDao appPurchaseDao;
    @Resource
    private CouponDao couponDao;

    /**
     * 根据订单ID查询订单详情
     *
     * @param orderId 订单ID
     * @return AppPurchaseItemVo 订单信息
     * @throws Exception 异常
     */
    @Override
    public AppPurchaseItemOutput findOrderItemList(String orderId) throws Exception {
        /**
         * 1.如果查询待付款订单详情用合并编号查询
         * 2.如果查询其他状态订单详情则用订单编号查询
         */
        List<AppPurchasesVo> appPurchasesVoList = new ArrayList<>();
        AppPurchasesVo appPurchasesVos = new AppPurchasesVo();
        Integer orderStatus = 0;
        //计算运费
        BigDecimal totalOrderPostageList = new BigDecimal(0);
        //配送方式列表
        List<AppPurchaseShipMethodVo> appPurchaseShipMethodVos = new ArrayList<>();
        //存放订单ID集合
        List<String> orderIds = new ArrayList<>();
        //存放发票信息
        List<ReceiptPurchase> receiptPurchases = new ArrayList<>();
        //配送方式
        AppPurchaseShipMethodVo appPurchaseShipMethodVo = new AppPurchaseShipMethodVo();
        if(orderId.length() == 28){
            //根据合并支付ID查询所有订单
            appPurchasesVoList = appPurchaseDao.findOrderByPayId(orderId);
            for(AppPurchasesVo appPurchasesVo : appPurchasesVoList){
                totalOrderPostageList = totalOrderPostageList.add(appPurchasesVo.getOrderPostage());
                orderIds.add(appPurchasesVo.getOrderId());
            }
            appPurchasesVos = appPurchasesVoList.get(0);
            appPurchasesVos.setOrderPostage(totalOrderPostageList);
            orderStatus = Constant.OrderStatusConfig.PAYMENT;
        }else{
            //查询订单信息
            appPurchasesVos = appPurchaseDao.findOrderByOrderId(orderId);
            orderIds.add(appPurchasesVos.getOrderId());
            if(Objects.equals(appPurchasesVos.getOrderStatus(),Constant.OrderStatusConfig.ISSUE_SHIP) ||
                    Objects.equals(appPurchasesVos.getOrderStatus(),Constant.OrderStatusConfig.RECEIVED) ||
                    Objects.equals(appPurchasesVos.getOrderStatus(),Constant.OrderStatusConfig.REJECT) ||
                    Objects.equals(appPurchasesVos.getOrderStatus(),Constant.OrderStatusConfig.REFUNDED)){
                appPurchaseShipMethodVo = setOrderShipMethod(appPurchasesVos, appPurchaseShipMethodVo);
                appPurchaseShipMethodVos.add(appPurchaseShipMethodVo);
            }
        }

        //获取订单ID集合
        List<String> orderIdList = new ArrayList<>();
        for(AppPurchasesVo appPurchasesVo : appPurchasesVoList){
            orderIdList.add(appPurchasesVo.getOrderId());
        }

        List<AppPurchaseItemVo> appPurchaseItemVoList = new ArrayList<>();
        //业务逻辑
        //1.如果订单状态为待付款的时候（订单状态为1），则把payID相同的订单列出；
        //2.如果订单状态为其他时，则只列出orderId对应的订单
        if(Objects.equals(orderStatus,Constant.OrderStatusConfig.PAYMENT)){
            //根据订单ID列表查询订单详情
            appPurchaseItemVoList = appPurchaseItemDao.findOrderItemList(orderIdList);
        }else{
            //查询详情信息
            appPurchaseItemVoList = appPurchaseItemDao.findOrderItemListByOrderId(orderId);
        }
        AppPurchaseItemOutput appPurchaseItemOutput = null;
        //查询优惠券信息
        Coupon coupon = couponDao.findCouponById(appPurchasesVos.getCouponId());
        if(null != appPurchasesVos){
            appPurchaseItemOutput = BeanMapper.map(appPurchasesVos,AppPurchaseItemOutput.class);
            if(Objects.equals(orderStatus,Constant.OrderStatusConfig.PAYMENT)){
                appPurchaseItemOutput.setOrderId(appPurchasesVos.getPayId());
                appPurchaseItemOutput.setStoreId(null);
                appPurchaseItemOutput.setStoreName(null);
                if (null == coupon){
                    appPurchaseItemOutput.setDiscount("0");
                } else {
                    appPurchaseItemOutput.setDiscount(String.valueOf(coupon.getCouponValue()));
                }
            }
            //输出运费
            //1.如果运费为0，则显示“包邮”
            //2.如果有运费，则输出实际金额的千分值
            if(appPurchasesVos.getOrderPostage().compareTo(new BigDecimal(0)) == 0){
                appPurchaseItemOutput.setOrderPostage("包邮");
            } else {
                appPurchaseItemOutput.setOrderPostage(NumberUtil.number2Thousand(appPurchasesVos.getOrderPostage()));
            }
        }
        //查询发票信息列表
        receiptPurchases = appPurchaseDao.findReceiptByOrderIds(orderIds);
        for (AppPurchaseItemVo appPurchaseItemVo : appPurchaseItemVoList){
            for (ReceiptPurchase receiptPurchase : receiptPurchases){
                if (appPurchaseItemVo.getOrderId().equals(receiptPurchase.getOrderId())){
                    appPurchaseItemVo.setReceiptPurchase(receiptPurchase);
                }
            }
        }
        if(null!=appPurchaseItemVoList && appPurchaseItemVoList.size()>0){
            //当查询订单状态为1时，计算该订单下所有商品总价
            BigDecimal goodsAllPrice = new BigDecimal(0);
            for(AppPurchaseItemVo appPurchaseItemVo : appPurchaseItemVoList){
                //计算总价
                BigDecimal goodsNum = new BigDecimal(appPurchaseItemVo.getGoodsNumber());
                String goodsUnit = appPurchaseItemVo.getGoodsUnitPrice().replaceAll(",","");
                goodsAllPrice = goodsAllPrice.add(goodsNum.multiply(new BigDecimal(goodsUnit)));
            }
            appPurchaseItemOutput.setOrderPrice(String.valueOf(goodsAllPrice));
            if(orderStatus == 1){
                appPurchaseItemOutput.setOrderId(appPurchasesVos.getPayId());
            }
            appPurchaseItemOutput.setAppPurchaseShipMethods(appPurchaseShipMethodVos);
            appPurchaseItemOutput.setAppPurchaseItemVos(appPurchaseItemVoList);
        }
        return appPurchaseItemOutput;
    }

    private AppPurchaseShipMethodVo setOrderShipMethod(AppPurchasesVo appPurchasesVo, AppPurchaseShipMethodVo appPurchaseShipMethodVo) {
        //赋值配送方式
        if(null == appPurchasesVo.getOrderShipMethod()){
            appPurchaseShipMethodVo = null;
        } else {
            if(appPurchasesVo.getOrderShipMethod() == 1){
                appPurchaseShipMethodVo.setOrderShipMethod(appPurchasesVo.getOrderShipMethod());
                appPurchaseShipMethodVo.setDistributorName(appPurchasesVo.getDistributorName());
                appPurchaseShipMethodVo.setDistributorMobile(appPurchasesVo.getDistributorMobile());
            } else {
                appPurchaseShipMethodVo.setOrderShipMethod(appPurchasesVo.getOrderShipMethod());
                appPurchaseShipMethodVo.setLogisticsCompany(appPurchasesVo.getLogisticsCompany());
                appPurchaseShipMethodVo.setOrderShipmentNumber(appPurchasesVo.getOrderShipmentNumber());
            }
        }
        return appPurchaseShipMethodVo;
    }
}
