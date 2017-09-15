package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by acer on 2017/8/25.
 */
public class CancelReasonInput {
    /**
     * 订单编号
     */
    @NotEmpty(message = "订单编号不能为空")
    private String orderId;
    /**
     * 取消类型
     */
    private String cancelType;
    /**
     * 取消原因
     */
    private String cancelReason;

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCancelType() {
        return cancelType;
    }

    public void setCancelType(String cancelType) {
        this.cancelType = cancelType;
    }
}
