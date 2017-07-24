package com.sao.so.shop.service;

import com.sao.so.shop.pojo.output.RecordToPurchaseOutput;

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
    RecordToPurchaseOutput searchPurchaseItems(Integer pageNum, Integer pageSize, Long orderId);

}
