package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import so.sao.shop.supplier.config.StorageConfig;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommSearchInput;
import so.sao.shop.supplier.pojo.input.CommSimpleSearchInput;
import so.sao.shop.supplier.pojo.input.CommodityInput;
import so.sao.shop.supplier.pojo.input.CommodityUpdateInput;
import so.sao.shop.supplier.pojo.output.CommodityExportOutput;
import so.sao.shop.supplier.service.CommodityService;
import so.sao.shop.supplier.util.CheckUtil;
import so.sao.shop.supplier.util.CommodityExcelView;
import so.sao.shop.supplier.util.ExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        CheckUtil.supplierIdCheck(request,commSearchInput.getSupplierId());
        return commodityService.searchCommodities(commSearchInput);
    }

    @ApiOperation(value="查询供应商商品信息集合（简单查询）", notes="根据参数返回符合条件的商品信息集合（简单查询）【责任人：刘刚】")
    @GetMapping(value="/simplesearch")
    public Result simpleSearch(HttpServletRequest request, CommSimpleSearchInput commSimpleSearchInput) throws Exception {

        //供应商ID校验
        CheckUtil.supplierIdCheck(request,commSimpleSearchInput.getSupplierId());
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

    @ApiOperation(value="上架商品", notes="")
    @PutMapping(value="/updateStatusSj/{id}")
    public Result updateStatusSj(@PathVariable long id){
        return commodityService.updateStatusSj(id);
    }

    @ApiOperation(value="批量商品上架", notes="")
    @PutMapping(value="/updateStatusSj/bulk")
    public BaseResult updateStatusSjs(@RequestParam Long[] ids){
        return commodityService.updateStatusSjs(ids);
    }

    @ApiOperation(value="下架商品", notes="")
    @PutMapping(value="/updateStatusXj/{id}")
    public Result updateStatusXj(@PathVariable long id){
        return commodityService.updateStatusXj(id);
    }

    @ApiOperation(value="批量商品下架", notes="")
    @PutMapping(value="/updateStatusXj/bulk")
    public BaseResult updateStatusXjs(@RequestParam Long[] ids){
        return commodityService.updateStatusXjs(ids);
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

    @ApiOperation(value="删除商品图片", notes="根据ID删除相应的商品图片")
    @DeleteMapping(value="/delete/imge/{id}")
    public BaseResult deleteImge(@PathVariable Long id){
      return commodityService.deleteCommImge(id);
    }

    @ApiOperation(value="批量导入商品", notes="通过Excel模板批量导入商品信息")
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

    @ApiOperation(value="导出商品信息", notes="导出商品信息到Excel")
    @GetMapping(value="/exportExcel")
    public ModelAndView exportExcel(@RequestParam Long[] ids){
        if(ids == null || ids.length == 0){
            throw new RuntimeException("请至少选择一个商品进行导出！");
        }
        List<CommodityExportOutput> commodityList = commodityService.findByIds(ids);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("members", commodityList);
        map.put("name", "商品信息表");
        ExcelView excelView = new CommodityExcelView();
        return new ModelAndView(excelView, map);
    }

}
