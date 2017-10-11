package so.sao.shop.supplier.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 供应商资质图片实体类
 * Created By liugang ON 2017/10/11
 */
public class QualificationImage {

    /**
     * ID
     */
    private Long id;

    /**
     * 资质ID
     */
    private Long qualificationId;

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
     * 云端名称
     */
    private String cloudName;

    /**
     * 云端缩略图名称
     */
    private String minCloudName;

    /**
     * 删除状态（0、未删除（默认）  1、删除）
     */
    private Integer delete;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(Long qualificationId) {
        this.qualificationId = qualificationId;
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

    public Integer getDelete() {
        return delete;
    }

    public void setDelete(Integer delete) {
        this.delete = delete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
