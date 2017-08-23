package so.sao.shop.supplier.domain;

/**
 * Created by niewenchao on 2017/8/11.
 */
public class RecordPurchase {

    private Long id;

    /**
     * 提现申请记录id
     */
    private String recordId;

    /**
     * 订单id
     */
    private String orderId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}
