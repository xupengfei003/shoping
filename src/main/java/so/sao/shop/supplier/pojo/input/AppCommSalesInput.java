package so.sao.shop.supplier.pojo.input;

/**
 * <p>Version: supplier V1.1.0 </p>
 * <p>Title: AppCommSalesInput</p>
 * <p>Description: 商品销量更新入参</p>
 * @author: liugang
 * @Date: Created in 2017/10/31 9:02
 */
public class AppCommSalesInput {

    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 销售数量
     */
    private Integer goodsNum;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }
}
