package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.PayInput;
import so.sao.shop.supplier.service.PayService;
import so.sao.shop.supplier.service.PurchaseService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by bzh on 2017/8/15.
 */
@RestController
@RequestMapping("/pay")
@Api(description = "支付接口")
public class PayController {

    @Resource
    private PayService payService;
    @Resource
    private PurchaseService purchaseService;

    /**
     * 支付回调接口
     *
     * @param payInput 封装了回调参数
     * @return Result 封装了结果
     * @throws Exception 异常
     */
    @PostMapping(value = "/create")
    @ApiOperation(value = "支付接口",notes = "")
    public Result create(@RequestBody @Valid PayInput payInput) throws Exception {
        //验证订单状态格式
        List<String> orderIdList = purchaseService.findOrderStatusByPayId(payInput.getOrderId());//获取该支付ID下的所有订单ID
        for (String getOrderId : orderIdList) {
            if(!verifyOrderStatus(getOrderId,Constant.OrderStatusConfig.PENDING_SHIP)){
                return Result.fail(Constant.MessageConfig.ORDER_STATUS_EERO);
            }
        }
        if (payService.updatePurchasePayment(payInput)) {
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }
        return Result.fail(Constant.MessageConfig.MSG_FAILURE);
    }

    /**
     * 支付回调接口(单订单支付)
     *
     * @param payInput 封装了回调参数
     * @return Result 封装了结果
     * @throws Exception 异常
     */
    @PostMapping(value = "/createPaymentByOrderId")
    @ApiOperation(value = "单订单支付接口",notes = "")
    public Result createPaymentByOrderId(@RequestBody @Valid PayInput payInput) throws Exception{
        //验证订单状态格式
        if(!verifyOrderStatus(payInput.getOrderId(),Constant.OrderStatusConfig.PENDING_SHIP)){
            return Result.fail(Constant.MessageConfig.ORDER_STATUS_EERO);
        }
        if (payService.updatePurchasePaymentByOrderId(payInput)) {
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }
        return Result.fail(Constant.MessageConfig.MSG_FAILURE);
    }

    /**
     * 根据支付ID获取支付总金额
     *
     * @param orderId 合并支付ID
     * @return Result 封装了结果
     */
    @GetMapping(value = "/getPayOrderTotalPrice/{orderId}")
    @ApiOperation(value = "获取支付总金额",notes = "")
    public Result getPayOrderTotalPriceByPayId(@PathVariable String orderId){
        BigDecimal totalPrice = payService.getPayOrderTotalPriceByPayId(orderId);
        if (null != totalPrice) {
            return Result.success(Constant.MessageConfig.MSG_SUCCESS,totalPrice);
        }
        return Result.success(Constant.MessageConfig.MSG_SUCCESS,0);
    }
    //验证订单状态
    private boolean verifyOrderStatus(String orderId,Integer orderStatus) {
        Integer getOrderStatus = purchaseService.findOrderStatus(orderId);
        String status = Constant.OrderStatusRule.RULES[getOrderStatus-1];
        return status.contains(String.valueOf(orderStatus));
    }
}
