package so.sao.shop.supplier.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 供应商商品关系表
 * Created by QuJunLong on 2017/7/18.
 */
public class SupplierCommodity {
    /**
     * ID
     */
    private Long id;
    /**
     * SKU
     */
    private String sku;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 商品标签ID
     */
    private Long tagId;
    /**
     * 商家编码
     */
    private String code;
    /**
     * 商品编码
     */
    private String code69;
    /**
     * 商品描述
     */
    private String remark;
    /**
     * 商品介绍
     */
    private String description;
    /**
     * 商品状态
     * 已上架 待上架 已下架
     */
    private int status;
    /**
     * 计量规格ID
     */
    private Long measureSpecId;
    /**
     * 规格值
     */
    private String ruleVal;

    /**
     * 计量单位ID
     */
    private Long unitId;
    /**
     * 库存
     */
    private Double inventory;
    /**
     * 缩略图
     */
    private String minImg;
    /**
     * 市场价
     */
    private BigDecimal price;
    /**
     * 售价
     */
    private BigDecimal unitPrice;
    /**
     * 删除标记
     */
    private int deleted;
    /**
     *  创建人
     */
    private Long createdBy;
    /**
     *  创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createdAt;
    /**
     * 更新人
     */
    private Long updatedBy;
    /**
     *  更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updatedAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Double getInventory() {
        return inventory;
    }

    public void setInventory(Double inventory) {
        this.inventory = inventory;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode69() {
        return code69;
    }

    public void setCode69(String code69) {
        this.code69 = code69;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRuleVal() {
        return ruleVal;
    }

    public void setRuleVal(String ruleVal) {
        this.ruleVal = ruleVal;
    }

    public String getMinImg() {
        return minImg;
    }

    public void setMinImg(String minImg) {
        this.minImg = minImg;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getMeasureSpecId() {
        return measureSpecId;
    }

    public void setMeasureSpecId(Long measureSpecId) {
        this.measureSpecId = measureSpecId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }
}
