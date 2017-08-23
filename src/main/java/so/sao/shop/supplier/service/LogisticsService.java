package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.Result;

/**
 * Created by wyy on 2017/8/16.
 */

public interface LogisticsService {
    /**
     * 根据物流单号查询物流信息
     * @param num 物流单号
     * @return
     */
    Result<Object> findLogisticInfo(String num);
}
