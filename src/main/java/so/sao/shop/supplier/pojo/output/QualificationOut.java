package so.sao.shop.supplier.pojo.output;

/**
 * Created by tengfei.zhang on 2017/10/12.
 */
public class QualificationOut {
    /**
     * 资质id
     */
    private Long id;
    /**
     * 审核原因
     */
    private String reason;
    /**
     * 资质类型
     */
    private String qualificationType;
    /**
     * 大图云端名称
     */
    private String cloudName;
    /**
     * 缩略图云端名称
     */
    private String minCloudName;
    /**
     * 图片名称
     */
    private String fileName;
    /**
     * 大图云端地址
     */
    private String url;
    /**
     * 缩略图云端地址
     */
    private String minImgUrl;
    /**
     * 审核状态
     */
    private String qualificationStatus;
    /**
     * 是否已读（0、未读 1、已读）
     */
    private Integer isRead;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getQualificationType() {
        return qualificationType;
    }

    public void setQualificationType(String qualificationType) {
        this.qualificationType = qualificationType;
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

    public String getQualificationStatus() {
        return qualificationStatus;
    }

    public void setQualificationStatus(String qualificationStatus) {
        this.qualificationStatus = qualificationStatus;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
}
