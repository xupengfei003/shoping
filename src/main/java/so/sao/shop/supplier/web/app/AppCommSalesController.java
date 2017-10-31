package so.sao.shop.supplier.web.app;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.AppCommSalesInput;
import so.sao.shop.supplier.service.app.AppCommSalesService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *<p>Version: shop-supplier V1.1.0 </p>
 *<p>Title: AppCommSalesController</p>
 *<p>Description: 商品销量相关接口</p>
 *@author: liugang
 *@Date: Created in 2017/10/30 15:46
 */
@RestController
@RequestMapping("/order")
@Api(description = "统计商品销售量接口")
public class AppCommSalesController {
    @Resource
    private AppCommSalesService appCommSalesService;
    @GetMapping(value = "/countOrderNum/{goodsId}")
    @ApiOperation(value = "获取已销售商品数量", notes = "负责人【白治华】")
    public Result countOrderNum(@PathVariable("goodsId") String goodsIds) throws Exception{
        String[] goodsIdArr = goodsIds.split(",");
        List<String> countNum = appCommSalesService.countSoldCommNum(goodsIdArr);
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("goodsCount",countNum);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS,resMap);
    }

/*    @PostMapping(value = "/saveCommSales/{goodsId}")
    @ApiOperation(value = "添加商品销量", notes = "用于功能测试")
    public Result saveCommSales(String goodsIds){
        Result result = new Result();
        appCommSalesService.saveCommSales(goodsIds);
        result.setCode(1);
        result.setMessage("添加成功");
        return result;
    }

    @PutMapping(value = "/updateSalesNum")
    @ApiOperation(value = "更新商品销量", notes = "用于功能测试")
    public Result saveCommSales(@RequestBody List<AppCommSalesInput> commSalesInputs){
        Result result = new Result();
        appCommSalesService.updateSalesNum(commSalesInputs);
        result.setCode(1);
        result.setMessage("更新销量成功");
        return result;
    }*/
}
