package so.sao.shop.supplier.pojo.output;

/**
 * Created by acer on 2017/8/17.
 */
public class OrderCancelReasonOutput {
    /**
     * 取消类型
     */
    private String cancelType;
    /**
     * 取消理由
     */
    private String orderCancelReason;

    public String getCancelType() {
        return cancelType;
    }

    public void setCancelType(String cancelType) {
        this.cancelType = cancelType;
    }

    public String getOrderCancelReason() {
        return orderCancelReason;
    }

    public void setOrderCancelReason(String orderCancelReason) {
        this.orderCancelReason = orderCancelReason;
    }
}
