package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.CommAppService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/comm/app")
@Api(description = "商品管理模块对接店主APP接口")
public class CommAppController {

    @Autowired
    private CommAppService commAppService;

    @ApiOperation(value="查询所有已上架商品信息集合", notes="根据参数返回符合条件的商品信息集合【责任人：刘刚】")
    @GetMapping(value="/searchAll")
    public Result searchAll(@RequestParam(required = false) Long supplierId,@RequestParam(required = false) String sku, @RequestParam(required = false) String code69,
                            @RequestParam(required = false) String suppCommCode, @RequestParam(required = false) String inputValue, @RequestParam(required = false) BigDecimal minPrice,
                            @RequestParam(required = false) BigDecimal maxPrice, @RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize){

        return commAppService.searchAllCommodities(supplierId, sku, code69, suppCommCode,inputValue, minPrice, maxPrice, pageNum, pageSize);
    }


    @ApiOperation(value="查询供应商列表", notes="根据code69返回供应商列表【责任人：陈沙】")
    @GetMapping(value="/searchSuppliers/{code69}")
    public Result searchSuppliers(@PathVariable String code69){
        return commAppService.searchSuppliers(code69);
    }

    @ApiOperation(value="获取所有商品分类", notes="获取所有商品分类【责任人：刘刚】")
    @GetMapping(value="/searchCategories")
    public Result searchCategories(){
        return commAppService.searchCategories();
    }


}
