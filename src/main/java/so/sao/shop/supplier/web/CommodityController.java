package so.sao.shop.supplier.web;

import com.github.pagehelper.PageInfo;
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
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommodityInput;
import so.sao.shop.supplier.pojo.input.CommodityUpdateInput;
import so.sao.shop.supplier.pojo.output.CommodityExportOutput;
import so.sao.shop.supplier.pojo.output.CommodityImportOutput;
import so.sao.shop.supplier.pojo.output.CommodityOutput;
import so.sao.shop.supplier.service.CommodityService;
import so.sao.shop.supplier.util.CheckUtil;
import so.sao.shop.supplier.util.CommodityExcelView;
import so.sao.shop.supplier.util.Constant;
import so.sao.shop.supplier.util.ExcelView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
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
    private StorageConfig storageConfig;

    @Autowired
    private CommodityService commodityService;

    @ApiOperation(value="查询供应商商品信息集合（高级搜索）", notes="根据参数返回符合条件的商品信息集合（高级搜索）")
    @GetMapping(value="/search")
    public Result search(HttpServletRequest request, @RequestParam(required = false)  Long supplierId, @RequestParam(required = false) String commCode69, @RequestParam(required = false) String sku,
                         @RequestParam(required = false) String suppCommCode, @RequestParam(required = false) String commName, @RequestParam(required = false) Integer status,
                         @RequestParam(required = false) Long typeId, @RequestParam(required = false) BigDecimal minPrice, @RequestParam(required = false) BigDecimal maxPrice,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE) Date beginCreateAt,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE) Date endCreateAt,
                         @RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize) throws Exception {

        //供应商ID校验
        supplierId = CheckUtil.supplierIdCheck(request,supplierId);
        return commodityService.searchCommodities(supplierId, commCode69, sku, suppCommCode, commName, status, typeId, minPrice, maxPrice, beginCreateAt, endCreateAt, pageNum, pageSize);
    }

    @ApiOperation(value="查询供应商商品信息集合（简单查询）", notes="根据参数返回符合条件的商品信息集合（简单查询）")
    @GetMapping(value="/simplesearch")
    public Result simpleSearch(HttpServletRequest request,@RequestParam(required = false)  Long supplierId,@RequestParam(required = false)  String inputvalue,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE) Date beginCreateAt,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE) Date endCreateAt,
                         @RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize) throws Exception {

        //供应商ID校验
        supplierId = CheckUtil.supplierIdCheck(request,supplierId);
        return commodityService.simpleSearchCommodities(supplierId, inputvalue, beginCreateAt, endCreateAt, pageNum, pageSize);
    }

    @ApiOperation(value="查询商品详情信息", notes="根据ID返回相应的商品信息")
    @GetMapping(value="/get/{id}")
    public Result get(@PathVariable Long id){
        return commodityService.getCommodity(id);
    }

    @ApiOperation(value="查询商品详情信息", notes="根据code69返回相应的商品信息")
    @GetMapping(value="/findByCode69/{code69}")
    public Result find(@PathVariable String code69){
        return commodityService.findCommodity(code69);
    }

    @ApiOperation(value="新增商品信息", notes="")
    @PostMapping(value="/save")
    public Result save(HttpServletRequest request,@Valid @RequestBody CommodityInput commodityInput,@RequestParam(required = false) Long supplierId, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            Result result = new Result();
            List<ObjectError> list = bindingResult.getAllErrors();
            for (ObjectError error : list) {
                result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_NOT_EMPTY);
                result.setMessage(error.getDefaultMessage());
            }
            return result;
        }else{
            //校验供应商ID
            supplierId = CheckUtil.supplierIdCheck(request,supplierId);
            return commodityService.saveCommodity(commodityInput, supplierId);
        }
    }

    @ApiOperation(value="修改商品信息", notes="责任人：武凯江")
    @PutMapping(value="/update")
    public Result update(HttpServletRequest request, @Valid @RequestBody CommodityUpdateInput commodityUpdateInput, @RequestParam(required = false) Long supplierId, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            Result result = new Result();
            List<ObjectError> list = bindingResult.getAllErrors();
            for (ObjectError error : list) {
                result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_NOT_EMPTY);
                result.setMessage(error.getDefaultMessage());
            }
            return result;
        }else{
            //校验供应商ID
            supplierId = CheckUtil.supplierIdCheck(request,supplierId);
            return commodityService.updateCommodity(commodityUpdateInput, supplierId);
        }
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


    @ApiOperation(value="删除商品信息", notes="根据ID删除相应的商品")
    @DeleteMapping(value="/delete/{id}")
    public Result delete(@PathVariable Long id){
        return commodityService.deleteCommodity(id);
    }

    @ApiOperation(value="批量删除商品信息", notes="根据ID批量删除相应的商品")
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
        return   commodityService.importExcel(excelFile,request,storageConfig,supplierId);
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
