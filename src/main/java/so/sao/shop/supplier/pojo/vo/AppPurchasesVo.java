package so.sao.shop.supplier.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import so.sao.shop.supplier.util.NumberUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by bzh on 2017/7/20.
 */
public class AppPurchasesVo {
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
    private BigDecimal orderPrice;
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 合并支付编号
     */
    private String payId;
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
     * 订单邮费
     */
    private BigDecimal orderPostage;
    /**
     * 门店ID
     */
    private String userId;
    /**
     * 门店名称
     */
    private String userName;
    /**
     * 商户ID
     */
    private String storeId;
    /**
     * 配送方式（1.自配送，2.物流公司）
     */
    private Integer orderShipMethod;
    /**
     * 配送人姓名
     */
    private String distributorName;
    /**
     * 配送人电话
     */
    private String distributorMobile;
    /**
     * 物流公司
     */
    private String logisticsCompany;
    /**
     * 物流公司物流单号
     */
    private String orderShipmentNumber;
    /**
     * 折扣优惠
     */
    private BigDecimal discount;
    /**
     * 优惠券ID
     */
    private Long couponId;
    /**
     * 合计总价
     */
    private BigDecimal orderTotalPrice;
    /**
     * 实付金额
     */
    private BigDecimal payAmount;
    /**
     * 退款金额
     */
    private BigDecimal drawbackPrice;
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

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
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

    public BigDecimal getOrderPostage() {
        return orderPostage;
    }

    public void setOrderPostage(BigDecimal orderPostage) {
        this.orderPostage = orderPostage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getOrderShipMethod() {
        return orderShipMethod;
    }

    public void setOrderShipMethod(Integer orderShipMethod) {
        this.orderShipMethod = orderShipMethod;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getDistributorMobile() {
        return distributorMobile;
    }

    public void setDistributorMobile(String distributorMobile) {
        this.distributorMobile = distributorMobile;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public BigDecimal getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(BigDecimal orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getDrawbackPrice() {
        return drawbackPrice;
    }

    public void setDrawbackPrice(BigDecimal drawbackPrice) {
        this.drawbackPrice = drawbackPrice;
    }
}
