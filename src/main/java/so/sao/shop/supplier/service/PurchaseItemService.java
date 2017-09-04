package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.output.RecordToPurchaseItemOutput;

/**
 * Created by niewenchao on 2017/7/19.
 */
public interface PurchaseItemService {

    /**
     * 根据订单编号查询所有相关的订单明细记录（分页）
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @param orderId 订单编号
     * @return 相关记录的集合
     */
    Result<RecordToPurchaseItemOutput> searchPurchaseItems(Integer pageNum, Integer pageSize, String orderId);

}
