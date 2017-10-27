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
     * 商品编号
     */
    private String goodsId;

    /**
     * 商品编码
     */
    private String code69;

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
//    private String goodsTatolPriceFormat;

    /**
     * 库存单位
     */
    private String goodsUnitName;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getCode69() {
        return code69;
    }

    public void setCode69(String code69) {
        this.code69 = code69;
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

    public String getGoodsTatolPrice() {
        return NumberUtil.number2Thousand(goodsTatolPrice);
    }

    public void setGoodsTatolPrice(BigDecimal goodsTatolPrice) {
        this.goodsTatolPrice = goodsTatolPrice;
    }



    public String getGoodsUnitName() {
        return goodsUnitName;
    }

    public void setGoodsUnitName(String goodsUnitName) {
        this.goodsUnitName = goodsUnitName;
    }
}
