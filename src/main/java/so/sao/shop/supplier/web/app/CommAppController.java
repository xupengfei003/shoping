package so.sao.shop.supplier.web.app;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommAppInput;
import so.sao.shop.supplier.pojo.input.CommodityAppInput;
import so.sao.shop.supplier.pojo.output.CommAppOutput;
import so.sao.shop.supplier.service.app.CommAppService;
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

    @ApiOperation(value="根据code69/供应商ID/商品名称/分类/品牌id查询商品详情", notes="根据供应商ID/商品名称/分类/品牌id查询商品详情")
    @GetMapping(value="/getCommodities")
    public Result getCommodities(CommAppInput commAppInput){
        return commAppService.getCommodities(commAppInput);
    }
    @ApiOperation(value = "根据供应商ID查询主营商品" ,notes = "根据供应商ID查询主营商品")
    @GetMapping(value = "/mainCategory")
    public Result getMainCategory (@RequestParam Long supplierId){
        return commAppService.getMainCateGory(supplierId);
    }


    @ApiOperation(value="根据科属的等级参数获取所有的2或3级科属", notes="获取所有的2或3级科属【责任人：许鹏飞】")
    @GetMapping(value="/searchTwoOrThreeLevelCommCategories")
    public Result searchTwoOrThreeLevelCommCategories(@RequestParam(required = true) Integer level){
        return commAppService.getAllLevelTwoOrThreeCategories(level);
    }

    @ApiOperation(value="查询商品的全部品牌", notes="获取一类或者二类商品的全部品牌【责任人：许鹏飞】")
    @GetMapping(value="/getAllBrands")
    public Result getAllBrands( @RequestParam(required = false) Integer categoryId){
        return commAppService.getAllBrands( categoryId );
    }

    @ApiOperation(value="根据动态条件(供应商ID/分类/品牌ids/排序条件)查询商品", notes="动态条件查询商品【责任人：许鹏飞】")
    @GetMapping(value="/searchCommoditiesByConditionOrder")
    public PageInfo<CommAppOutput> searchCommoditiesByConditionOrder(CommodityAppInput commodityAppInput){
        return commAppService.searchCommodities( commodityAppInput );
    }



}

