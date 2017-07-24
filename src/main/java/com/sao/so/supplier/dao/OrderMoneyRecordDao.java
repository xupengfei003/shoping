package com.sao.so.supplier.dao;


import com.sao.so.supplier.domain.OrderMoneyRecord;
import com.sao.so.supplier.pojo.input.OrderMoneyRecordInput;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by niewenchao on 2017/7/19.
 * 提现申请表（order_money_record）对应的Dao层
 */
public interface OrderMoneyRecordDao {
    /**
     * 保存提现申请记录
     * @param orderMoneyRecord
     * @return
     */
    Long save(OrderMoneyRecord orderMoneyRecord);

    /**
     * 查询提现申请记录列表
     * @param input 入参
     * @return
     */
    List<OrderMoneyRecord> findPageByState(@Param("input") OrderMoneyRecordInput input);

    /**
     * 更新提现申请记录状态
     * @param orderMoneyRecord
     * @return
     */
    int updateOrderMoneyRecord(OrderMoneyRecord orderMoneyRecord);

    /**
     * 根据记录id,查询提现申请记录
     * @param recordId
     * @return
     */
    OrderMoneyRecord findOne(@Param("recordId") Long recordId);

    /**
     * 根据提现申请表（order_money_record）中的申请人ID（user_id）查询申请该ID下所有的申请记录
     * @param userId
     * @return
     */
    List<OrderMoneyRecord> findPage(@Param("userId") Long userId, @Param("put")OrderMoneyRecordInput put);

}