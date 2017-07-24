package com.sao.so.supplier.dao;

import com.sao.so.supplier.domain.CartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartItemDao {

    int deleteByPrimaryKey(Long id);

    int insert(CartItem record);

    int insertSelective(CartItem record);

    CartItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CartItem record);

    int updateByPrimaryKey(CartItem record);

    /**
     * 根据用户ID查找购物车中的商品信息
     * @param userId
     * @return
     */
    List<CartItem> findCartItemByUserId(Long userId);

    /**
     * 根据ID查找到关联的商品信息和商户信息
     * @param id
     * @return
     */
    CartItem findCartItemById(Long id);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteBatchByPrimaryKey(List<Long> ids);

    /**
     * 通过USERID,供应商id和产品id查找唯一记录
     * @param userId
     * @param supplierId
     * @param commodityId
     */
    CartItem findExistsCartItem(@Param("user_id")Long userId, @Param("supplier_id")Long supplierId, @Param("commodity_id")Long commodityId);
}