package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.OrderMoneyRecordInput;
import so.sao.shop.supplier.pojo.output.OrderMoneyRecordOutput;
import so.sao.shop.supplier.pojo.output.RecordToPurchaseOutput;
import so.sao.shop.supplier.service.OrderMoneyRecordService;
import so.sao.shop.supplier.util.DataCompare;
import so.sao.shop.supplier.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by niewenchao on 2017/7/19.
 */
@RestController
@Api(description = "结算明细")
@RequestMapping(value = "/billingDetails")
public class OrderMoneyRecordController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderMoneyRecordService orderMoneyRecordService;

    /**
     * 根据账期时间、结算状态查询结算明细列表
     *
     * 1.创建返回对象，并设置返回信息
     * 2.校验参数合法性
     *      2.1 判断起始时间、结束时间、结算状态不为空
     *      2.2 判断时间格式是否正确
     *      2.3 判断起始时间是否小于结束时间
     * 3.查询结算明细列表
     *      3.1 未查找到结果
     *      3.2 查询成功，返回结果
     * 4.异常信息
     *
     * @param pageNo  页码
     * @param pageSize 每页条数
     * @param input    入参
     * @return
     */
    @ApiOperation(value = "查询结算明细列表", notes = "查询结算明细列表")
    @GetMapping(value = "/search")
    public Result<OrderMoneyRecordOutput> search(Integer pageNo, Integer pageSize, OrderMoneyRecordInput input) {
        //1.创建返回对象，并设置返回信息
        Result<OrderMoneyRecordOutput> result = new Result<>();
        result.setCode(Constant.CodeConfig.CODE_FAILURE);
        result.setMessage(Constant.MessageConfig.MSG_FAILURE);
        try {
            if (null == input) {
                return result;
            }

            String startTime = input.getStartTime();
            String endTime = input.getEndTime();

            //2.校验参数合法性
            //2.1 判断起始时间、结束时间、结算状态不为空
            if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime) || StringUtils.isEmpty(input.getState())) {
                result.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
                result.setMessage(Constant.MessageConfig.MSG_NOT_EMPTY);
                return result;
            }

            //2.2 判断时间格式是否正确
            if (!DateUtil.isDate(startTime) || !DateUtil.isDate(endTime)) {
                result.setCode(Constant.CodeConfig.CODE_DATE_INPUT_FORMAT_ERROR);
                result.setMessage(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
                return result;
            }

            //2.3 判断起始时间是否小于结束时间
            if (DataCompare.compareDate(startTime, endTime)) {
                result.setCode(Constant.CodeConfig.DateNOTLate);
                result.setMessage(Constant.MessageConfig.DateNOTLate);
                return result;
            }

            //3.查询结算明细列表
            OrderMoneyRecordOutput output = orderMoneyRecordService.searchOrderMoneyRecords(pageNo, pageSize, input);

            //3.1 未查找到结果
            if (null == output) {
                result.setCode(Constant.CodeConfig.CODE_NOT_FOUND_RESULT);
                result.setMessage(Constant.MessageConfig.MSG_NO_DATA);
                return result;
            }

            //3.2 查询成功，返回结果
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
            result.setData(output);

        } catch (Exception e) {
            //4.异常信息
            logger.error("系统异常", e);
            result.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
            result.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
        }
        return result;
    }


    /**
     * 更新结算状态
     *
     * 1.创建返回对象，并设置返回信息
     * 2.校验参数合法性
     *      2.1 判断参数不为空
     * 3.修改结算状态
     *      3.1 修改状态成功，返回信息
     * 4.异常信息
     *
     * @param recordId 结算明细id
     * @param state    结算状态
     * @return
     */
    @ApiOperation(value = "更新结算状态", notes = "更新结算状态")
    @PostMapping(value = "/orderMoneyRecord/updateState")
    public Result updateState(String recordId, String state) {
        //1.创建返回对象，并设置返回信息
        Result result = new Result();
        result.setCode(Constant.CodeConfig.CODE_FAILURE);
        result.setMessage(Constant.MessageConfig.MSG_FAILURE);
        try {

            //2.校验参数合法性
            //2.1 判断参数不为空
            if (StringUtils.isEmpty(recordId) || StringUtils.isEmpty(state)) {
                result.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
                result.setMessage(Constant.MessageConfig.MSG_NOT_EMPTY);
                return result;
            }

            //3.修改结算状态
            boolean flag = orderMoneyRecordService.updateOrderMoneyRecordState(recordId, state);

            //3.1 修改状态成功，返回信息
            if (flag) {
                result.setCode(Constant.CodeConfig.CODE_SUCCESS);
                result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
            }
        } catch (Exception e) {
            //4.异常信息
            logger.error("系统异常", e);
            result.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
            result.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
        }
        return result;
    }


    /**
     * 根据根据申请人账户查询满足结算时间条件的已结算/待结算明细，并根据pageNum和pageSize进行分页
     * @param pageNum
     * @param pageSize
     * @param request
     * @param put
     * @return
     */
    @ApiOperation(value = "根据账户ID查询已结算/待结算明细", notes = "根据账户ID查询已结算/待结算明细并分页")
    @GetMapping(value = "/orderMoneyRecords")
    public Result searchOrderMoneyRecords(Integer pageNum, Integer pageSize,HttpServletRequest request,@Valid OrderMoneyRecordInput put) {
        //初始化
        Result result = new Result<>();
        result.setCode(Constant.CodeConfig.CODE_DATE_INPUT_FORMAT_ERROR);
        result.setMessage(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
        result.setData(null);
        //获取用户
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (null == user){
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
            return result;
        }
        try {
              // 校验时间参数
              if (!StringUtils.isEmpty(put.getStartTime()) && !DateUtil.isDate(put.getStartTime())) {
                   return result;
              }
              if (!StringUtils.isEmpty(put.getEndTime()) && !DateUtil.isDate(put.getEndTime())) {
                   return result;
              }
              if(DataCompare.compareDate(put.getStartTime(),put.getEndTime())){
                  result.setCode(Constant.CodeConfig.DateNOTLate);
                  result.setMessage(Constant.MessageConfig.DateNOTLate);
                  return result;
              }
              // 查询数据
              result = orderMoneyRecordService.searchRecords(user.getAccountId(), put, pageNum, pageSize);
            } catch (Exception e) {
                logger.error("系统异常", e);
                result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
                result.setMessage(so.sao.shop.supplier.config.Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
            }
        return result;
    }

    /**
     * 根据结算明细id查询该明细所对应的订单列表
     *
     * 1.创建返回对象，并设置返回信息
     * 2.校验参数合法性
     *      2.1 判断recordId不为空
     * 3.查询结算明细id对应的订单列表
     *      3.1 未查找到结果
     *      3.2 查询成功，返回结果
     * 4.异常信息
     *
     * @param recordId 结算明细id
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param orderId  订单id
     * @return
     */
    @ApiOperation(value = "根据结算明细id查询该明细对应的订单列表", notes = "根据结算明细id查询该明细对应的订单列表，并根据pageNum和pageSize进行分页")
    @GetMapping(value = "/orderMoneyRecord/searchPurchasesByRecordId")
    public Result<RecordToPurchaseOutput> searchOMRPurchaseDetails(String recordId, Integer pageNum, Integer pageSize, String orderId) {
        //1.创建返回对象，并设置返回信息
        Result<RecordToPurchaseOutput> result = new Result<>();
        result.setCode(Constant.CodeConfig.CODE_FAILURE);
        result.setMessage(Constant.MessageConfig.MSG_FAILURE);

        try {

            //2.校验参数合法性
            //2.1 2.1 判断recordId不为空
            if (StringUtils.isEmpty(recordId)) {
                result.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
                result.setMessage(Constant.MessageConfig.MSG_NOT_EMPTY);
                return result;
            }

            //3.查询结算明细id对应的订单列表
            RecordToPurchaseOutput output = orderMoneyRecordService.searchOMRPurchaseDetails(recordId, pageNum, pageSize, orderId);

            //3.1 未查找到结果
            if (null == output) {
                result.setCode(Constant.CodeConfig.CODE_NOT_FOUND_RESULT);
                result.setMessage(Constant.MessageConfig.MSG_NO_DATA);
                return result;
            }

            //3.2 查询成功，返回结果
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
            result.setData(output);

        } catch (Exception e) {
            //4.异常信息
            logger.error("系统异常", e);
            result.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
            result.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
        }
        return result;
    }

    /**
     * 根据账期时间计算当月已结算金额
     *
     * @param startTime 本月开始时间
     * @param endTime   本月结束时间
     * @return settled_amount 已结算金额
     */
    @ApiOperation(value = "根据开始时间和结束时间查询已结算金额", notes = "根据开始时间和结束时间,计算其已结算金额")
    @GetMapping(value = "/orderMoneyRecord/settlement")
    public Result<String> settlementMoney(String startTime, String endTime) {
        Result<String> result = new Result<>();  //返回类型
        result.setCode(Constant.CodeConfig.CODE_FAILURE);
        result.setMessage(Constant.MessageConfig.MSG_FAILURE);
        try {
            if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {  //判断null和空值
                result.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
                result.setMessage(Constant.MessageConfig.MSG_NOT_EMPTY);
                return result;
            }
            if (!DateUtil.isDate(startTime) || !DateUtil.isDate(endTime)) {  //判断时间格式
                result.setCode(Constant.CodeConfig.CODE_DATE_INPUT_FORMAT_ERROR);
                result.setMessage(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
                return result;
            }
            //判断起始时间是否小于结束时间
            if (DataCompare.compareDate(startTime, endTime)) {
                result.setCode(Constant.CodeConfig.DateNOTLate);
                result.setMessage(Constant.MessageConfig.DateNOTLate);
                return result;
            }
            //获取业务层数据-已结算金额
            String sumSettledAmout = orderMoneyRecordService.settlementMoney(startTime, endTime);
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);  //判断逻辑成功
            result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
            result.setData(StringUtils.isEmpty(sumSettledAmout) ? "0.00" : sumSettledAmout);
        } catch (Exception e) {  //出现异常
            logger.error("系统异常", e);
            result.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
            result.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
        }
        return result;

    }

    /**
     * 根据账期时间计算当月未结算金额
     *
     * @param startTime 本月开始时间
     * @param endTime   本月结束时间
     * @return totalMoney 待结算金额
     */
    @ApiOperation(value = "根据开始时间和结束时间查询未结算金额", notes = "根据开始时间和结束时间,计算其未结算金额")
    @GetMapping(value = "/orderMoneyRecord/unsettled")
    public Result<String> totalMoney(String startTime, String endTime) {
        Result<String> result = new Result<>();//返回类型
        result.setCode(Constant.CodeConfig.CODE_FAILURE);
        result.setMessage(Constant.MessageConfig.MSG_FAILURE);
        try {
            if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {  //判断null和空值
                result.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
                result.setMessage(Constant.MessageConfig.MSG_NOT_EMPTY);
                return result;
            }
            if (!DateUtil.isDate(startTime) || !DateUtil.isDate(endTime)) {  //判断时间格式
                result.setCode(Constant.CodeConfig.CODE_DATE_INPUT_FORMAT_ERROR);
                result.setMessage(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
                result.setData(String.valueOf(0));
                return result;
            }
            //判断起始时间是否小于结束时间
            if (DataCompare.compareDate(startTime, endTime)) {
                result.setCode(Constant.CodeConfig.DateNOTLate);
                result.setMessage(Constant.MessageConfig.DateNOTLate);
                return result;
            }
            String sumUnsettled = orderMoneyRecordService.unsettled(startTime, endTime);
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);  //判断逻辑成功
            result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
            result.setData(StringUtils.isEmpty(sumUnsettled) ? "0.00" : sumUnsettled);
        } catch (Exception e) {  //出现异常
            logger.error("系统异常", e);
            result.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
            result.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
        }

        return result;
    }

    @ApiOperation(value="导出结算明细excel", notes = "根据查询条件导出excel")
    @GetMapping("/excel")
    public Result<Map<String,?>> exportExcel(HttpServletRequest request, HttpServletResponse response){
        Result<Map<String,?>> result = new Result<>();
        Map<String,?> map = new HashMap<>();
        result.setData(map);
        result.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
        result.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);

        try {
            orderMoneyRecordService.exportExcel(request,response);
        } catch (Exception e) {
            logger.error("系统异常", e);
            response.reset();
            return result;
        }
        return null;
    }

}
