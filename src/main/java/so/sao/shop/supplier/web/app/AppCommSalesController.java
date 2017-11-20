package so.sao.shop.supplier.web.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
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
public class AppCommSalesController {
    @Resource
    private AppCommSalesService appCommSalesService;
    @GetMapping(value = "/countOrderNum/{goodsId}")
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
