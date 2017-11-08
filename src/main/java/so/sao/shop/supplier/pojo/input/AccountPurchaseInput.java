package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date payBeginTime;

    /**
     * 结束时间（支付时间）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date payEndTime;

    /**
     * 开始时间（创建时间）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createBeginTime;

    /**
     * 结束时间（创建时间）  
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createEndTime;

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

    /**
     * 排序的字段 0 付款时间
     */
    @Pattern(regexp = "^[0]$", message = "排序字段不存在")
    @NotEmpty(message = "排序字段码不能为空")
    private String sortName;

    /**
     * 排序的次序 0 正序; 1 逆序
     */
    @Pattern(regexp = "^[0-1]$", message = "排序次序码错误")
    @NotEmpty(message = "排序次序码不能为空")
    private String sortType;

    public Date getPayBeginTime() {
        return payBeginTime;
    }

    public void setPayBeginTime(Date payBeginTime) {
        this.payBeginTime = payBeginTime;
    }

    public Date getPayEndTime() {
        return payEndTime;
    }

    public void setPayEndTime(Date payEndTime) {
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

    public Date getCreateBeginTime() {
        return createBeginTime;
    }

    public void setCreateBeginTime(Date createBeginTime) {
        this.createBeginTime = createBeginTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

    public Integer getOrderPaymentMethod() {
        return orderPaymentMethod;
    }

    public void setOrderPaymentMethod(Integer orderPaymentMethod) {
        this.orderPaymentMethod = orderPaymentMethod;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
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
