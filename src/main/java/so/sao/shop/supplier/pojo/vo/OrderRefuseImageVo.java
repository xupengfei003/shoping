package so.sao.shop.supplier.pojo.vo;

import java.util.List;

/**
 * Created by acer on 2017/9/15.
 */
public class OrderRefuseImageVo {
    /**
     * 拒收图片
     */
    private String refuseImgUrl;
    /**
     * 拒收缩略图片
     */
    private String refuseMinImgUrl;
    /**
     * 拒收图片名称
     */
    private String fileName;

    public String getRefuseImgUrl() {
        return refuseImgUrl;
    }

    public void setRefuseImgUrl(String refuseImgUrl) {
        this.refuseImgUrl = refuseImgUrl;
    }

    public String getRefuseMinImgUrl() {
        return refuseMinImgUrl;
    }

    public void setRefuseMinImgUrl(String refuseMinImgUrl) {
        this.refuseMinImgUrl = refuseMinImgUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
