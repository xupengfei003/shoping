package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by acer on 2017/8/15.
 */
public class PayInput {
    /**
     * 订单ID(付款后合并的订单编号)
     */
    @NotEmpty(message = "订单ID不能为空")
    private String orderId;
    /**
     * 订单实付金额
     */
    @DecimalMin("0")
    @NotNull(message = "付款金额不能为空")
    private BigDecimal orderPrice;
    /**
     * 订单支付流水号
     */
    @NotEmpty(message = "支付流水号不能为空")
    private String orderPaymentNum;
    /**
     * 支付方式（1、支付宝；2、微信）
     */
    @NotNull(message = "支付方式不能为空")
    private Integer orderPaymentMethod;

    /**
     * 支付回调加密码
     */
    @NotEmpty(message = "支付回调加密码不能为空")
    private String sign;

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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
