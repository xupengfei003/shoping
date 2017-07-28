package so.sao.shop.supplier.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

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
     * 订单金额
     */
    private BigDecimal orderPrice;

    /**
     * 下单时间
     */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
    private Date orderCreateTime;

    /**
     * 支付时间
     */
    private Long orderPaymentTime;

    /**
     * 支付方式
     */
    private Integer orderPaymentMethod;

    /**
     * 支付流水号
     */
    private String orderPaymentNum;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public Short getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Short orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getOrderPaymentTime() {
        return orderPaymentTime;
    }

    public void setOrderPaymentTime(Long orderPaymentTime) {
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

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
