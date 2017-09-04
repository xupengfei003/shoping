package so.sao.shop.supplier.pojo.vo;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 订单详情
 * </p>
 *
 * @author 透云-中软-西安项目组
 * @since 2017-07-21
 */
public class PurchaseItemVo {
    /**
     * 商品属性
     */
    @NotEmpty(message="商品属性不能为空")
    private String goodsAttribute;
    /**
     * 商品编号
     */
    @NotNull(message="商品编号不能为空")
    private Long goodsId;
    /**
     * 商品数量
     */
    @NotNull(message="商品数量不能为空")
    private Integer goodsNumber;
    /**
     * 商品总价
     */
    private String goodsTatolPrice;
    /**
     * 商品图片
     */
    private String goodsImage;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 商品单价
     */
    private String goodsUnitPrice;

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


    public String getGoodsTatolPrice() {
        return goodsTatolPrice;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getGoodsUnitPrice() {
        return goodsUnitPrice;
    }

    public void setGoodsUnitPrice(String goodsUnitPrice) {
        this.goodsUnitPrice = goodsUnitPrice;
    }
}
