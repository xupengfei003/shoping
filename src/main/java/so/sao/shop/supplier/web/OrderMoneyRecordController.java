package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.input.OrderMoneyRecordInput;
import so.sao.shop.supplier.pojo.output.OrderMoneyRecordAddOutput;
import so.sao.shop.supplier.pojo.output.OrderMoneyRecordOutput;
import so.sao.shop.supplier.pojo.output.RecordToPurchaseOutput;
import so.sao.shop.supplier.service.OrderMoneyRecordService;
import so.sao.shop.supplier.util.DateUtil;

import java.util.Map;

/**
 * Created by niewenchao on 2017/7/19.
 */
@RestController
@Api(description = "提现申请")
@RequestMapping(value = "/withdraw")
public class OrderMoneyRecordController {

    @Autowired
    private OrderMoneyRecordService orderMoneyRecordService;

    /**
     * 新增提现申请
     * @param userId 提现申请实体信息
     * @return
     */
    @ApiOperation(value = "新增提现申请", notes = "新增提现申请")
    @PostMapping(value = "/orderMoneyRecords")
    public OrderMoneyRecordAddOutput createOrderMoneyRecord(Long userId) {
        OrderMoneyRecordAddOutput output = new OrderMoneyRecordAddOutput();
        try {
            if(null != userId){
                Map<String, Object> map = orderMoneyRecordService.saveOrderMoneyRecord(userId);
                Integer status = Integer.parseInt(String.valueOf(map.get("status")));
                if(status == -3){
                    output.setCode(Constant.OMRCodeConfig.OMR_CODE_ACCOUNT_NOT_EXIST);
                    output.setMessage(Constant.OMRMessageConfig.OMR_MSG_ACCOUNT_NOT_EXIST);
                } else if(status == -2){
                    output.setCode(Constant.OMRCodeConfig.OMR_CODE_NOT_TIME_PERIOD);
                    output.setMessage(Constant.OMRMessageConfig.OMR_MSG_NOT_TIME_PERIOD);
                } else if(status == -1) {
                    output.setCode(Constant.OMRCodeConfig.OMR_CODE_MIN_WITHDRAW);
                    output.setMessage(Constant.OMRMessageConfig.OMR_MSG_MIN_WITHDRAW);
                } else if(status == 1) {
                    output.setCode(Constant.OMRCodeConfig.OMR_CODE_SUCCESS);
                    output.setMessage(Constant.OMRMessageConfig.OMR_MSG_SUCCESS);
                    output.setRecordId(String.valueOf(map.get("recordId")));
                } else {
                    output.setCode(Constant.OMRCodeConfig.OMR_CODE_FAILURE);
                    output.setMessage(Constant.OMRMessageConfig.OMR_MSG_FAILURE);
                }
            } else {
                output.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
                output.setMessage(Constant.MessageConfig.MSG_NOT_EMPTY);
            }
        } catch (Exception e) {
            e.printStackTrace();
            output.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
            output.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
        }
        return output;
    }


    /**
     * 查询提现申请列表
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param input 入参
     * @return
     */
    @ApiOperation(value = "查询提现申请列表", notes = "查询提现申请列表")
    @GetMapping(value = "/search")
    public OrderMoneyRecordOutput search(Integer pageNum, Integer pageSize, OrderMoneyRecordInput input) {
        OrderMoneyRecordOutput output = new OrderMoneyRecordOutput();
        output.setCode(Constant.CodeConfig.CODE_DATE_INPUT_FORMAT_ERROR);
        output.setMessage(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
        try {
            if(null != input){
                if(!StringUtils.isEmpty(input.getStartTime()) && !DateUtil.isDate(input.getStartTime())){
                    return output;
                }
                if(!StringUtils.isEmpty(input.getEndTime()) && !DateUtil.isDate(input.getEndTime())){
                    return output;
                }
            }
            output = orderMoneyRecordService.searchOrderMoneyRecords(pageNum, pageSize, input);
        } catch (Exception e) {
            e.printStackTrace();
            output.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
            output.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
        }
        return output;
    }


    /**
     * 更新提现申请状态
     * @param recordId 提现记录id
     * @param state 提现记录状态
     * @return
     */
    @ApiOperation(value = "更新提现申请状态", notes = "更新提现申请状态")
    @PutMapping(value = "/orderMoneyRecord/{recordId}")
    public BaseResult updateState(@PathVariable("recordId") Long recordId, @RequestParam("state") String state) {
        BaseResult output = new BaseResult();
        try {
            if(null != recordId && !StringUtils.isEmpty(state)){
                Map<String, Object> map = orderMoneyRecordService.updateOrderMoneyRecordState(recordId, state);
                Integer status = Integer.parseInt(String.valueOf(map.get("status")));
                if(status == 1){
                    output.setCode(Constant.OMRCodeConfig.OMR_CODE_PASSED);
                    output.setMessage(Constant.OMRMessageConfig.OMR_MSG_PASSED);
                } else if (status == 2){
                    output.setCode(Constant.OMRCodeConfig.OMR_CODE_DONE);
                    output.setMessage(Constant.OMRMessageConfig.OMR_MSG_DONE);
                } else if (status == 3){
                    output.setCode(Constant.OMRCodeConfig.OMR_CODE_APPLY);
                    output.setMessage(Constant.OMRMessageConfig.OMR_MSG_APPLY);
                } else if (status == 4){
                    output.setCode(Constant.OMRCodeConfig.OMR_CODE_PARAM_ERROR);
                    output.setMessage(Constant.OMRMessageConfig.OMR_MSG_PARAM_ERROR);
                } else {
                    output.setCode(Constant.CodeConfig.CODE_FAILURE);
                    output.setMessage(Constant.MessageConfig.MSG_FAILURE);
                }
            } else {
                output.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
                output.setMessage(Constant.MessageConfig.MSG_NOT_EMPTY);
            }
        } catch (Exception e) {
            e.printStackTrace();
            output.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
            output.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
        }
        return output;
    }


    /**
     * 根据提现申请表中的申请人ID查询申请该ID下所有的申请记录，并根据pageNum和pageSize进行分页
     *
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "根据申请人ID查询申请提现记录", notes = "根据申请人的ID，对提现申请表进行全查询，并根据pageNum和pageSize进行分页")
    @GetMapping(value = "/orderMoneyRecords/{id}")//访问路径
    public OrderMoneyRecordOutput searchOrderMoneyRecords(OrderMoneyRecordInput put, Integer pageNum, Integer pageSize,@PathVariable("id") @Validated Long id) {
        OrderMoneyRecordOutput orderMoneyRecordOutput = new OrderMoneyRecordOutput();
        try{
            orderMoneyRecordOutput=orderMoneyRecordService.searchOrderMoneyRecords(id, put, pageNum, pageSize);//根据Service方法，返回余额对象
        }catch (Exception e){
            e.printStackTrace();
            orderMoneyRecordOutput.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
            orderMoneyRecordOutput.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
        }
        return orderMoneyRecordOutput;
    }


    /**
     * 根据提现申请记录查询该记录所对应的订单列表
     * @param recordId 提现记录id
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return
     */
    @ApiOperation(value = "根据提现申请记录查询该记录所对应的订单列表", notes = "根据提现申请记录查询该记录所对应的订单列表，并根据pageNum和pageSize进行分页")
    @GetMapping(value = "/orderMoneyRecord/searchPurchasesByRecordId/{recordId}")
    public RecordToPurchaseOutput searchOMRPurchaseDetails(@PathVariable("recordId") Long recordId, Integer pageNum, Integer pageSize) {
        RecordToPurchaseOutput output = new RecordToPurchaseOutput();
        try {
            if(null != recordId){
                output = orderMoneyRecordService.searchOMRPurchaseDetails(recordId, pageNum, pageSize);
            } else {
                output.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
                output.setMessage(Constant.MessageConfig.MSG_NOT_EMPTY);
            }
        } catch (Exception e) {
            e.printStackTrace();
            output.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
            output.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
        }
        return output;
    }

}
