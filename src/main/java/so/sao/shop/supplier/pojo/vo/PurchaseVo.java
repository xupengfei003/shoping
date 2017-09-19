package so.sao.shop.supplier.pojo.vo;


/**
 * Created by niewenchao on 2017/7/24.
 */
public class PurchaseVo {

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
     * 订单实付金额
     */
    private String orderPrice;

    /**
     * 订单结算金额
     */
    private String orderSettlemePrice;

    /**
     * 订单运费
     */
    private String orderPostage;

    /**
     * 创建时间
     */
    private String orderCreateTime;

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

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderSettlemePrice() {
        return orderSettlemePrice;
    }

    public void setOrderSettlemePrice(String orderSettlemePrice) {
        this.orderSettlemePrice = orderSettlemePrice;
    }

    public String getOrderPostage() {
        return orderPostage;
    }

    public void setOrderPostage(String orderPostage) {
        this.orderPostage = orderPostage;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }
}
