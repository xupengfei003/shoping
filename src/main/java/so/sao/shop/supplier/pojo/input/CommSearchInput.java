package so.sao.shop.supplier.pojo.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

public class CommSearchInput {

    /**
     * 供应商id
     */
    private Long supplierId;

    /**
     * 商品条码
     */
    private String commCode69;

    /**
     * 商家编码
     */
    private String code;


    /**
     * 商品商家品牌
     */
    private String commBrand;

    /**
     * 商品名称
     */
    private String commName;

    /**
     * 商品状态
     */
    private Integer status;

    /**
     *	一级类型ID
     */
    private Long typeOneId;
    /**
     *  二级类型ID
     */
    private Long typeTwoId;
    /**
     *三级类型ID
     */
    private Long typeThreeId;
    /**
     * 供货售价[低]
     */
    private BigDecimal minPrice;
    /**
     * 供货售价[高]
     */
    private BigDecimal maxPrice;
    /**
    * 更新时间[前]
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss",iso= DateTimeFormat.ISO.DATE)
    private Date beginUpdateAt;
    /**
     * 更新时间[后]
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss",iso= DateTimeFormat.ISO.DATE)
    private Date endUpdateAt;
    /**
    * 当前页号
    */
    private Integer pageNum;
    /**
     * 页面大小
     */
    private Integer pageSize;
    /**
    * 审核结果：0代表未通过，1代表通过
    */
    private Integer auditResult;
    /**
     * 供应商名称
     */
    private String supplierName;


    /**
     * 用来区分管理员的字段
     */
    private String role;


    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getCommCode69() {
        return commCode69;
    }

    public void setCommCode69(String commCode69) {
        this.commCode69 = commCode69;
    }

    public String getCommBrand() {
        return commBrand;
    }

    public void setCommBrand(String commBrand) {
        this.commBrand = commBrand;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getTypeOneId() {
        return typeOneId;
    }

    public void setTypeOneId(Long typeOneId) {
        this.typeOneId = typeOneId;
    }

    public Long getTypeTwoId() {
        return typeTwoId;
    }

    public void setTypeTwoId(Long typeTwoId) {
        this.typeTwoId = typeTwoId;
    }

    public Long getTypeThreeId() {
        return typeThreeId;
    }

    public void setTypeThreeId(Long typeThreeId) {
        this.typeThreeId = typeThreeId;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Date getBeginUpdateAt() {
        return beginUpdateAt;
    }

    public void setBeginUpdateAt(Date beginUpdateAt) {
        this.beginUpdateAt = beginUpdateAt;
    }

    public Date getEndUpdateAt() {
        return endUpdateAt;
    }

    public void setEndUpdateAt(Date endUpdateAt) {
        this.endUpdateAt = endUpdateAt;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(Integer auditResult) {
        this.auditResult = auditResult;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "CommSearchInput{" +
                "supplierId=" + supplierId +
                ", commCode69='" + commCode69 + '\'' +
                ", code='" + code + '\'' +
                ", commBrand='" + commBrand + '\'' +
                ", commName='" + commName + '\'' +
                ", status=" + status +
                ", typeOneId=" + typeOneId +
                ", typeTwoId=" + typeTwoId +
                ", typeThreeId=" + typeThreeId +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", beginUpdateAt=" + beginUpdateAt +
                ", endUpdateAt=" + endUpdateAt +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", auditResult=" + auditResult +
                ", supplierName='" + supplierName + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
