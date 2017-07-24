package com.sao.so.supplier.web;

import com.sao.so.supplier.config.Constant;
import com.sao.so.supplier.pojo.BaseResult;
import com.sao.so.supplier.pojo.output.OrderMoneyRecordAddOutput;
import com.sao.so.supplier.pojo.output.OrderMoneyRecordOutput;
import com.sao.so.supplier.service.OrderMoneyRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        } catch (Exception e) {
            e.printStackTrace();
            output.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
            output.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
        }
        return output;
    }


    @ApiOperation(value = "查询提现申请列表", notes = "查询提现申请列表")
    @GetMapping(value = "/search")
    public OrderMoneyRecordOutput search(Integer pageNum, Integer pageSize) {
        return orderMoneyRecordService.searchOrderMoneyRecords(pageNum, pageSize);
    }

    @ApiOperation(value = "更新提现申请状态", notes = "更新提现申请状态")
    @PutMapping(value = "/orderMoneyRecord/{recordId}")
    public BaseResult updateState(@PathVariable("recordId") Long recordId, @RequestParam("state") String state) {
        BaseResult output = new BaseResult();
        try {
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
            } else if (status == 5){
                output.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
                output.setMessage(Constant.MessageConfig.MSG_NOT_EMPTY);
            } else {
                output.setCode(Constant.CodeConfig.CODE_FAILURE);
                output.setMessage(Constant.MessageConfig.MSG_FAILURE);
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
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "根据申请人ID查询申请提现记录",notes = "根据申请人的ID，对提现申请表进行全查询，并根据pageNum和pageSize进行分页")
    @GetMapping(value = "/orderMoneyRecords/{id}")//访问路径
    public OrderMoneyRecordOutput getBalance(@PathVariable("id") @Validated Long id, Integer pageNum, Integer pageSize){
        return orderMoneyRecordService.searchOrderMoneyRecords(id,pageNum,pageSize);//根据Service方法，返回余额对象
    }

}
