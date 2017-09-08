package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.output.AppPurchaseItemOutput;

import java.util.List;

/**
 * Created by acer on 2017/9/7.
 */
public interface AppPurchaseItemService {
    /**
     * 根据订单ID查询订单详情
     *
     * @param orderId 订单ID
     * @return List<AppPurchaseItemVo> 订单列表
     * @throws Exception 异常
     */
    List<AppPurchaseItemOutput> findOrderItemList(String orderId) throws Exception;
}
