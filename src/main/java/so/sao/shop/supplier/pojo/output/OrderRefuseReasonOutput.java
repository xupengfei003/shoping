package so.sao.shop.supplier.pojo.output;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date refuseTime;
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

    public Date getRefuseTime() {
        return refuseTime;
    }

    public void setRefuseTime(Date refuseTime) {
        this.refuseTime = refuseTime;
    }

    public List<String> getRefuseImgUrl() {
        return refuseImgUrl;
    }

    public void setRefuseImgUrl(List<String> refuseImgUrl) {
        this.refuseImgUrl = refuseImgUrl;
    }
}
