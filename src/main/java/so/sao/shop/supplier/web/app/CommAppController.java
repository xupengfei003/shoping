package so.sao.shop.supplier.web.app;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommAppInput;
import so.sao.shop.supplier.pojo.input.CommodityAppInput;
import so.sao.shop.supplier.pojo.output.CommAppOutput;
import so.sao.shop.supplier.service.CommodityService;
import so.sao.shop.supplier.service.app.CommAppService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/comm/app")
public class CommAppController {

    @Autowired
    private CommAppService commAppService;
    @Autowired
    private CommodityService commodityService;

    @GetMapping(value="/searchAll")
    public Result searchAll(@RequestParam(required = false) Long supplierId,@RequestParam(required = false) String sku, @RequestParam(required = false) String code69,
                            @RequestParam(required = false) String suppCommCode, @RequestParam(required = false) String inputValue, @RequestParam(required = false) BigDecimal minPrice,
                            @RequestParam(required = false) BigDecimal maxPrice, @RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize){

        return commAppService.searchAllCommodities(supplierId, sku, code69, suppCommCode,inputValue, minPrice, maxPrice, pageNum, pageSize);
    }


    @GetMapping(value="/searchSuppliers/{code69}")
    public Result searchSuppliers(@PathVariable String code69){
        return commAppService.searchSuppliers(code69);
    }

    @GetMapping(value="/searchCategories")
    public Result searchCategories(){
        return commAppService.searchCategories();
    }

    @GetMapping(value="/getBrandName")
    public Result getBrandName(@RequestParam(required = false)  String name){
        return commAppService.getBrandName(name);
    }

    @GetMapping(value="/getCommodity/{id}")
    public Result getCommodity(@PathVariable Long id){
        return commAppService.getCommodity(id);
    }

    @GetMapping(value="/getSuppliers")
    public Result getSuppliers(@RequestParam(required = false) Long accountId,@RequestParam(required = false) String providerName,@RequestParam(required = false) Integer pageNum,
                               @RequestParam(required = false) Integer pageSize){
        return commAppService.getSuppliers(accountId,providerName,pageNum,pageSize);
    }

    @GetMapping(value="/getCommodities")
    public Result getCommodities(CommAppInput commAppInput) throws Exception {
        return commAppService.getCommodities(commAppInput);
    }
    @GetMapping(value = "/mainCategory")
    public Result getMainCategory (@RequestParam Long supplierId){
        return commAppService.getMainCateGory(supplierId);
    }


    @GetMapping(value="/searchTwoOrThreeLevelCommCategories")
    public Result searchTwoOrThreeLevelCommCategories(@RequestParam(required = false) Long supplierId, @RequestParam(required = true) Integer level){
        return commAppService.getAllLevelTwoOrThreeCategories( supplierId,level );
    }

    @GetMapping(value="/getAllBrands")
    public Result getAllBrands( @RequestParam(required = false) Long supplierId, @RequestParam(required = false) Integer categoryId){
        return commAppService.getAllBrands( supplierId, categoryId );
    }

    @GetMapping(value="/searchCommoditiesByConditionOrder")
    public PageInfo<CommAppOutput> searchCommoditiesByConditionOrder( CommodityAppInput commodityAppInput){
        return commAppService.searchCommodities( commodityAppInput );
    }
    @GetMapping(value = "/listCommodities")
    public Result listCommodity(@RequestParam(required = false) Long supplierId, @RequestParam(required = false) String commName,
                                @RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize){
        return commAppService.listCommodities(supplierId,commName, pageNum, pageSize);
    }
    
<<<<<<< HEAD
    @ApiOperation(value="查询商品V2.4.0", notes="根据商品名称模糊查询商品，返回商品列表")
=======

>>>>>>> 8c9da54aa68045def053ad94a6c6b4820f4c7135
    @GetMapping(value="/getNames/{goodsName}")
    public Result searchGoods(@PathVariable String goodsName){
        return commAppService.getGoods(goodsName);
    }

	
	
	
<<<<<<< HEAD
	@ApiOperation(value="查询商品 v2.5.2", notes="搜索接口（根据商品名称/供应商名称/品牌名称模糊匹配）【责任人：刘刚】")
=======
>>>>>>> 8c9da54aa68045def053ad94a6c6b4820f4c7135
    @GetMapping(value="/getNames")
    public Result searchGoods(@RequestParam String name, @RequestParam Integer nameType){
        return commAppService.getGoods(name, nameType);
    }


    @GetMapping(value="/searchCommsByName")
    public Result searchComms(@RequestParam(required = false) String name,
                              @RequestParam(required = false) Integer pageNum,
                              @RequestParam(required = false) Integer pageSize ){
        return commAppService.getComms(name, pageNum, pageSize);
    }

    /**
     * 根据分类等级查询全部商品科属信息或供应商商品科属信息
     *
     * @param supplierId 供应商ID
     * @param level  商品科属分类等级
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/searchCategoriesByLevel")
    public Result getCategoriesByLevel(Long supplierId,@RequestParam(required = true)Integer level) throws Exception {
        // 校验分类等级参数，正确则进行查询，错误则返回查询失败
        if (new Integer(1).equals(level) || new Integer(2).equals(level) || new Integer(3).equals(level)){
            return commAppService.getCategories(supplierId,level);
        } else {
            return Result.fail("商品科属分类等级错误");
        }
    }

    /**
     * 根据一级分类ID查询对应二级及三级分类信息
     *
     * @param supplierId 供应商ID
     * @param id  一级分类ID
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/twoAndThreeCategories")
    public Result getTwoAndThreeCategories(Long supplierId,Long id) throws Exception {
        return commAppService.getTwoAndThreeCategories(supplierId,id);
    }


}

