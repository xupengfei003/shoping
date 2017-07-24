package com.sao.so.supplier.dao;



import com.sao.so.supplier.domain.DeliveryAddress;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author zhangruibing
 * @since 2017-07-17
 */
@Repository

public interface DeliveryAddressDao {

    /**
     * <p>新增收货地址</p>
     * @param deliveryAddress
     */
    int save(DeliveryAddress deliveryAddress);

    /**
     * <p>编辑收货地址</p>
     *
     * @param deliveryAddress
     * @return
     */
    int update(DeliveryAddress deliveryAddress);

    /**
     * 删除收货地址(逻辑删除)
     * @param addrId 收货地址id
     */
    int deleteByAddrId(@Param("addrId") Long addrId);

    /**
     * <p>根据用户id查询收货地址</p>
     * @param userId
     * @return
     */
    List<DeliveryAddress> findByUserId(@Param("userId") Long userId);

    /**
     * 收货地址默认状态归零
     * @param userId
     * @return
     */
    int  updateDeliveryAddressStatus(@Param("userId") Long userId);

    /**
     * 设置默认收货地址
     * @param id
     * @return
     */
    int updateDeliveryAddressStatusByid(@Param("id") Long id);
}