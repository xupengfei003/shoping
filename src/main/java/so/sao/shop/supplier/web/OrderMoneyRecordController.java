package so.sao.shop.supplier.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.OrderMoneyRecordInput;
import so.sao.shop.supplier.pojo.input.OrderMoneyRecordRankInput;
import so.sao.shop.supplier.pojo.output.OrderMoneyRecordOutput;
import so.sao.shop.supplier.pojo.output.RecordToPurchaseOutput;
import so.sao.shop.supplier.service.OrderMoneyRecordService;
import so.sao.shop.supplier.util.DataCompare;
import so.sao.shop.supplier.util.DateUtil;
import so.sao.shop.supplier.util.Ognl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.Map;

/**
 * Created by niewenchao on 2017/7/19.
 */
@RestController
@RequestMapping(value = "/billingDetails")
public class OrderMoneyRecordController {

    @Autowired
    private OrderMoneyRecordService orderMoneyRecordService;

    /**
     * 根据账期时间、结算状态查询结算明细列表
     *
     * 1.校验参数合法性
     *      1.1 如果入参对象为null,则返回失败信息
     *      1.2 判断起始时间、结束时间、结算状态不为空
     *      1.3 判断起始时间是否小于结束时间
     * 2.查询结算明细列表
     *      2.1 未查找到结果
     *      2.2 查询成功，返回结果
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param input    入参
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/search")
    public Result search(Integer pageNum, Integer pageSize, @Valid OrderMoneyRecordInput input) throws Exception {

        //1.校验参数合法性
        //1.1 如果入参对象为null,则返回失败信息
        if (null == input) {
            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
        }

        Date startTime = input.getStartTime();
        Date endTime = input.getEndTime();

        //1.2 判断起始时间、结束时间、结算状态不为空
        if (Ognl.isEmpty(startTime) || Ognl.isEmpty(endTime) || Ognl.isEmpty(input.getState())) {
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }

        //1.3 判断起始时间是否小于结束时间
        String timeMsg = DataCompare.createAtCheck(startTime, endTime);
        if (Ognl.isNotEmpty(timeMsg)) {
            return Result.fail(timeMsg);
        }

        //2.查询结算明细列表
        OrderMoneyRecordOutput output = orderMoneyRecordService.searchOrderMoneyRecords(pageNum, pageSize, input);

        //2.1 未查找到结果
        if (null == output) {
            return Result.success(Constant.MessageConfig.MSG_NO_DATA);
        }

        //2.2 查询成功，返回结果
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, output);
    }


    /**
     * 更新结算状态
     *
     * 1.校验参数合法性
     *      1.1 判断参数不为空
     * 2.修改结算状态
     *      2.1 修改状态成功，返回信息
     *      2.2 修改状态失败，返回信息
     *
     * @param params Map封装入参键值对，包含recordId、serialNumber
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/orderMoneyRecord/updateState")
    public Result updateState(@RequestBody Map params) throws Exception {
        String recordId = (String) params.get("recordId");
        String serialNumber = (String) params.get("serialNumber");

        //1.校验参数合法性
        //1.1 判断参数不为空
        if (Ognl.isEmpty(recordId) || Ognl.isEmpty(serialNumber)) {
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }

        //2.修改结算状态
        boolean flag = orderMoneyRecordService.updateOrderMoneyRecordState(recordId, serialNumber);

        //2.1 修改状态成功，返回信息
        if (flag) {
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }

        //2.2 修改状态失败，返回信息
        return Result.fail(Constant.MessageConfig.MSG_FAILURE);
    }


    /**
     * 根据根据申请人账户查询满足结算时间条件的已结算/待结算明细，并根据pageNum和pageSize进行分页
     *
     * @param pageNum
     * @param pageSize
     * @param request
     * @param put
     * @return
     */
    @GetMapping(value = "/orderMoneyRecords")
    public Result searchOrderMoneyRecords(Integer pageNum, Integer pageSize, HttpServletRequest request, @Valid OrderMoneyRecordRankInput put) throws Exception {
        //获取用户
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (Ognl.isNull(user)) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }

        // 若传入起始和结束时间，检查起始时间是否大于结束时间
        String timeMsg = DataCompare.createAtCheck(put.getStartTime(), put.getEndTime());
        if (Ognl.isNotEmpty(timeMsg)) {
            return Result.fail(timeMsg);
        }
        // 查询数据
        Map<String, Object> map = orderMoneyRecordService.searchRecords(user.getAccountId(), put, pageNum, pageSize);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, map);
    }

    /**
     * 根据结算明细id查询该明细所对应的订单列表
     *
     * 1.校验参数合法性
     *      1.1 判断recordId不为空
     * 2.查询结算明细id对应的订单列表
     *      2.1 查询成功，返回结果
     *
     * @param recordId 结算明细id
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param orderId  订单id
     * @return
     */
    @GetMapping(value = "/orderMoneyRecord/searchPurchasesByRecordId")
    public Result searchOMRPurchaseDetails(String recordId, Integer pageNum, Integer pageSize, String orderId) throws Exception {

        //1.校验参数合法性
        //1.1 判断recordId不为空
        if (Ognl.isEmpty(recordId)) {
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }

        //2.查询结算明细id对应的订单列表
        RecordToPurchaseOutput output = orderMoneyRecordService.searchOMRPurchaseDetails(recordId, pageNum, pageSize, orderId);

        //2.1 查询成功，返回结果
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, output);
    }

    /**
     * 根据账期时间计算当月已结算金额
     *
     * @param startTime 本月开始时间
     * @param endTime   本月结束时间
     * @return settled_amount 已结算金额
     */
    @GetMapping(value = "/orderMoneyRecord/settlement")
    public Result settlementMoney(String startTime, String endTime) throws Exception {
        //判断null和空值
        if (Ognl.isEmpty(startTime) || Ognl.isEmpty(endTime)) {
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }

        //判断时间格式
        if (!DateUtil.isDate(startTime) || !DateUtil.isDate(endTime)) {
            return Result.fail(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
        }

        //判断起始时间是否小于结束时间
        if (DataCompare.compareDate(startTime, endTime)) {
            return Result.fail(Constant.MessageConfig.DateNOTLate);
        }

        //获取传入时间段内的已结算金额
        String sumSettledAmout = orderMoneyRecordService.settlementMoney(startTime, endTime);

        return Result.success(Constant.MessageConfig.MSG_SUCCESS, sumSettledAmout);
    }

    /**
     * 根据账期时间计算当月未结算金额
     *
     * @param startTime 本月开始时间
     * @param endTime   本月结束时间
     * @return totalMoney 待结算金额
     */
    @GetMapping(value = "/orderMoneyRecord/unsettled")
    public Result totalMoney(String startTime, String endTime) throws Exception {
        //判断null和空值
        if (Ognl.isEmpty(startTime) || Ognl.isEmpty(endTime)) {
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }

        //判断时间格式
        if (!DateUtil.isDate(startTime) || !DateUtil.isDate(endTime)) {
            return Result.fail(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
        }

        //判断起始时间是否小于结束时间
        if (DataCompare.compareDate(startTime, endTime)) {
            return Result.fail(Constant.MessageConfig.DateNOTLate);
        }

        //获取传入时间段内的待结算金额
        String totalMonay = orderMoneyRecordService.unsettled(startTime, endTime);

        return Result.success(Constant.MessageConfig.MSG_SUCCESS, totalMonay);
    }


    /**
     * 结算账单列表 导出excel
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/excel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception{
        orderMoneyRecordService.exportExcel(request,response);
    }

	 /**
     * 供应商订单金额统计
     * @param timeType 统计时间类型--时间类型（1.本周，2.当月，3.近三个月）
     * @return
     */
    @GetMapping("/countOrderMoneyRecords")
    public Result countOrderMoneyRecords(HttpServletRequest request,@RequestParam Integer timeType){
        //获取用户
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (Ognl.isNull(user)) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        Map<String,Object> resultMap = orderMoneyRecordService.countOrderMoneyRecords(timeType,user.getAccountId());
        return Result.success(Constant.MessageConfig.MSG_SUCCESS,resultMap);
    }

    /**
     * 账单明细数据列表 导出Excel
     * @param recordId 结算明细id
     * @param orderId 订单id
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param response
     * @throws Exception
     */
    @GetMapping("/orderMoneyRecord/recordToPurchasesExcel")
    public void exportRecordToPurchasesExcel(String recordId, String orderId, String pageNum, String pageSize, HttpServletResponse response) throws Exception {
        orderMoneyRecordService.exportRecordToPurchasesExcel(recordId, orderId, pageNum, pageSize, response);
    }

}
