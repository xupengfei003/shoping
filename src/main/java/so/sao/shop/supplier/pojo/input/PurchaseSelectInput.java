package so.sao.shop.supplier.pojo.input;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by acer on 2017/7/20.
 */
public class PurchaseSelectInput {
    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 收货人姓名
     */
    private String orderReceiverName;

    /**
     * 开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private String beginDate;

    /**
     * 截至时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private String endDate;

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

    /**
     * 收货起始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private String orderReceiveBeginTime;

    /**
     * 收货截止时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private String orderReceiveEndTime;

    /**
     * 订单支付起始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private String orderPaymentBeginTime;

    /**
     * 订单支付截止时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private String orderPaymentEndTime;


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

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
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

    public String getOrderReceiveBeginTime() {
        return orderReceiveBeginTime;
    }

    public void setOrderReceiveBeginTime(String orderReceiveBeginTime) {
        this.orderReceiveBeginTime = orderReceiveBeginTime;
    }

    public String getOrderReceiveEndTime() {
        return orderReceiveEndTime;
    }

    public void setOrderReceiveEndTime(String orderReceiveEndTime) {
        this.orderReceiveEndTime = orderReceiveEndTime;
    }

    public String getOrderPaymentBeginTime() {
        return orderPaymentBeginTime;
    }

    public void setOrderPaymentBeginTime(String orderPaymentBeginTime) {
        this.orderPaymentBeginTime = orderPaymentBeginTime;
    }

    public String getOrderPaymentEndTime() {
        return orderPaymentEndTime;
    }

    public void setOrderPaymentEndTime(String orderPaymentEndTime) {
        this.orderPaymentEndTime = orderPaymentEndTime;
    }
}
