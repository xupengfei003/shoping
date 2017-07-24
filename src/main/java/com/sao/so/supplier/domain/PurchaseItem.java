package com.sao.so.supplier.domain;

import java.math.BigDecimal;

/**
 * <p>
 * 订单详情
 * </p>
 *
 * @author 透云-中软-西安项目组
 * @since 2017-07-19
 */
public class PurchaseItem {
    /**
     * 详情编号
     */
    private String detailsId;
    /**
     * 商品属性
     */
    private String goodsAttribute;
    /**
     * 商品编号
     */
    private Long goodsId;
    /**
     * 商品数量
     */
    private Integer goodsNumber;
    /**
     * 商品单价
     */
    private BigDecimal goodsUnitPrice;
    /**
     * 商品总价
     */
    private BigDecimal goodsTatolPrice;
    /**
     * 商品优惠
     */
    private BigDecimal goodsFavourable;
    /**
     * 商品图片
     */
    private String goodsImage;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 品牌名称
     */
    private String brandName;


    public String getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(String detailsId) {
        this.detailsId = detailsId;
    }

    public String getGoodsAttribute() {
        return goodsAttribute;
    }

    public void setGoodsAttribute(String goodsAttribute) {
        this.goodsAttribute = goodsAttribute;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public BigDecimal getGoodsUnitPrice() {
        return goodsUnitPrice;
    }

    public void setGoodsUnitPrice(BigDecimal goodsUnitPrice) {
        this.goodsUnitPrice = goodsUnitPrice;
    }

    public BigDecimal getGoodsTatolPrice() {
        return goodsTatolPrice;
    }

    public void setGoodsTatolPrice(BigDecimal goodsTatolPrice) {
        this.goodsTatolPrice = goodsTatolPrice;
    }

    public BigDecimal getGoodsFavourable() {
        return goodsFavourable;
    }

    public void setGoodsFavourable(BigDecimal goodsFavourable) {
        this.goodsFavourable = goodsFavourable;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
