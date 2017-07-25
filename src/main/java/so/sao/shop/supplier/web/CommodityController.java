package so.sao.shop.supplier.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommodityInput;
import so.sao.shop.supplier.pojo.output.CommodityExportOutput;
import so.sao.shop.supplier.pojo.output.CommodityImportOutput;
import so.sao.shop.supplier.pojo.output.CommodityOutput;
import so.sao.shop.supplier.service.CommodityService;
import so.sao.shop.supplier.util.CommodityExcelView;
import so.sao.shop.supplier.util.Constant;
import so.sao.shop.supplier.util.ExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

    @ApiOperation(value="查询供应商商品信息集合", notes="根据参数返回符合条件的商品信息集合")
    @GetMapping(value="/search")
    public PageInfo search(HttpServletRequest request, @RequestParam(required = false) Long supplierId,@RequestParam(required = false) String commCode69,@RequestParam(required = false) Long commId,
                           @RequestParam(required = false) String suppCommCode,@RequestParam(required = false) String commName,
                           @RequestParam(required = false) Integer status,@RequestParam(required = false) Long typeId,@RequestParam(required = false) Double minPrice,
                           @RequestParam(required = false) Double maxPrice,@RequestParam int pageNum, @RequestParam int pageSize){
        if(supplierId==null||supplierId==0){
            supplierId = commodityService.findAccountByUserId(((User) request.getAttribute(Constant.REQUEST_USER)).getId()).getAccountId();
        }
        return commodityService.searchCommodities(supplierId, commCode69, commId, suppCommCode, commName, status, typeId, minPrice, maxPrice, pageNum, pageSize);
    }

    @ApiOperation(value="查询商品详情信息", notes="根据ID返回相应的商品信息")
    @GetMapping(value="/get/{id}")
    public CommodityOutput get(@PathVariable Long id){
        return commodityService.getCommodity(id);
    }

    @ApiOperation(value="新增商品信息", notes="")
    @PostMapping(value="/save")
    public BaseResult save(HttpServletRequest request,@Valid @RequestBody CommodityInput commodityInput){
        return commodityService.saveCommodity(request,commodityInput);
    }

    @ApiOperation(value="修改商品信息", notes="")
    @PutMapping(value="/update")
    public BaseResult update(HttpServletRequest request,@Valid CommodityInput commodityInput){
        return commodityService.updateCommodity(request,commodityInput);
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
    public  List<CommodityImportOutput> importExcel(@RequestParam(value = "excelFile") MultipartFile excelFile, HttpServletRequest request){
        return   commodityService.importExcel(excelFile,request);
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
