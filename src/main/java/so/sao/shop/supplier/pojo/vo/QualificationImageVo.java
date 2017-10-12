package so.sao.shop.supplier.pojo.vo;

/**
 * Created by liugang on 2017/10/11.
 */
public class QualificationImageVo {

    /**
     * 图片名称
     */
    private String fileName;

    /**
     * 云端大图地址
     */
    private String url;

    /**
     * 云端缩略图地址
     */
    private String minImgUrl;

    /**
     * 原图云端名称
     */
    private String cloudName;

    /**
     * 缩略图云端名称
     */
    private String minCloudName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMinImgUrl() {
        return minImgUrl;
    }

    public void setMinImgUrl(String minImgUrl) {
        this.minImgUrl = minImgUrl;
    }

    public String getCloudName() {
        return cloudName;
    }

    public void setCloudName(String cloudName) {
        this.cloudName = cloudName;
    }

    public String getMinCloudName() {
        return minCloudName;
    }

    public void setMinCloudName(String minCloudName) {
        this.minCloudName = minCloudName;
    }
}
