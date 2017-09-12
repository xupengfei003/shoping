package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.AppCartItem;

import java.util.List;

public interface AppCartItemDao {

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