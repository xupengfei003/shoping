package so.sao.shop.supplier.service;

import java.util.List;

/**
 * Created by acer on 2017/9/8.
 */
public interface CountSoldCommService {
    /**
     * 根据商品ID统计已销售商品数量
     *
     * @param goodsIds 商品ID列表
     * @return List<String> 统计数
     */
    List<String> countSoldCommNum(String[] goodsIds) throws Exception ;
}
