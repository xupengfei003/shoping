package so.sao.shop.supplier.dao;


import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.OrderMoneyRecord;
import so.sao.shop.supplier.domain.Purchase;
import so.sao.shop.supplier.pojo.input.OrderMoneyRecordInput;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by niewenchao on 2017/7/19.
 * 结算明细表（order_money_record）对应的Dao层
 */
public interface OrderMoneyRecordDao {

    /**
     * 批量保存结算明细
     * @param list
     * @return
     * @throws Exception
     */
    int saveOrderMoneyRecords(List<OrderMoneyRecord> list) throws Exception;

    /**
     * 结算明细列表
     * @param input 入参
     * @return
     */
    List<OrderMoneyRecord> findPageByState(OrderMoneyRecordInput input);

    /**
     * 更新结算状态
     * @param orderMoneyRecord
     * @return
     */
    int updateOrderMoneyRecord(OrderMoneyRecord orderMoneyRecord);

    /**
     * 根据记录id,查询结算明细
     * @param recordId
     * @return
     */
    OrderMoneyRecord findOne(@Param("recordId") String recordId);

    /**
     * 根据申请人ID查询满足结算时间条件的已结算明细
     * @param accountId
     * @param put
     *
     * @return
     */
    List<OrderMoneyRecord> findSettledPage(@Param("accountId") Long accountId,@Param("put")OrderMoneyRecordInput put) throws Exception;

    /**
     * 根据申请人ID查询满足结账时间条件的待结算明细
     * @param accountId
     * @param put
     *
     * @return
     */
    List<OrderMoneyRecord> findUnpaidPage(@Param("accountId") Long accountId,@Param("put")OrderMoneyRecordInput put) throws Exception;

    /**
     * 根据结算明细recordId和订单orderId 查询订单列表
     * @param recordId
     * @param orderId
     * @return
     */
    List<Purchase> findPageOMRPurchase(@Param("recordId") String recordId, @Param("orderId") String orderId);

    /**
     * 根据开始时间和结束时间查询已结算金额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 已结算金额
     */
    BigDecimal settledAmount(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 根据开始时间和结束时间查询未结算金额
     *
     * @param startTime
     * @param endTime
     * @return 未结算金额
     */
    BigDecimal totalMoney(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 根据条件查询结算列表
     * @param startTime 起始时间
     * @param endTime 结束时间
     * @param state 结算状态
     * @param limits 分页信息
     * @return
     */
    List<OrderMoneyRecord> findRecords(@Param("startTime")String startTime,
                                       @Param("endTime")String endTime,
                                       @Param("state") String state,
                                       @Param("limit")String limits);
    /**
     *根据账户id统计该账户名下所有已结算金额之和
     *
     * @param accountId
     * @return
     * @throws Exception
     */
    BigDecimal findTotalSettledAmount(@Param("accountId") Long accountId) throws Exception;

    /**
     *根据账户id统计该账户名下所有待结算金额之和
     *
     * @param accountId
     * @return
     * @throws Exception
     */
    BigDecimal findTotalUnpaidMoney(@Param("accountId") Long accountId) throws Exception;

}