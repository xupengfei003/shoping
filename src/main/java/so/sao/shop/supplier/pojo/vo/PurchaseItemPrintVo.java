package so.sao.shop.supplier.pojo.vo;

import so.sao.shop.supplier.util.NumberUtil;

import java.math.BigDecimal;

/**
 * 打印订单页面的商品条目的vo对象
 *
 * @author hengle.yang
 * @since 2017/8/11 8:56
 */
public class PurchaseItemPrintVo {

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品编号（商品编码）
     */
    private Long goodsId;

    /**
     * 品牌名称（商品品牌）
     */
    private String brandName;

    /**
     * 商品属性（商品规格）
     */
    private String goodsAttribute;

    /**
     * 商品单价（单价）
     */
    private BigDecimal goodsUnitPrice;

    /**
     * 商品数量（数量）
     */
    private Integer goodsNumber;

    /**
     * 商品总价（总价）
     */
    private BigDecimal goodsTatolPrice;

    /**
     * 千分位商品总价（总价）
     */
    private String goodsTatolPriceFormat;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getGoodsAttribute() {
        return goodsAttribute;
    }

    public void setGoodsAttribute(String goodsAttribute) {
        this.goodsAttribute = goodsAttribute;
    }

    public String getGoodsUnitPrice() {
        return NumberUtil.number2Thousand(goodsUnitPrice);
    }

    public void setGoodsUnitPrice(BigDecimal goodsUnitPrice) {
        this.goodsUnitPrice = goodsUnitPrice;
    }

    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public BigDecimal getGoodsTatolPrice() {
        return goodsTatolPrice;
    }

    public void setGoodsTatolPrice(BigDecimal goodsTatolPrice) {
        this.goodsTatolPrice = goodsTatolPrice;
    }

    public String getGoodsTatolPriceFormat() {
        return NumberUtil.number2Thousand(this.goodsTatolPrice);
    }

    public void setGoodsTatolPriceFormat(String goodsTatolPriceFormat) {
        this.goodsTatolPriceFormat = goodsTatolPriceFormat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PurchaseItemPrintVo that = (PurchaseItemPrintVo) o;

        if (goodsName != null ? !goodsName.equals(that.goodsName) : that.goodsName != null) return false;
        if (goodsId != null ? !goodsId.equals(that.goodsId) : that.goodsId != null) return false;
        if (brandName != null ? !brandName.equals(that.brandName) : that.brandName != null) return false;
        if (goodsAttribute != null ? !goodsAttribute.equals(that.goodsAttribute) : that.goodsAttribute != null)
            return false;
        if (goodsUnitPrice != null ? !goodsUnitPrice.equals(that.goodsUnitPrice) : that.goodsUnitPrice != null)
            return false;
        if (goodsNumber != null ? !goodsNumber.equals(that.goodsNumber) : that.goodsNumber != null) return false;
        if (goodsTatolPrice != null ? !goodsTatolPrice.equals(that.goodsTatolPrice) : that.goodsTatolPrice != null)
            return false;
        return goodsTatolPriceFormat != null ? goodsTatolPriceFormat.equals(that.goodsTatolPriceFormat) : that.goodsTatolPriceFormat == null;
    }

    @Override
    public int hashCode() {
        int result = goodsName != null ? goodsName.hashCode() : 0;
        result = 31 * result + (goodsId != null ? goodsId.hashCode() : 0);
        result = 31 * result + (brandName != null ? brandName.hashCode() : 0);
        result = 31 * result + (goodsAttribute != null ? goodsAttribute.hashCode() : 0);
        result = 31 * result + (goodsUnitPrice != null ? goodsUnitPrice.hashCode() : 0);
        result = 31 * result + (goodsNumber != null ? goodsNumber.hashCode() : 0);
        result = 31 * result + (goodsTatolPrice != null ? goodsTatolPrice.hashCode() : 0);
        result = 31 * result + (goodsTatolPriceFormat != null ? goodsTatolPriceFormat.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PurchaseItemPrintVo{" +
                "goodsName='" + goodsName + '\'' +
                ", goodsId=" + goodsId +
                ", brandName='" + brandName + '\'' +
                ", goodsAttribute='" + goodsAttribute + '\'' +
                ", goodsUnitPrice=" + goodsUnitPrice +
                ", goodsNumber=" + goodsNumber +
                ", goodsTatolPrice=" + goodsTatolPrice +
                ", goodsTatolPriceFormat='" + goodsTatolPriceFormat + '\'' +
                '}';
    }
}
