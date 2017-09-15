package so.sao.shop.supplier.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

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
     * 合并支付单号
     */
    private String payId;

    /**
     * 商户ID
     */
    private Long storeId;

    /**
     * 商户名称
     */
    private String storeName;

    /**
     * 店铺ID
     */
    private Long userId;

    /**
     * 店铺名称
     */
    private String userName;

    /**
     * 订单实付金额
     */
    private BigDecimal orderPrice;

    /**
     * 订单结算金额
     */
    private BigDecimal orderSettlemePrice;

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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderCreateTime;

    /**
     * 支付方式
     */
    private Integer orderPaymentMethod;

    /**
     * 订单支付时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderPaymentTime;

    /**
     * 卖家拒绝理由
     */
    private String orderRefuseReason;

    /**
     * 卖家拒绝时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderRefuseTime;

    /**
     * 买家取消订单原因
     */
    private String orderCancelReason;

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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date drawbackTime;

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
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updatedAt;

    /**
     * 收货时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderReceiveTime;

    /**
     * 支付状态
     */
    private Integer payStatus;
    /**
     * 发货时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date deliverGoodsTime;
    /**
     * 取消时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderCancelTime;
    /**
     * 取消类型
     */
    private String cancelType;
    /**
     * 拒收类型
     */
    private String refuseType;

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

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public Integer getOrderPaymentMethod() {
        return orderPaymentMethod;
    }

    public void setOrderPaymentMethod(Integer orderPaymentMethod) {
        this.orderPaymentMethod = orderPaymentMethod;
    }

    public Date getOrderPaymentTime() {
        return orderPaymentTime;
    }

    public void setOrderPaymentTime(Date orderPaymentTime) {
        this.orderPaymentTime = orderPaymentTime;
    }

    public String getOrderRefuseReason() {
        return orderRefuseReason;
    }

    public void setOrderRefuseReason(String orderRefuseReason) {
        this.orderRefuseReason = orderRefuseReason;
    }

    public Date getOrderRefuseTime() {
        return orderRefuseTime;
    }

    public void setOrderRefuseTime(Date orderRefuseTime) {
        this.orderRefuseTime = orderRefuseTime;
    }

    public String getOrderCancelReason() {
        return orderCancelReason;
    }

    public void setOrderCancelReason(String orderCancelReason) {
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

    public Date getDrawbackTime() {
        return drawbackTime;
    }

    public void setDrawbackTime(Date drawbackTime) {
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BigDecimal getOrderSettlemePrice() {
        return orderSettlemePrice;
    }

    public void setOrderSettlemePrice(BigDecimal orderSettlemePrice) {
        this.orderSettlemePrice = orderSettlemePrice;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public Date getOrderReceiveTime() {
        return orderReceiveTime;
    }

    public void setOrderReceiveTime(Date orderReceiveTime) {
        this.orderReceiveTime = orderReceiveTime;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Date getDeliverGoodsTime() {
        return deliverGoodsTime;
    }

    public void setDeliverGoodsTime(Date deliverGoodsTime) {
        this.deliverGoodsTime = deliverGoodsTime;
    }

    public Date getOrderCancelTime() {
        return orderCancelTime;
    }

    public void setOrderCancelTime(Date orderCancelTime) {
        this.orderCancelTime = orderCancelTime;
    }

    public String getCancelType() {
        return cancelType;
    }

    public void setCancelType(String cancelType) {
        this.cancelType = cancelType;
    }

    public String getRefuseType() {
        return refuseType;
    }

    public void setRefuseType(String refuseType) {
        this.refuseType = refuseType;
    }
}
