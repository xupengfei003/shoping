package so.sao.shop.supplier.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import so.sao.shop.supplier.util.NumberUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by bzh on 2017/7/20.
 */
public class PurchasesVo {
    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 收货人姓名
     */
    private String orderReceiverName;

    /**
     * 收货人电话
     */
    private String orderReceiverMobile;

    /**
     * 订单状态
     */
    private Short orderStatus;

    /**
     * 订单金额(商品金额小计)
     */
    private String orderPrice;

    /**
     * 下单时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderCreateTime;

    /**
     * 支付时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderPaymentTime;

    /**
     * 支付方式(支付类型)
     */
    private Integer orderPaymentMethod;

    /**
     * 支付流水号
     */
    private String orderPaymentNum;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;
    /**
     * 订单邮费 (0:包邮，非零显示具体金额)
     */
    private String orderPostage;
    /**
     * 供应商名称
     */
    private String storeName;
    /**
     * 收货时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderReceiveTime;
    /**
     * 折扣优惠
     */
    private BigDecimal discount;
    /**
     * 合计金额
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOrderPostage() {
        return orderPostage;
    }

    public void setOrderPostage(String orderPostage) {
        this.orderPostage = orderPostage;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Date getOrderReceiveTime() {
        return orderReceiveTime;
    }

    public void setOrderReceiveTime(Date orderReceiveTime) {
        this.orderReceiveTime = orderReceiveTime;
    }

    public String getDiscount() {
        return NumberUtil.number2Thousand(discount);
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getOrderTotalPrice() {
        return NumberUtil.number2Thousand(orderTotalPrice);
    }

    public void setOrderTotalPrice(BigDecimal orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public String getPayAmount() {
        return NumberUtil.number2Thousand(payAmount);
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getDrawbackPrice() {
        return NumberUtil.number2Thousand(drawbackPrice);
    }

    public void setDrawbackPrice(BigDecimal drawbackPrice) {
        this.drawbackPrice = drawbackPrice;
    }
}
