package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.input.OrderMoneyRecordInput;
import so.sao.shop.supplier.pojo.output.OrderMoneyRecordOutput;
import so.sao.shop.supplier.pojo.output.RecordToPurchaseOutput;

import java.util.Map;

/**
 * Created by niewenchao on 2017/7/19.
 * 提现申请表（order_money_record）对应的Service接口
 */
public interface OrderMoneyRecordService {

    /**
     * 保存提现申请记录
     * @param userId
     * @return
     */
    Map<String, Object> saveOrderMoneyRecord(Long userId);

    /**
     * 查询提现申请记录
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param input 入参
     * @return
     */
    OrderMoneyRecordOutput searchOrderMoneyRecords(Integer pageNum, Integer pageSize, OrderMoneyRecordInput input);

    /**
     * 更新审核状态
     * @param recordId
     * @param state
     * @return
     */
    Map<String, Object> updateOrderMoneyRecordState(Long recordId, String state);

    /**
     * 根据提现申请表中的申请人ID查询申请该ID下所有的申请记录，并根据pageNum和pageSize进行分页展示
     * 返回开发规定的提现申请记录的Output对象
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    OrderMoneyRecordOutput searchOrderMoneyRecords(Long userId, OrderMoneyRecordInput put, Integer pageNum, Integer pageSize);


    /**
     * 根据提现申请记录查询该记录所对应的订单列表，并根据pageNum和pageSize进行分页展示
     * @param recordId
     * @param pageNum
     * @param pageSize
     * @return
     */
    RecordToPurchaseOutput searchOMRPurchaseDetails(Long recordId, Integer pageNum, Integer pageSize);

}
