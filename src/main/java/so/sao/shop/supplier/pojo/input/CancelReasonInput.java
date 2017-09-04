package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by acer on 2017/8/25.
 */
public class CancelReasonInput {
    /**
     * 取消原因
     */
    @NotEmpty(message = "取消原因不能为空")
    private String cancelReason;

    /**
     * 订单编号
     */
    @NotEmpty(message = "订单编号不能为空")
    private String orderId;

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
}
