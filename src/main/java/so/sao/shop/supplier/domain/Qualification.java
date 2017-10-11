package so.sao.shop.supplier.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 供应商资质实体类
 * Created By liugang ON 2017/10/11
 */
public class Qualification {

    /**
     * ID
     */
    private Long id;

    /**
     * 供应商ID
     */
    private Long accountId;

    /**
     * 资质类型（1、质检报告  2、营业执照  3、授权报告  4、食品流通许可证）
     */
    private Integer qualificationType;

    /**
     * 资质上传类型（1、小食品零售  2、其他）
     */
    private Integer uploadType;

    /**
     * 资质状态（1、待审核  2、审核通过  3、审核未通过）
     */
    private Integer status;

    /**
     * 审核未通过原因
     */
    private String reason;

    /**
     * 删除状态（0、未删除（默认）  1、删除）
     */
    private Integer delete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Integer getQualificationType() {
        return qualificationType;
    }

    public void setQualificationType(Integer qualificationType) {
        this.qualificationType = qualificationType;
    }

    public Integer getUploadType() {
        return uploadType;
    }

    public void setUploadType(Integer uploadType) {
        this.uploadType = uploadType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
