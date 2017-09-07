package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.pojo.vo.AppPurchaseItemVo;

import java.util.List;

/**
 * Created by acer on 2017/9/7.
 */
public interface AppPurchaseItemDao {
    /**
     * 根据订单ID查询订单详情
     *
     * @param orderId 订单ID
     * @return List<AppPurchaseItemVo> 订单列表
     * @throws Exception 异常
     */
    List<AppPurchaseItemVo> findOrderItemList(@Param("orderId") String orderId) throws Exception;
}
