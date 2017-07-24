package com.sao.so.supplier.domain;

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
     * 品牌
     */
    private Long brandId;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 商家编码
     */
    private String code;
    /**
     * 商品编码
     */
    private String code69;
    /**
     * 一级类型Id
     */
    private Long categoryOneId;
    /**
     * 二级类型Id
     */
    private Long categoryTwoId;
    /**
     * 三级类型Id
     */
    private Long categoryThreeId;
    /**
     * 商品
     */
    private Long commodityId;
    /**
     * 商品名称
     */
    private String name;
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
     * 规格名称
     */
    private String ruleName;
    /**
     * 规格值
     */
    private String ruleVal;

    /**
     * 计量单位
     */
    private String unit;
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
    private Double price;
    /**
     * 售价
     */
    private Double unitPrice;
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
    private Long createdAt;
    /**
     * 更新人
     */
    private Long updatedBy;
    /**
     *  更新时间
     */
    private Long updatedAt;


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

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
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

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getCode69() {
        return code69;
    }

    public void setCode69(String code69) {
        this.code69 = code69;
    }

    public Long getCategoryOneId() {
        return categoryOneId;
    }

    public void setCategoryOneId(Long categoryOneId) {
        this.categoryOneId = categoryOneId;
    }

    public Long getCategoryTwoId() {
        return categoryTwoId;
    }

    public void setCategoryTwoId(Long categoryTwoId) {
        this.categoryTwoId = categoryTwoId;
    }

    public Long getCategoryThreeId() {
        return categoryThreeId;
    }

    public void setCategoryThreeId(Long categoryThreeId) {
        this.categoryThreeId = categoryThreeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleVal() {
        return ruleVal;
    }

    public void setRuleVal(String ruleVal) {
        this.ruleVal = ruleVal;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMinImg() {
        return minImg;
    }

    public void setMinImg(String minImg) {
        this.minImg = minImg;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
