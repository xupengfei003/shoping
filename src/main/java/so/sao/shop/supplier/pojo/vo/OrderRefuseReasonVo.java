package so.sao.shop.supplier.pojo.vo;

/**
 * Created by acer on 2017/9/15.
 */
public class OrderRefuseReasonVo {
    /**
     * 拒收类型
     */
    private String refuseType;
    /**
     * 拒收理由
     */
    private String refuseReason;
    /**
     * 拒收时间
     */
    private String refuseTime;

    public String getRefuseType() {
        return refuseType;
    }

    public void setRefuseType(String refuseType) {
        this.refuseType = refuseType;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getRefuseTime() {
        return refuseTime;
    }

    public void setRefuseTime(String refuseTime) {
        this.refuseTime = refuseTime;
    }
}
