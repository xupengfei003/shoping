package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.CommAppService;
import so.sao.shop.supplier.service.CommodityService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/comm/app")
@Api(description = "商品管理模块对接店主APP接口")
public class CommAppController {

    @Autowired
    private CommAppService commAppService;
    @Autowired
    private CommodityService commodityService;

    @ApiOperation(value="查询所有已上架商品信息集合", notes="根据参数返回符合条件的商品信息集合【责任人：刘刚】")
    @GetMapping(value="/searchAll")
    public Result searchAll(@RequestParam(required = false) Long supplierId,@RequestParam(required = false) String sku, @RequestParam(required = false) String code69,
                            @RequestParam(required = false) String suppCommCode, @RequestParam(required = false) String inputValue, @RequestParam(required = false) BigDecimal minPrice,
                            @RequestParam(required = false) BigDecimal maxPrice, @RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize){

        return commAppService.searchAllCommodities(supplierId, sku, code69, suppCommCode,inputValue, minPrice, maxPrice, pageNum, pageSize);
    }


    @ApiOperation(value="根据code69查询供应商列表", notes="根据code69返回供应商列表")
    @GetMapping(value="/searchSuppliers/{code69}")
    public Result searchSuppliers(@PathVariable String code69){
        return commAppService.searchSuppliers(code69);
    }

    @ApiOperation(value="获取所有商品分类", notes="获取所有商品分类【责任人：刘刚】")
    @GetMapping(value="/searchCategories")
    public Result searchCategories(){
        return commAppService.searchCategories();
    }

    @ApiOperation(value="查询品牌名称", notes="模糊查询品牌名称")
    @GetMapping(value="/getBrandName")
    public Result getBrandName(@RequestParam(required = false)  String name){
        return commAppService.getBrandName(name);
    }

    @ApiOperation(value="根据id查询商品详情", notes="根据供应商商品表ID查询商品详情")
    @GetMapping(value="/getCommodity/{id}")
    public Result getCommodity(@PathVariable Long id){
        return commodityService.getCommodity(id);
    }

    @ApiOperation(value="根据供应商ID或名称查询供应商详情", notes="根据供应商ID或名称查询供应商详情")
    @GetMapping(value="/getSuppliers")
    public Result getSuppliers(@RequestParam(required = false) Long accountId,@RequestParam(required = false) String providerName,@RequestParam(required = false) Integer pageNum,
                               @RequestParam(required = false) Integer pageSize){
        return commAppService.getSuppliers(accountId,providerName,pageNum,pageSize);
    }

    @ApiOperation(value="根据名称/分类/品牌id查询商品信息", notes="根据名称/分类/品牌id查询供商品详情")
    @GetMapping(value="/getCommodities")
    public Result getCommodities(@RequestParam(required = false) String commName,@RequestParam(required = false) Long categoryOneId,
                                 @RequestParam(required = false) Long categoryTwoId,@RequestParam(required = false) Long categoryThreeId,
                                 @RequestParam(required = false) Long[] brandIds, @RequestParam(required = false) Integer pageNum,
                                 @RequestParam(required = false) Integer pageSize){
        return commAppService.getCommodities(commName,categoryOneId,categoryTwoId,categoryThreeId,brandIds,pageNum,pageSize);
    }

}

