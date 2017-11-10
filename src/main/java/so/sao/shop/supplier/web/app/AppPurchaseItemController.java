package so.sao.shop.supplier.web.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.output.AppPurchaseItemOutput;
import so.sao.shop.supplier.service.app.AppPurchaseItemService;

import javax.annotation.Resource;

/**
 * Created by acer on 2017/9/7.
 */
@RestController
@RequestMapping("/order")
public class AppPurchaseItemController {
    @Resource
    private AppPurchaseItemService appPurchaseItemService;
    @GetMapping(value = "/appOrderItem/{orderId}")
    public Result findPurchaseItemByOrderId(@PathVariable("orderId") String orderId) throws Exception{
        AppPurchaseItemOutput appPurchaseItemOutput = appPurchaseItemService.findOrderItemList(orderId);
        if(null != appPurchaseItemOutput) {
            return Result.success(Constant.MessageConfig.MSG_SUCCESS, appPurchaseItemOutput);
        }
        return Result.fail(Constant.MessageConfig.MSG_FAILURE);
    }
}
