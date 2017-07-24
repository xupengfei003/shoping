package com.sao.so.supplier.pojo.input;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by acer on 2017/7/20.
 */
public class PurchaseSelectInput {
    /**
     * 订单编号
     */
    private BigInteger orderId;

    /**
     * 收货人姓名
     */
    private String orderReceiverName;


//    private Date beginTime;


//    private Date endTime;

    /**
     * 开始时间
     */
    private Long beginDate;

    /**
     * 截至时间
     */
    private Long endDate;

    /**
     * 起始金额
     */
    private BigDecimal beginMoney;

    /**
     * 截至金额
     */
    private BigDecimal endMoney;

    /**
     * 收货人电话
     */
    private String orderReceiverMobile;


//    private Date orderPaymentTime;

    /**
     * 付款时间
     */
    private Long orderPaymentDate;

    /**
     * 支付方式
     */
    private Integer orderPaymentMethod;

    /**
     * 支付流水号
     */
    private String orderPaymentNum;

    /**
     * 订单状态
     */
    private Short orderStatus;

    /**
     * 买家ID
     */
    private BigInteger userId;

    /**
     * 商户ID
     */
    private BigInteger storeId;

    public BigInteger getOrderId() {
        return orderId;
    }

    public void setOrderId(BigInteger orderId) {
        this.orderId = orderId;
    }

    public String getOrderReceiverName() {
        return orderReceiverName;
    }

    public void setOrderReceiverName(String orderReceiverName) {
        this.orderReceiverName = orderReceiverName;
    }

    public Long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Long beginDate) {
        this.beginDate = beginDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getBeginMoney() {
        return beginMoney;
    }

    public void setBeginMoney(BigDecimal beginMoney) {
        this.beginMoney = beginMoney;
    }

    public BigDecimal getEndMoney() {
        return endMoney;
    }

    public void setEndMoney(BigDecimal endMoney) {
        this.endMoney = endMoney;
    }

    public String getOrderReceiverMobile() {
        return orderReceiverMobile;
    }

    public void setOrderReceiverMobile(String orderReceiverMobile) {
        this.orderReceiverMobile = orderReceiverMobile;
    }

    public Long getOrderPaymentDate() {
        return orderPaymentDate;
    }

    public void setOrderPaymentDate(Long orderPaymentDate) {
        this.orderPaymentDate = orderPaymentDate;
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

    public Short getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Short orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public BigInteger getStoreId() {
        return storeId;
    }

    public void setStoreId(BigInteger storeId) {
        this.storeId = storeId;
    }
}
