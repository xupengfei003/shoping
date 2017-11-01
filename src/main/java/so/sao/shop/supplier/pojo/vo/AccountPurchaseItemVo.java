package so.sao.shop.supplier.pojo.vo;

import so.sao.shop.supplier.util.NumberUtil;

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
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品数量
     */
    private Integer goodsNumber;

    /**
     * 商品商家编号
     */
    private String code;

    /**
     * 商品条码
     */
    private String code69;

    /**
     * 规格值
     */
    private String goodsAttribute;

    /**
     * app订货价(科学计数法)
     */
    private String goodsUnitPrice;

    /**
     * 商品总价(科学记数法)
     */
    private String goodsTatolPrice;

    /**
     * 商品图片
     */
    private String goodsImage;

    /**
     * 商品品牌
     */
    private String brandName;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode69() {
        return code69;
    }

    public void setCode69(String code69) {
        this.code69 = code69;
    }

    public String getGoodsAttribute() {
        return goodsAttribute;
    }

    public void setGoodsAttribute(String goodsAttribute) {
        this.goodsAttribute = goodsAttribute;
    }

    public String getGoodsUnitPrice() {
        return NumberUtil.number2Thousand(new BigDecimal(goodsUnitPrice));
    }


    public void setGoodsUnitPrice(String goodsUnitPrice) {
        this.goodsUnitPrice = goodsUnitPrice;
    }

    public String getGoodsTatolPrice() {
        return NumberUtil.number2Thousand(new BigDecimal(goodsTatolPrice));
    }

    public void setGoodsTatolPrice(String goodsTatolPrice) {
        this.goodsTatolPrice = goodsTatolPrice;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Override
    public String toString() {
        return "AccountPurchaseItemVo{" +
                "goodsName='" + goodsName + '\'' +
                ", goodsNumber=" + goodsNumber +
                ", code='" + code + '\'' +
                ", code69='" + code69 + '\'' +
                ", goodsAttribute='" + goodsAttribute + '\'' +
                ", goodsUnitPrice='" + goodsUnitPrice + '\'' +
                ", goodsTatolPrice='" + goodsTatolPrice + '\'' +
                ", goodsImage='" + goodsImage + '\'' +
                ", brandName='" + brandName + '\'' +
                '}';
    }
}
