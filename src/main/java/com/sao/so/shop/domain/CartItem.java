package com.sao.so.shop.domain;

import java.math.BigDecimal;

public class CartItem {

    /**
     * 记录ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 商品id
     */
    private Long commodityId;
    /**
     * 价格
     */
    private BigDecimal commodityPrice;

    /**
     * 商品名称
     */
    private String commodityName;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 商品图片路径
     */
    private String commodityPic;
    /**
     * 商品属性
     */
    private String commodityProperties;

    /**
     * 创建时间
     */
    private Long createdAt;
    /**
     * 更新时间
     */
    private Long updatedAt;
    /**
     * 数量
     */
    private Integer count;

    /**
     * 供应商对应的商品
     */
    private SupplierCommodity supplierCommodity;
    /**
     * 商品
     */
    private Commodity commodity;
    /**
     * 用户
     */
    private User user;

    public String getCommodityProperties() {
        return commodityProperties;
    }

    public void setCommodityProperties(String commodityProperties) {
        this.commodityProperties = commodityProperties;
    }

    public SupplierCommodity getSupplierCommodity() {
        return supplierCommodity;
    }

    public void setSupplierCommodity(SupplierCommodity supplierCommodity) {
        this.supplierCommodity = supplierCommodity;
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public BigDecimal getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(BigDecimal commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName == null ? null : commodityName.trim();
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }

    public String getCommodityPic() {
        return commodityPic;
    }

    public void setCommodityPic(String commodityPic) {
        this.commodityPic = commodityPic == null ? null : commodityPic.trim();
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}