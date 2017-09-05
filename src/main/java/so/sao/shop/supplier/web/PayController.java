package so.sao.shop.supplier.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private PayService payService;
    @Resource
    private PurchaseService purchaseService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result create(@Valid PayInput payInput, BindingResult result, String sign) throws Exception {
        Result output = new Result();
        //判断验证是否通过。true 未通过  false通过
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                output.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
                output.setMessage(error.getDefaultMessage());
            }
        } else {
            //验证订单状态格式
            List<String> orderIdList = purchaseService.findOrderStatusByPayId(payInput.getOrderId());//获取该支付ID下的所有订单ID
            for (String getOrderId : orderIdList) {
                if (!verifyOrderStatus(getOrderId, Constant.OrderStatusConfig.PENDING_SHIP)) {
                    return Result.fail(Constant.MessageConfig.ORDER_STATUS_EERO);
                }
            }
            if (payService.updatePurchasePayment(sign, payInput)) {
                output.setCode(Constant.CodeConfig.CODE_SUCCESS);
                output.setMessage(Constant.MessageConfig.MSG_SUCCESS);
            } else {
                output.setMessage(Constant.MessageConfig.MSG_FAILURE);
                output.setCode(Constant.CodeConfig.CODE_FAILURE);
            }
        }
        return output;
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
