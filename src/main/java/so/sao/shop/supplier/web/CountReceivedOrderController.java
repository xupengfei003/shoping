package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.CountSoldCommService;

import javax.annotation.Resource;

/**
 * Created by acer on 2017/9/8.
 */
@RestController
@RequestMapping("/order")
@Api(description = "统计商品销售量接口")
public class CountReceivedOrderController {
    @Resource
    private CountSoldCommService countSoldCommService;
    @GetMapping(value = "/countOrderNum/{goodsId}")
    @ApiOperation(value = "获取已销售商品数量", notes = "负责人【白治华】")
    public Result countOrderNum(@PathVariable("goodsId") String goodsId) throws Exception{
        Integer countNum = countSoldCommService.countSoldCommNum(goodsId);
        if(countNum > 0){
            return Result.success(Constant.MessageConfig.MSG_SUCCESS,countNum);
        }
        return Result.fail(Constant.MessageConfig.MSG_FAILURE);
    }

}
