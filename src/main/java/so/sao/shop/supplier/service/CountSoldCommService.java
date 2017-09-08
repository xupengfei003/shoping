package so.sao.shop.supplier.service;

/**
 * Created by acer on 2017/9/8.
 */
public interface CountSoldCommService {
    /**
     * 根据商品ID统计已销售商品数量
     *
     * @param goodsId 商品ID
     * @return Integer 统计数
     */
    Integer countSoldCommNum(String goodsId) throws Exception ;
}
