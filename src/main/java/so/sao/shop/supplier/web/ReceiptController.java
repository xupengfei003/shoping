package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.Receipt;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.ReceiptService;
import so.sao.shop.supplier.util.Ognl;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

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
    public Result createReceipt(@RequestBody @Valid Receipt receipt) {

        /**
         * 1.校验入参
         * 2.根据门店ID和发票类型搜索是否存在此门店相同类型的发票记录
         *  3.1.若存在则调用更新方法
         *  3.2.若不存在则调用创建方法
         */

        //1.检验入参
        if (this.validatedParam(receipt)){
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }

        //2.查询该门店该类发票类型是否存在相关记录
        boolean flag = false;
        Receipt receiptDb = receiptService.getReceiptByUserIdAndType(receipt.getUserId(),receipt.getReceiptType());
        Date date = new Date();

        //3.1.如果不存在，执行插入操作
        if (Ognl.isEmpty(receiptDb)){
            receipt.setCreateTime(date);
            flag = receiptService.insertReceipt(receipt);
        }
        //3.2.如果不存在,执行更新操作
        else {
            receipt.setReceiptId(receiptDb.getReceiptId());
            receipt.setUpdateTime(date);
            flag = receiptService.updateReceiptById(receipt);
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

        //1.入参校验
        if (Ognl.isEmpty(userId) || Ognl.isEmpty(receiptType)) {
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }
        if (!(Constant.ReceiptConfig.RECEIPTTYPE_COMPANY.equals(receiptType) || Constant.ReceiptConfig.RECEIPTTYPE_SPECIAL.equals(receiptType))){
            return Result.fail(Constant.MessageConfig.PARAMETER_ABNORMITY);
        }

        //2.调用业务层方法
        Receipt receipt = receiptService.getReceiptByUserIdAndType(userId,receiptType);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS,receipt);
    }

    /**
     * 参数校验
     * @param receipt
     * @return
     */
    private boolean validatedParam(Receipt receipt){
        /**
         * 根据发票类型校验参数
         *     若发票类型为1 表示增值税普通单位发票 单位名称，纳税人识别号为必传参数(此处已在入参时校验)
         *     若发票类型为2 表示增值税专用发票  单位名称，纳税人识别号，注册地址，注册电话，开户银行，银行账户为必传参数
         */
        if (Ognl.isNotEmpty(receipt)){
            //发票类型为2
            if (Constant.ReceiptConfig.RECEIPTTYPE_SPECIAL.equals(receipt.getReceiptType())){
                boolean flag = Ognl.isEmpty(receipt.getRegisterAdress()) && Ognl.isEmpty(receipt.getRegisterPhone()) && Ognl.isEmpty(receipt.getDepositBank())
                        && Ognl.isEmpty(receipt.getBankAmount());
                return flag;
            }
        }
        return false;
    }
}
