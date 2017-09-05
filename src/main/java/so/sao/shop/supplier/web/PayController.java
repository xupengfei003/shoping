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
import java.util.ArrayList;
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
        for (String getOrderId : orderIdList) {
            if (!"2#7".equals(verifyOrderStatus(getOrderId))) {
                return Result.fail(Constant.MessageConfig.ORDER_STATUS_EERO);
            }
        }
        if (payService.updatePurchasePayment(payInput)) {
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }
        return Result.success(Constant.MessageConfig.MSG_FAILURE);
    }

    //验证订单状态
    private String verifyOrderStatus(String orderId) {
        List<String> orderStatusList = new ArrayList<>();
        Integer getOrderStatus = purchaseService.findOrderStatus(orderId);
        orderStatusList.add("2#7");//1
        orderStatusList.add("3#7");//2
        orderStatusList.add("4#5");//3
        orderStatusList.add("6");//5
        orderStatusList.add("6");//7
        return orderStatusList.get(getOrderStatus - 1);
    }
}
