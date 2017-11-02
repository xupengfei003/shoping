package so.sao.shop.supplier.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author gxy on 2017/7/26.
 */
public class PurchaseInfoVo {
    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 收货人姓名
     */
    private String orderReceiverName;

    /**
     * 收货人地址
     */
    private String orderAddress;

    /**
     * 收货人电话
     */
    private String orderReceiverMobile;

    /**
     * 订单状态
     */
    private Short orderStatus;

    /**
     * 订单金额
     */
    private String orderPrice;

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
     * 支付时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderPaymentTime;

    /**
     * 支付流水号
     */
    private String orderPaymentNum;

    /**
     * 配送方式
     */
    private Integer orderShipMethod;

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
     * 物流单号
     */
    private String orderShipmentNumber;

    /**
     * 订单邮费 (0:包邮，非零显示具体金额)
     */
    private String orderPostage;

    /**
     * 折扣优惠
     */
    private String discount;

    /**
     * 合计金额
     */
    private String orderTotalPrice;

    /**
     * 实付金额
     */
    private String payAmount;

    /**
     * 退款金额
     */
    private String drawbackPrice;

    /**
     * 该订单是否有发票
     */
    private Integer isReceipt;

    /**
     * 订单明细列表
     */
    private List<PurchaseItemVo> purchaseItemVoList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public Short getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Short orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Integer getOrderPaymentMethod() {
        return orderPaymentMethod;
    }

    public void setOrderPaymentMethod(Integer orderPaymentMethod) {
        this.orderPaymentMethod = orderPaymentMethod;
    }

    public String getOrderPaymentNum() {
        return orderPaymentNum;
    }

    public void setOrderPaymentNum(String orderPaymentNum) {
        this.orderPaymentNum = orderPaymentNum;
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

    public String getOrderShipmentNumber() {
        return orderShipmentNumber;
    }

    public void setOrderShipmentNumber(String orderShipmentNumber) {
        this.orderShipmentNumber = orderShipmentNumber;
    }

    public List<PurchaseItemVo> getPurchaseItemVoList() {
        return purchaseItemVoList;
    }

    public void setPurchaseItemVoList(List<PurchaseItemVo> purchaseItemVoList) {
        this.purchaseItemVoList = purchaseItemVoList;
    }

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public Date getOrderPaymentTime() {
        return orderPaymentTime;
    }

    public void setOrderPaymentTime(Date orderPaymentTime) {
        this.orderPaymentTime = orderPaymentTime;
    }

    public Date getDrawbackTime() {
        return drawbackTime;
    }

    public void setDrawbackTime(Date drawbackTime) {
        this.drawbackTime = drawbackTime;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrderPostage() {
        return orderPostage;
    }

    public void setOrderPostage(String orderPostage) {
        this.orderPostage = orderPostage;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(String orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getDrawbackPrice() {
        return drawbackPrice;
    }

    public void setDrawbackPrice(String drawbackPrice) {
        this.drawbackPrice = drawbackPrice;
    }

    public Integer getIsReceipt() {
        return isReceipt;
    }

    public void setIsReceipt(Integer isReceipt) {
        this.isReceipt = isReceipt;
    }
}
