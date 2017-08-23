package so.sao.shop.supplier.service;

import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.OrderMoneyRecordInput;
import so.sao.shop.supplier.pojo.output.OrderMoneyRecordOutput;
import so.sao.shop.supplier.pojo.output.RecordToPurchaseOutput;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by niewenchao on 2017/7/19.
 * 结算明细表（order_money_record）对应的Service接口
 */
public interface OrderMoneyRecordService {

    /**
     * 保存结算明细
     * @param accountList
     * @return
     */
    void saveOrderMoneyRecord(List<Account> accountList) throws Exception;

    /**
     * 结算明细列表
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param input 入参
     * @return
     */
    OrderMoneyRecordOutput searchOrderMoneyRecords(Integer pageNum, Integer pageSize, OrderMoneyRecordInput input) throws Exception;

    /**
     * 更新结算状态
     * @param recordId
     * @param state
     * @return
     */
    boolean updateOrderMoneyRecordState(String recordId, String state) throws Exception;

    /**
     * 根据根据申请人账户ID查询满足结算时间条件的已结算/待结算明细，并根据pageNum和pageSize进行分页
     * @param accountId
     * @param put
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    Result searchRecords(Long accountId, OrderMoneyRecordInput put, Integer pageNum, Integer pageSize) throws Exception;

    /**
     * 根据结算明细查询该明细所对应的订单列表，并根据pageNum和pageSize进行分页展示
     * @param recordId
     * @param pageNum
     * @param pageSize
     * @param orderId
     * @return
     */
    RecordToPurchaseOutput searchOMRPurchaseDetails(String recordId, Integer pageNum, Integer pageSize, String orderId);

    /**
     * 根据账期时间计算当月已结算金额
     *
     * @param startTime 本月开始时间
     * @param endTime   本月结束时间
     * @return 结算金额
     */
    String settlementMoney(String startTime, String endTime);


    /**
     * 根据账期时间计算当月未结算金额
     *
     * @param startTime 本月开始时间
     * @param endTime   本月结束时间
     * @return 待结算金额
     */
    String unsettled(String startTime, String endTime);

    /**
     * 结算明细列表 导出excel
     * @param request
     * @param response
     * @throws Exception
     */
    void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
