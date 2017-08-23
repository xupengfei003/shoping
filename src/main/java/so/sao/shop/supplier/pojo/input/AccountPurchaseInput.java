package so.sao.shop.supplier.pojo.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
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
     * 起始金额
     */
    @Min(value = 0,message = "金额不能为负数")
    private BigDecimal beginMoney;

    /**
     * 截至金额
     */
    @Min(value = 0,message = "金额不能为负数")
    private BigDecimal endMoney;

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
    @Length(min = 30,max = 30,message = "订单编号为30位")
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

    @Override
    public String toString() {
        return "AccountPurchaseInput{" +
                "payBeginTime='" + payBeginTime + '\'' +
                ", payEndTime='" + payEndTime + '\'' +
                ", createBeginTime='" + createBeginTime + '\'' +
                ", createEndTime='" + createEndTime + '\'' +
                ", beginMoney=" + beginMoney +
                ", endMoney=" + endMoney +
                ", orderReceiverName='" + orderReceiverName + '\'' +
                ", orderReceiverMobile='" + orderReceiverMobile + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
