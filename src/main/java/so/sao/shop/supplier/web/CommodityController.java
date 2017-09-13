package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.config.CommConstant;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.*;
import so.sao.shop.supplier.service.CommodityService;
import so.sao.shop.supplier.util.CheckUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * 商品管理 Controller
 * Created by QuJunLong on 2017/7/17.
 */
@RestController
@RequestMapping("/comm")
@Api(description = "商品管理接口")
public class CommodityController {

    @Autowired
    private CommodityService commodityService;

    @ApiOperation(value="查询供应商商品信息集合（高级搜索）", notes="根据参数返回符合条件的商品信息集合（高级搜索）【责任人：刘刚】")
    @GetMapping(value="/search")
    public Result search(HttpServletRequest request, CommSearchInput commSearchInput) throws Exception {

        //供应商ID校验
        commSearchInput.setSupplierId(CheckUtil.supplierIdCheck(request,commSearchInput.getSupplierId()));
        return commodityService.searchCommodities(commSearchInput);
    }

    @ApiOperation(value="查询供应商商品信息集合（简单查询）", notes="根据参数返回符合条件的商品信息集合（简单查询）【责任人：刘刚】")
    @GetMapping(value="/simplesearch")
    public Result simpleSearch(HttpServletRequest request, CommSimpleSearchInput commSimpleSearchInput) throws Exception {

        //供应商ID校验
        commSimpleSearchInput.setSupplierId(CheckUtil.supplierIdCheck(request,commSimpleSearchInput.getSupplierId()));
        return commodityService.simpleSearchCommodities(commSimpleSearchInput);
    }

    @ApiOperation(value="查询商品详情信息", notes="根据ID返回相应的商品信息【责任人：刘刚】")
    @GetMapping(value="/get/{id}")
    public Result get(@PathVariable Long id){
        return commodityService.getCommodity(id);
    }

    @ApiOperation(value = "查询商品详情信息", notes = "根据code69返回商品信息【责任人：陈沙】")
    @GetMapping(value = "/findByCode69/{code69}")
    public Result find(@PathVariable String code69) {
        return commodityService.findCommodity(code69);
    }

    @ApiOperation(value="新增商品信息", notes="责任人：武凯江")
    @PostMapping(value="/save")
    public Result save(HttpServletRequest request,@Valid @RequestBody CommodityInput commodityInput,@RequestParam(required = false) Long supplierId) throws Exception {
        //校验供应商ID
        supplierId = CheckUtil.supplierIdCheck(request,supplierId);
        return commodityService.saveCommodity(commodityInput, supplierId);
    }

    @ApiOperation(value="修改商品信息", notes="责任人：武凯江")
    @PutMapping(value="/update")
    public Result update(HttpServletRequest request, @Valid @RequestBody CommodityUpdateInput commodityUpdateInput, @RequestParam(required = false) Long supplierId) throws Exception {
        //校验供应商ID
        supplierId = CheckUtil.supplierIdCheck(request,supplierId);
        return commodityService.updateCommodity(commodityUpdateInput, supplierId);
    }

    @ApiOperation(value="上架商品", notes="【负责人：张瑞兵】")
    @PutMapping(value="/onShelves/{id}")
    public Result onShelves(@PathVariable long id){
        return commodityService.onShelves(id);
    }

    @ApiOperation(value="批量商品上架", notes="【负责人：张瑞兵】")
    @PutMapping(value="/onShelves/batch")
    public Result onShelvesBatch(@RequestParam Long[] ids){
        return commodityService.onShelvesBatch(ids);
    }

    @ApiOperation(value="下架商品", notes="【负责人：张瑞兵】")
    @PutMapping(value="/offShelves/{id}")
    public Result offShelves(@PathVariable long id){
        return commodityService.offShelves(id);
    }

    @ApiOperation(value="批量商品下架", notes="【负责人：张瑞兵】")
    @PutMapping(value="/offShelves/batch")
    public Result offShelvesBatch(@RequestParam Long[] ids){
        return commodityService.offShelvesBatch(ids);
    }

    @ApiOperation(value="删除商品信息", notes="根据ID删除相应的商品【责任人：武凯江】")
    @DeleteMapping(value="/delete/{id}")
    public Result delete(@PathVariable Long id){
        return commodityService.deleteCommodity(id);
    }

    @ApiOperation(value="批量删除商品信息", notes="根据ID批量删除相应的商品【责任人：武凯江】")
    @DeleteMapping(value="/delete/bulk")
    public Result delete(@RequestParam Long[] id){
        return commodityService.deleteCommodities(id);
    }

    @ApiOperation(value="批量导入商品", notes="通过Excel模板批量导入商品信息【负责人：潘帅帅】")
    @PostMapping(value="/importExcel")
    public  Result importExcel(@RequestPart(value = "excelFile") MultipartFile excelFile, HttpServletRequest request,@RequestParam(required = false) Long supplierId ) throws Exception {
        //校验供应商ID
       supplierId = CheckUtil.supplierIdCheck(request,supplierId);
       //判断文件是否选择文件
       if (null == excelFile || excelFile.isEmpty()) {
           return Result.fail("文件为空,请选择文件");
       }
       return commodityService.importExcel(excelFile, request, supplierId);
    }

    @ApiOperation(value="高级查询结果导出商品信息到Excel模板", notes="【责任人：张瑞兵】")
    @GetMapping(value="/exportExcel")
    public Result exportExcel(HttpServletRequest request , HttpServletResponse response , CommExportInput commExportInput) {
        return  commodityService.exportExcel(request , response , commExportInput);
    }

    @ApiOperation(value="简单查询结果导出商品信息到Excel模板", notes="【责任人：张瑞兵】")
    @GetMapping(value="/simpleExportExcel")
    public Result simpleExportExcel(HttpServletRequest request , HttpServletResponse response , CommExportInput commExportInput) {
        return  commodityService.exportExcel(request , response , commExportInput);
    }

}
