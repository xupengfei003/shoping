package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.Receipt;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.ReceiptService;
import so.sao.shop.supplier.util.Ognl;

import javax.annotation.Resource;

/**
 * <p>Version: supplier2 V1.1.0 </p>
 * <p>Title: ReceiptController</p>
 * <p>Description: </p>
 *
 * @author: zhenhai.zheng
 * @Date: Created in 2017/11/1 10:19
 */
@RestController
@RequestMapping("/receipt")
@Api(description = "发票类-所有接口")
public class ReceiptController {
    @Resource
    private ReceiptService receiptService;

    /**
     * 发票记录录入或更改
     * @return
     */
    @ApiOperation(value = "发票记录录入或更改",notes = "发票记录录入或更改【负责人：郑振海】")
    @PostMapping("/createReceipt")
    public Result createReceipt(Receipt receipt) {

        /**
         * 1.根据门店ID和发票类型搜索是否存在此门店相同类型的发票记录
         * 2.1.若存在则调用更新方法
         * 2.2.若不存在则调用创建方法
         */
        boolean flag = false;
        Receipt receiptDb = receiptService.getReceiptByUserIdAndType(receipt.getUserId(),receipt.getReceiptType());
        if (Ognl.isEmpty(receiptDb)){
            flag = receiptService.insertReceipt(receipt);
        }else {
            flag = receiptService.updateReceiptByUserId(receipt);
        }
        return flag == true ? Result.success(Constant.MessageConfig.MSG_SUCCESS): Result.fail(Constant.MessageConfig.MSG_FAILURE);
    }

    /**
     * 根据门店ID和发票类型获取发票记录
     * @param userId
     * @param receiptType 1增值税普通单位发票 2增值税专用发票
     * @return
     */
    @ApiOperation(value = "根据门店ID和发票类型获取发票记录",notes = "根据门店ID和发票类型获取发票记录【负责人：郑振海】")
    @GetMapping("/getReceipt/{userId}")
    public Result getReceiptByUserIdAndType(@PathVariable("userId") Long userId, Integer receiptType) {

        /**
         * 1.入参校验
         *   userId和receiptType不能为空
         *   receiptType值为1或2
         * 2.调用业务层方法
         */
        if (Ognl.isEmpty(userId) || Ognl.isEmpty(receiptType)) {
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }
        if (Constant.ReceiptConfig.RECEIPTTYPE_COMPANY.equals(receiptType) || Constant.ReceiptConfig.RECEIPTTYPE_SPECIAL.equals(receiptType)){
            return Result.fail(Constant.MessageConfig.PARAMETER_ABNORMITY);
        }
        Receipt receipt = receiptService.getReceiptByUserIdAndType(userId,receiptType);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS,receipt);
    }
}
