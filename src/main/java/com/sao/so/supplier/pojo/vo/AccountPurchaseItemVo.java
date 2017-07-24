package com.sao.so.supplier.pojo.vo;

import java.math.BigDecimal;

/**
 * <p>
 * 订单明细表对应的vo类
 * </p>
 *
 * @author 透云-中软-西安项目组-zhenhai.zheng
 * @since 2017年7月20日
 */
public class AccountPurchaseItemVo {
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
     * 商品名称
     */
    private String goodsName;

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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @Override
    public String toString() {
        return "PurchaseItemVo{" +
                "goodsId=" + goodsId +
                ", goodsNumber=" + goodsNumber +
                ", goodsUnitPrice=" + goodsUnitPrice +
                ", goodsName='" + goodsName + '\'' +
                '}';
    }
}
