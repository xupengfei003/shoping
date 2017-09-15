package so.sao.shop.supplier.pojo.output;

import java.util.List;

/**
 * Created by acer on 2017/8/17.
 */
public class OrderRefuseReasonOutput {
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
    /**
     * 拒收图片列表
     */
    private List<String> refuseImgUrl;

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

    public List<String> getRefuseImgUrl() {
        return refuseImgUrl;
    }

    public void setRefuseImgUrl(List<String> refuseImgUrl) {
        this.refuseImgUrl = refuseImgUrl;
    }
}
