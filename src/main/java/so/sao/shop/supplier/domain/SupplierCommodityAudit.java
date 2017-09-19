package so.sao.shop.supplier.domain;

import java.util.Date;

public class SupplierCommodityAudit {

    /**
     * id
     */
    private Long id;

    /**
     * 供应商商品表id
     */
    private Long scId;

    /**
     * 供应商id
     */
    private Long supplierId;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 提交审核时间
     */
    private Date createdAt;

    /**
     * 审核状态
     */
    private int status;

    /**
     * 审核状态
     */
    private int auditResult;

    /**
     * 审核记录标记,1代表供应商当前审核记录
     */
    private int auditFlag;

    /**
     * 审核人
     */
    private Long auditBy;

    /**
     * 审核意见
     */
    private String auditOpinion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScId() {
        return scId;
    }

    public void setScId(Long scId) {
        this.scId = scId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(int auditResult) {
        this.auditResult = auditResult;
    }

    public int getAuditFlag() {
        return auditFlag;
    }

    public void setAuditFlag(int auditFlag) {
        this.auditFlag = auditFlag;
    }

    public Long getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(Long auditBy) {
        this.auditBy = auditBy;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }
}
