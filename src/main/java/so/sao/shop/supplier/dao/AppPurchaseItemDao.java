package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.pojo.vo.AppPurchaseItemVo;

import java.util.List;

/**
 * Created by acer on 2017/9/7.
 */
public interface AppPurchaseItemDao {
    /**
     * 根据订单ID列表查询订单详情
     *
     * @param orderIdList 订单ID列表
     * @return List<AppPurchaseItemVo> 订单列表
     * @throws Exception 异常
     */
    List<AppPurchaseItemVo> findOrderItemList(@Param("orderIdList") List<String> orderIdList) throws Exception;

    /**
     * 根据订单ID列表查询订单详情
     *
     * @param orderId 订单ID
     * @return List<AppPurchaseItemVo> 订单列表
     * @throws Exception 异常
     */
    List<AppPurchaseItemVo> findOrderItemListByOrderId(@Param("orderId") String orderId) throws Exception;

    /**
     * 根据合并支付ID列表查询订单详情
     *
     * @param payIdList 订单ID列表
     * @return List<AppPurchaseItemVo> 订单列表
     * @throws Exception 异常
     */
    List<AppPurchaseItemVo> findOrderItemListByPayId(@Param("payIdList") List<String> payIdList) throws Exception;
}
