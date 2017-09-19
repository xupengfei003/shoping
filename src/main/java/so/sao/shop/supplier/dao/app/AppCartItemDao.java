package so.sao.shop.supplier.dao.app;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.AppCartItem;

import java.util.List;

public interface AppCartItemDao {

    /**
     * 根据购物车记录ID删除该记录
     * @param id
     * @return
     */
    int deleteById(@Param("id")Long id,@Param("userId")Long userId) throws Exception;

    /**
     * 根据购物车记录ID和userId查询购物车记录中的商品Id
     * @param cartitemId
     * @param userId
     * @return
     * @throws Exception
     */
    AppCartItem selectByIdAndUserId(@Param("cartitemId")Long cartitemId,@Param("userId")Long userId) throws Exception;

    /**
     * 根据购物车记录ID更新购物车商品数量
     * @param appCartItem
     * @throws Exception
     */
    int updateById(AppCartItem appCartItem) throws Exception;

    /**
     * 根据商品ID和用户ID查询购物车记录中的购物车记录ID
     * @param commodityId
     * @param userId
     * @return
     * @throws Exception
     */
    AppCartItem selectByCommodityId(@Param("commodityId")Long commodityId,@Param("userId")Long userId) throws Exception;

    /**
     * 给购物车表中插入一条记录
     * @param appCartItem
     * @return
     * @throws Exception
     */
    int insertOne(AppCartItem appCartItem) throws Exception;

    /**
     * 根据UserId查询购物车商品ID和商品数量
     * @param userId
     * @return
     * @throws Exception
     */
    List<AppCartItem> selectByUserId (@Param("userId")Long userId) throws Exception;

}