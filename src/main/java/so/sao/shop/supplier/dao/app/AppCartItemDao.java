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



    int deleteByPrimaryKey(Long id);

    int insert(AppCartItem record);

    int insertSelective(AppCartItem record);

    AppCartItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AppCartItem record);

    int updateByPrimaryKey(AppCartItem record);

    /**
     * 根据用户ID查找购物车中的商品信息
     * @param userId
     * @return
     */
    List<AppCartItem> findCartItemByUserId(Long userId);

    /**
     * 根据ID查找到关联的商品信息和商户信息
     * @param id
     * @return
     */
    AppCartItem findCartItemById(Long id);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteBatchByPrimaryKey(List<Long> ids);

    /**
     * 通过USERID,供应商id和产品id查找唯一记录
     * @param userId
     * @param commodityId
     */
    List<AppCartItem> findExistsCartItem(@Param("user_id")Long userId,@Param("commodity_id")Long commodityId);
}