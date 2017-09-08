package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;

/**
 * Created by acer on 2017/9/8.
 */
public interface CountSoldCommDao {
    /**
     * 根据商品ID统计已售商品数量
     *
     * @param goodsId 商品ID
     * @return Integer 统计数
     */
    Integer countSoldCommNum(@Param("goodsId") String goodsId) throws Exception;
}
