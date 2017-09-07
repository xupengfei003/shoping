package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.output.AppPurchaseOutput;
import so.sao.shop.supplier.pojo.vo.AppPurchasesVo;

import java.util.List;

/**
 * Created by acer on 2017/9/6.
 */
public interface AppPurchaseService {
    /**
     * 根据订单状态查询订单列表
     *
     * @param orderStatus 订单状态
     * @return List<AppPurchasesVo> 订单列表
     * @throws Exception 异常
     */
    List<AppPurchaseOutput> findOrderList(Integer orderStatus) throws Exception;
}
