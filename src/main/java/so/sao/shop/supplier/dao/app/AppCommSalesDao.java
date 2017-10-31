package so.sao.shop.supplier.dao.app;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.pojo.input.AppCommSalesInput;

import java.util.List;

/**
 *<p>Version: shop-supplier V1.1.0 </p>
 *<p>Title: AppCommSalesDao</p>
 *<p>Description:商品销量DAO</p>
 *@author: liugang
 *@Date: Created in 2017/10/30 15:53
 */
public interface AppCommSalesDao {
    /**
     * 根据商品ID统计已售商品数量
     *
     * @param goodsIds 商品ID列表
     * @return Integer 统计数
     */
    List countSoldCommNum(@Param("goodsIds") String[] goodsIds) throws Exception;

    /**
     * 更新商品销量
     * @param commSalesInputs 商品销量更新入参
     * @return
     */
    boolean updateSalesNum(@Param("commSalesInputs")List<AppCommSalesInput> commSalesInputs);

    /**
     * 添加商品销量
     * @param goodsIds 商品ID集合
     * @return
     */
    boolean saveCommSales(@Param("goodsIds")String[] goodsIds);
}
