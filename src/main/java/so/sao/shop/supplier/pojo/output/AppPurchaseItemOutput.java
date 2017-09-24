package so.sao.shop.supplier.pojo.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import so.sao.shop.supplier.pojo.vo.AppPurchaseItemVo;
import so.sao.shop.supplier.util.NumberUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by acer on 2017/9/7.
 */
public class AppPurchaseItemOutput {
    /**
     * 收货人
     */
    private String orderReceiverName;
    /**
     * 收货人电话
     */
    private String orderReceiverMobile;
    /**
     * 收货人地址
     */
    private String orderAddress;
    /**
     * 订单合计（订单实付金额）
     */
    private String orderPrice;
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 下单时
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderCreateTime;
    /**
     * 供应商名称
     */
    private String storeName;
    /**
     * 订单状态
     */
    private Integer orderStatus;
    /**
     * 物流单号
     */
    private String orderShipmentNumber;
    /**
     * 订单支付时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderPaymentTime;
    /**
     * 卖家拒绝时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderRefuseTime;
    /**
     * 退款时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date drawbackTime;
    /**
     * 发货时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date deliverGoodsTime;
    /**
     * 收货时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderReceiveTime;
    /**
     * 取消时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderCancelTime;
    /**
     * 支付流水号
     */
    private String orderPaymentNum;
    /**
     * 订单邮费 (0:包邮，非零显示具体金额)
     */
    private String orderPostage;
    /**
     * 门店ID
     */
    private BigDecimal userId;
    /**
     * 商品信息
     */
    private List<AppPurchaseItemVo> appPurchaseItemVos;

    public String getOrderReceiverName() {
        return orderReceiverName;
    }

    public void setOrderReceiverName(String orderReceiverName) {
        this.orderReceiverName = orderReceiverName;
    }

    public String getOrderReceiverMobile() {
        return orderReceiverMobile;
    }

    public void setOrderReceiverMobile(String orderReceiverMobile) {
        this.orderReceiverMobile = orderReceiverMobile;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = NumberUtil.number2Thousand(new BigDecimal(orderPrice));
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderShipmentNumber() {
        return orderShipmentNumber;
    }

    public void setOrderShipmentNumber(String orderShipmentNumber) {
        this.orderShipmentNumber = orderShipmentNumber;
    }

    public List<AppPurchaseItemVo> getAppPurchaseItemVos() {
        return appPurchaseItemVos;
    }

    public void setAppPurchaseItemVos(List<AppPurchaseItemVo> appPurchaseItemVos) {
        this.appPurchaseItemVos = appPurchaseItemVos;
    }

    public Date getOrderPaymentTime() {
        return orderPaymentTime;
    }

    public void setOrderPaymentTime(Date orderPaymentTime) {
        this.orderPaymentTime = orderPaymentTime;
    }

    public Date getOrderRefuseTime() {
        return orderRefuseTime;
    }

    public void setOrderRefuseTime(Date orderRefuseTime) {
        this.orderRefuseTime = orderRefuseTime;
    }

    public Date getDrawbackTime() {
        return drawbackTime;
    }

    public void setDrawbackTime(Date drawbackTime) {
        this.drawbackTime = drawbackTime;
    }

    public Date getDeliverGoodsTime() {
        return deliverGoodsTime;
    }

    public void setDeliverGoodsTime(Date deliverGoodsTime) {
        this.deliverGoodsTime = deliverGoodsTime;
    }

    public Date getOrderReceiveTime() {
        return orderReceiveTime;
    }

    public void setOrderReceiveTime(Date orderReceiveTime) {
        this.orderReceiveTime = orderReceiveTime;
    }

    public Date getOrderCancelTime() {
        return orderCancelTime;
    }

    public void setOrderCancelTime(Date orderCancelTime) {
        this.orderCancelTime = orderCancelTime;
    }

    public String getOrderPaymentNum() {
        return orderPaymentNum;
    }

    public void setOrderPaymentNum(String orderPaymentNum) {
        this.orderPaymentNum = orderPaymentNum;
    }

    public BigDecimal getUserId() {
        return userId;
    }

    public void setUserId(BigDecimal userId) {
        this.userId = userId;
    }

    public String getOrderPostage() {
        return orderPostage;
    }

    public void setOrderPostage(String orderPostage) {
        this.orderPostage = orderPostage;
    }
}
