package so.sao.shop.supplier.web;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.PayInput;
import so.sao.shop.supplier.service.PayService;
import so.sao.shop.supplier.service.PurchaseService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by bzh on 2017/8/15.
 */
@RestController
@RequestMapping("/pay")
public class PayController {
    @Resource
    private PayService payService;

    @Resource
    private PurchaseService purchaseService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result create(@RequestBody @Valid PayInput payInput) throws Exception {
        //验证订单状态格式
        List<String> orderIdList = purchaseService.findOrderStatusByPayId(payInput.getOrderId());//获取该支付ID下的所有订单ID
        for(String getOrderId : orderIdList){
            if (!verifyOrderStatus(getOrderId, Constant.OrderStatusConfig.PENDING_SHIP)) {
                return Result.fail(Constant.MessageConfig.ORDER_STATUS_EERO);
            }
        }
        if (payService.updatePurchasePayment(payInput)) {
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }
        return Result.success(Constant.MessageConfig.MSG_FAILURE);
    }

    //验证订单状态
    private boolean verifyOrderStatus(String orderId, Integer orderStatus) {
        boolean flag = false;
        Integer getOrderStatus = purchaseService.findOrderStatus(orderId);
        //待付款 --> 待发货
        if (orderStatus == Constant.OrderStatusConfig.PENDING_SHIP && getOrderStatus == Constant.OrderStatusConfig.PAYMENT) {
            flag = true;
        }

        //待付款 --> 已取消 / 待发货 --> 已取消
        if ((getOrderStatus == Constant.OrderStatusConfig.PAYMENT || getOrderStatus == Constant.OrderStatusConfig.PENDING_SHIP) && orderStatus == Constant.OrderStatusConfig.CANCEL_ORDER) {
            flag = true;
        }
        //待发货 --> 已发货
        if (getOrderStatus == Constant.OrderStatusConfig.PENDING_SHIP && orderStatus == Constant.OrderStatusConfig.ISSUE_SHIP) {
            flag = true;
        }
        //已发货 --> 已完成 / 已发货 --> 已拒收
        if (getOrderStatus == Constant.OrderStatusConfig.ISSUE_SHIP && (orderStatus == Constant.OrderStatusConfig.RECEIVED || orderStatus == Constant.OrderStatusConfig.REJECT)) {
            flag = true;
        }
        //已拒收 --> 已退款 / 已取消 --> 已退款
        if ((getOrderStatus == Constant.OrderStatusConfig.REJECT || getOrderStatus == Constant.OrderStatusConfig.CANCEL_ORDER) && orderStatus == Constant.OrderStatusConfig.REFUNDED) {
            flag = true;
        }
        return flag;
    }
}
