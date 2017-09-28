package so.sao.shop.supplier.pojo.vo;

/**
 * Created by acer on 2017/9/19.
 */
public class CountSoldNumVo {
    /**
     * 商品ID
     */
    private Integer goodsId;
    /**
     * 商品销售数量
     */
    private Integer goodsCountNum;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsCountNum() {
        return goodsCountNum;
    }

    public void setGoodsCountNum(Integer goodsCountNum) {
        this.goodsCountNum = goodsCountNum;
    }
}
