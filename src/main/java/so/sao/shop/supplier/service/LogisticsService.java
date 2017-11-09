package so.sao.shop.supplier.service;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.vo.PurchaseInfoVo;

import java.util.List;
import java.util.Map;

/**
 * Created by wyy on 2017/8/16.
 */

public interface LogisticsService {
    /**
     * 根据物流单号查询物流信息
     *
     * @param num 物流单号
     * @return
     */
    Result<Object> findLogisticInfo(String num);

    /**
     * 插入已确认收货的订单
     *
     * @return
     */
    Map<String, Object> insertReceivedOrder(String orderId) throws Exception;

    /**
     * 获取已收货7天的订单ID
     *
     * @return List<String> 订单编号集合
     */
    List<String> findOrderIdByTime();

    /**
     * 自动确认收货
     *
     * @param orderIds 订单ID集合
     * @return
     */
    int receiveOrder(@Param("orderIds") List<String> orderIds);

    /**
     * 根据订单ID删除received_purchase表已自动确认收货的订单
     *
     * @param orderIds 订单ID集合
     */
    void deleteReceivedOrderByOrderId(@Param("orderIds") List<String> orderIds);

    /**
     * 根据订单状态获取订单信息（订单ID、物流单号）
     *
     * @param orderStatus 订单状态
     * @return List<PurchaseInfoVo>  订单信息列表
     */
    List<PurchaseInfoVo> findOrderInfoByOrderStatus(@Param("orderStatus") Integer orderStatus);
}
