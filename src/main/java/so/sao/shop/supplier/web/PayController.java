package so.sao.shop.supplier.web;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.PayInput;
import so.sao.shop.supplier.service.PayService;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by bzh on 2017/8/15.
 */
@RestController
@RequestMapping("/pay")
public class PayController {
    @Resource
    private PayService payService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result create(@RequestBody @Valid PayInput payInput) throws Exception {
        if (payService.updatePurchasePayment(payInput)) {
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }
        return Result.success(Constant.MessageConfig.MSG_FAILURE);
    }
}
