package so.sao.shop.supplier.pojo.input;

import javax.validation.constraints.*;

/**
 * <p>
 * 账户管理下的收入明细查询input类 (高级条件类)
 * </p>
 *
 * @author 透云-中软-西安项目组-zhenhai.zheng
 * @since 2017年7月22日
 **/
public class AccountPurchaseInput {

    /**
     * 开始时间（支付时间）
     */
    private String payBeginTime;

    /**
     * 结束时间（支付时间）
     */
    private String payEndTime;

    /**
     * 开始时间（创建时间）
     */
    private String createBeginTime;

    /**
     * 结束时间（创建时间）  
     */
    private String createEndTime;

    /**
     * 支付方式(0.全部；1、支付宝；2、微信)
     */
    @Min(value = 0,message = "支付方式错误")
    @Max(value = 2,message = "支付方式错误")
    private Integer orderPaymentMethod;

    /**
     * 收货人姓名
     */
    private String orderReceiverName;

    /**
     * 收货人电话
     */
    @Pattern(regexp = "[0-9]*",message = "输入电话号码有误")
    private String orderReceiverMobile;

    /**
     * 订单编号
     */
    @Pattern(regexp = "^.{0}$|^.{30}$",message = "订单编号为30位")
    private String orderId;

    public String getPayBeginTime() {
        return  payBeginTime;
    }

    public void setPayBeginTime(String payBeginTime) {
        this.payBeginTime = payBeginTime;
    }

    public String getPayEndTime() {
        return payEndTime;
    }

    public void setPayEndTime(String payEndTime) {
        this.payEndTime = payEndTime;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCreateBeginTime() {
        return createBeginTime;
    }

    public void setCreateBeginTime(String createBeginTime) {
        this.createBeginTime = createBeginTime;
    }

    public String getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(String createEndTime) {
        this.createEndTime = createEndTime;
    }

    public Integer getOrderPaymentMethod() {
        return orderPaymentMethod;
    }

    public void setOrderPaymentMethod(Integer orderPaymentMethod) {
        this.orderPaymentMethod = orderPaymentMethod;
    }

    @Override
    public String toString() {
        return "AccountPurchaseInput{" +
                "payBeginTime='" + payBeginTime + '\'' +
                ", payEndTime='" + payEndTime + '\'' +
                ", createBeginTime='" + createBeginTime + '\'' +
                ", createEndTime='" + createEndTime + '\'' +
                ", orderPaymentMethod=" + orderPaymentMethod +
                ", orderReceiverName='" + orderReceiverName + '\'' +
                ", orderReceiverMobile='" + orderReceiverMobile + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
