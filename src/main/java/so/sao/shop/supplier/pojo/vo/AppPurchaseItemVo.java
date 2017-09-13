package so.sao.shop.supplier.pojo.vo;

import so.sao.shop.supplier.util.NumberUtil;

import java.math.BigDecimal;

/**
 * Created by acer on 2017/9/6.
 */
public class AppPurchaseItemVo {

    /**
     * 商品图片
     */
    private String goodsImage;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品单价
     */
    private String goodsUnitPrice;

    /**
     * 商品数量
     */
    private Integer goodsNumber;

    /**
     * 商品总价
     */
    private String goodsTatolPrice;
    /**
     * 商品规格
     */
    private String goodsAttribute;
    /**
     * 订单编号
     */
    private String orderId;

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

    public String getGoodsUnitPrice() {
        return goodsUnitPrice;
    }

    public void setGoodsUnitPrice(String goodsUnitPrice) {
        this.goodsUnitPrice = NumberUtil.number2Thousand(new BigDecimal(goodsUnitPrice));
    }

    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getGoodsTatolPrice() {
        return goodsTatolPrice;
    }

    public void setGoodsTatolPrice(String goodsTatolPrice) {
        this.goodsTatolPrice = NumberUtil.number2Thousand(new BigDecimal(goodsTatolPrice));
    }

    public String getGoodsAttribute() {
        return goodsAttribute;
    }

    public void setGoodsAttribute(String goodsAttribute) {
        this.goodsAttribute = goodsAttribute;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
