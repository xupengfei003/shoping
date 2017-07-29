package so.sao.shop.supplier.pojo.vo;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by niewenchao on 2017/7/24.
 */
public class PurchaseVo {

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
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 账户状态
     */
    private String accountStatus;

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

    public Integer getOrderPaymentMethod() {
        return orderPaymentMethod;
    }

    public void setOrderPaymentMethod(Integer orderPaymentMethod) {
        this.orderPaymentMethod = orderPaymentMethod;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAccountStatus() {
        return accountStatus;
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

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
