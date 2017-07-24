package com.sao.so.supplier.domain;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 订单信息
 * </p>
 *
 * @author 透云-中软-西安项目组
 * @since 2017-07-19
 */
public class Purchase {
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 商户ID
     */
    private Long storeId;
    /**
     * 买家ID
     */
    private Long userId;
    /**
     * 订单总金额
     */
    private BigDecimal orderPrice;
    /**
     * 收货人姓名
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
     * 配送方式
     */
    private Integer orderShipMethod;
    /**
     * 支付流水号
     */
    private String orderPaymentNum;
    /**
     * 下单时间
     */
    private Long orderCreateTime;
    /**
     * 支付方式
     */
    private Integer orderPaymentMethod;
    /**
     * 订单支付时间
     */
    private Long orderPaymentTime;
    /**
     * 买家申请退货理由
     */
    private String orderRefundReason;
    /**
     * 卖家申请退货时间
     */
    private Long orderRefundTime;
    /**
     * 卖家拒绝理由
     */
    private String orderRefuseReason;
    /**
     * 卖家拒绝时间
     */
    private Long orderRefuseTime;
    /**
     * 买家取消订单原因
     */
    private Integer orderCancelReason;
    /**
     * 物流单号
     */
    private String orderShipmentNumber;
    /**
     * 发票状态:0有1没有
     */
    private Integer orderInvoice;
    /**
     * 订单状态 1待付款2代发货3已发货4已收货5已拒收6已退款7已完成
     */
    private Integer orderStatus;
    /**
     * 订单详情信息
     */
    private List<PurchaseItem> purchaseItemList;

    /**
     * 账户状态（1：已统计 0：未统计 2：已提现）
     */
    private String accountStatus;

    /**
     * 退款时间
     */
    private Long drawbackTime;

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

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

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

    public Integer getOrderShipMethod() {
        return orderShipMethod;
    }

    public void setOrderShipMethod(Integer orderShipMethod) {
        this.orderShipMethod = orderShipMethod;
    }

    public String getOrderPaymentNum() {
        return orderPaymentNum;
    }

    public void setOrderPaymentNum(String orderPaymentNum) {
        this.orderPaymentNum = orderPaymentNum;
    }

    public Long getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Long orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public Integer getOrderPaymentMethod() {
        return orderPaymentMethod;
    }

    public void setOrderPaymentMethod(Integer orderPaymentMethod) {
        this.orderPaymentMethod = orderPaymentMethod;
    }

    public Long getOrderPaymentTime() {
        return orderPaymentTime;
    }

    public void setOrderPaymentTime(Long orderPaymentTime) {
        this.orderPaymentTime = orderPaymentTime;
    }

    public String getOrderRefundReason() {
        return orderRefundReason;
    }

    public void setOrderRefundReason(String orderRefundReason) {
        this.orderRefundReason = orderRefundReason;
    }

    public Long getOrderRefundTime() {
        return orderRefundTime;
    }

    public void setOrderRefundTime(Long orderRefundTime) {
        this.orderRefundTime = orderRefundTime;
    }

    public String getOrderRefuseReason() {
        return orderRefuseReason;
    }

    public void setOrderRefuseReason(String orderRefuseReason) {
        this.orderRefuseReason = orderRefuseReason;
    }

    public Long getOrderRefuseTime() {
        return orderRefuseTime;
    }

    public void setOrderRefuseTime(Long orderRefuseTime) {
        this.orderRefuseTime = orderRefuseTime;
    }

    public Integer getOrderCancelReason() {
        return orderCancelReason;
    }

    public void setOrderCancelReason(Integer orderCancelReason) {
        this.orderCancelReason = orderCancelReason;
    }

    public String getOrderShipmentNumber() {
        return orderShipmentNumber;
    }

    public void setOrderShipmentNumber(String orderShipmentNumber) {
        this.orderShipmentNumber = orderShipmentNumber;
    }

    public Integer getOrderInvoice() {
        return orderInvoice;
    }

    public void setOrderInvoice(Integer orderInvoice) {
        this.orderInvoice = orderInvoice;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getDrawbackTime() {
        return drawbackTime;
    }

    public void setDrawbackTime(Long drawbackTime) {
        this.drawbackTime = drawbackTime;
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

    public List<PurchaseItem> getPurchaseItemList() {
        return purchaseItemList;
    }

    public void setPurchaseItemList(List<PurchaseItem> purchaseItemList) {
        this.purchaseItemList = purchaseItemList;
    }
}
