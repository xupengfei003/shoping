package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.pojo.vo.PurchaseInfoVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物流信息转化(com转中文)
 * </p>
 *
 * @author 透云-中软-西安项目组-zhenhai.zheng
 * @since 2017年9月14日 14:51:15
 **/
public interface LogisticsDao {
    /**
     * 将传过来的快递公司英文code转化成中文
     * @param comStr 快递公司code
     * @return 快递公司中文
     */
    String findCompanyNameByCom(@Param("comStr") String comStr);
    /**
     * 插入已确认收货的订单
     *
     * @param map 封装了订单编号、创建时间
     * @return
     */
    int insertReceivedOrder(@Param("map") Map<String,Object> map) throws Exception;

    /**
     * 获取已收货7天的订单ID
     *
     * @param nowTime 当前时间
     * @return List<String> 订单编号集合
     */
    List<String> findOrderIdByTime(@Param("nowTime") String nowTime);

    /**
     * 自动确认收货
     *
     * @param map 订单ID、时间信息
     * @return
     */
    int receiveOrder(@Param("map") Map<String,Object> map);

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

    /**
     * 批量修改二维码状态
     * <p>
     * 根据订单编号修改二维码状态
     *
     * @param map 封装了要修改的信息
     * @return 成功返回true，失败返回false
     */
    boolean updateQrcodesStatus(@Param("map") Map<String,Object> map);
    /**
     * 更改订单状态为确认送达
     *
     * @param map 封装了要修改的信息
     * @return 成功返回true，失败返回false
     */
    void updateOrderStatus(@Param("map") Map<String,Object> map);
}
