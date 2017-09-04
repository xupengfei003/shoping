package so.sao.shop.supplier.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.util.Date;

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
     * 收货人姓名
     */
    private String orderReceiverName;

    /**
     * 收货人电话
     */
    private String orderReceiverMobile;

    /**
     * 订单状态 1待付款2代发货3已发货4已收货5已拒收6已退款7已完成
     */
    private Integer orderStatus;

    /**
     * 订单实付金额
     */
    @NumberFormat(pattern = "\"###,##0.00\"")
    private String orderPrice;

    /**
     * 订单结算金额
     */
    @NumberFormat(pattern = "\"###,##0.00\"")
    private String orderSettlemePrice;

    /**
     * 下单时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderCreateTime;

    /**
     * 订单支付时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderPaymentTime;

    /**
     * 支付流水号
     */
    private String orderPaymentNum;
    /**
     * 支付方式
     */
    private Integer orderPaymentMethod;

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

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
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

    public String getOrderSettlemePrice() {
        return orderSettlemePrice;
    }

    public void setOrderSettlemePrice(String orderSettlemePrice) {
        this.orderSettlemePrice = orderSettlemePrice;
    }

    @Override
    public String toString() {
        return "AccountPurchaseVo{" +
                "orderId='" + orderId + '\'' +
                ", orderReceiverName='" + orderReceiverName + '\'' +
                ", orderReceiverMobile='" + orderReceiverMobile + '\'' +
                ", orderStatus=" + orderStatus +
                ", orderPrice=" + orderPrice +
                ", orderSettlemePrice=" + orderSettlemePrice +
                ", orderCreateTime=" + orderCreateTime +
                ", orderPaymentTime=" + orderPaymentTime +
                ", orderPaymentNum='" + orderPaymentNum + '\'' +
                ", orderPaymentMethod=" + orderPaymentMethod +
                '}';
    }
}
