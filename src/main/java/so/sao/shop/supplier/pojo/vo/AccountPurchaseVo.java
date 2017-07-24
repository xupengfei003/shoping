package so.sao.shop.supplier.pojo.vo;

import java.math.BigDecimal;

/**
 * <p>
 * 订单表对应的vo类
 * </p>
 *
 * @author 透云-中软-西安项目组-zhenhai.zheng
 * @since 2017年7月20日
 **/
public class AccountPurchaseVo {
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 订单实付金额
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
     * 支付方式
     */
    private Integer orderPaymentMethod;

    /**
     * 订单状态
     */
    private Integer orderStatus;

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

    @Override
    public String toString() {
        return "PurchaseVo{" +
                "orderId=" + orderId +
                ", orderPrice=" + orderPrice +
                ", orderReceiverName='" + orderReceiverName + '\'' +
                ", orderReceiverMobile='" + orderReceiverMobile + '\'' +
                ", orderPaymentMethod=" + orderPaymentMethod +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
