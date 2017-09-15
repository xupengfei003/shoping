package so.sao.shop.supplier.service.app;

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
     * @return AppPurchaseItemVo 订单信息
     * @throws Exception 异常
     */
    AppPurchaseItemOutput findOrderItemList(String orderId) throws Exception;
}
