package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.output.AppPurchaseItemOutput;
import so.sao.shop.supplier.service.AppPurchaseItemService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by acer on 2017/9/7.
 */
@RestController
@RequestMapping("/appOrderItem")
@Api(description = "App订单详情类-所有接口")
public class AppPurchaseItemController {
    @Resource
    private AppPurchaseItemService appPurchaseItemService;
    @GetMapping(value = "/appOrderItemList")
    @ApiOperation(value = "门店端获取订单列表", notes = "负责人【白治华】")
    public Result findPurchaseItemByOrderId(String orderId) throws Exception{
        List<AppPurchaseItemOutput> appPurchaseItemOutputs = appPurchaseItemService.findOrderItemList(orderId);
        if(null != appPurchaseItemOutputs && appPurchaseItemOutputs.size()>0){
            return Result.success(Constant.MessageConfig.MSG_SUCCESS,appPurchaseItemOutputs);
        } else if(appPurchaseItemOutputs.size() == 0){
            return Result.success(Constant.MessageConfig.MSG_SUCCESS,null);
        }
        return Result.fail(Constant.MessageConfig.MSG_FAILURE);
    }
}
