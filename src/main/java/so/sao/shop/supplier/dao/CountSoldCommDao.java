package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by acer on 2017/9/8.
 */
public interface CountSoldCommDao {
    /**
     * 根据商品ID统计已售商品数量
     *
     * @param goodsIds 商品ID列表
     * @return Integer 统计数
     */
    List countSoldCommNum(@Param("goodsIds") String[] goodsIds) throws Exception;
}
