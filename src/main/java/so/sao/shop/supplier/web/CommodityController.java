package so.sao.shop.supplier.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import so.sao.shop.supplier.config.StorageConfig;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URL;
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

    @ApiOperation(value="查询供应商商品信息集合", notes="根据参数返回符合条件的商品信息集合")
    @GetMapping(value="/search")
    public PageInfo search(HttpServletRequest request, @RequestParam(required = false) Long supplierId,@RequestParam(required = false) String commCode69,@RequestParam(required = false) Long commId,
                           @RequestParam(required = false) String suppCommCode,@RequestParam(required = false) String commName,
                           @RequestParam(required = false) Integer status,@RequestParam(required = false) Long typeId,@RequestParam(required = false) Double minPrice,
                           @RequestParam(required = false) Double maxPrice,@RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize){
        if(supplierId==null||supplierId==0){
            supplierId = commodityService.findAccountByUserId(((User) request.getAttribute(Constant.REQUEST_USER)).getId()).getAccountId();
        }
        return commodityService.searchCommodities(supplierId, commCode69, commId, suppCommCode, commName, status, typeId, minPrice, maxPrice, pageNum, pageSize);
    }

    @ApiOperation(value="查询所有商品信息集合", notes="根据参数返回符合条件的商品信息集合")
    @GetMapping(value="/searchAll")
    public PageInfo searchAll(@RequestParam(required = false) Long id, @RequestParam(required = false) String commName, @RequestParam(required = false) String code69,
                              @RequestParam(required = false) String suppCommCode, @RequestParam(required = false) Long typeId, @RequestParam(required = false) Double minPrice,
                              @RequestParam(required = false) Double maxPrice, @RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize){

        return commodityService.searchAllCommodities(id, commName, code69, suppCommCode,typeId, minPrice, maxPrice, pageNum, pageSize);
    }

    @ApiOperation(value="查询商品详情信息", notes="根据ID返回相应的商品信息")
    @GetMapping(value="/get/{id}")
    public CommodityOutput get(@PathVariable Long id){
        return commodityService.getCommodity(id);
    }

    @ApiOperation(value="新增商品信息", notes="")
    @PostMapping(value="/save")
    public BaseResult save(HttpServletRequest request,@Valid @RequestBody CommodityInput commodityInput,@RequestParam(required = false) Long accountId){
        return commodityService.saveCommodity(request,commodityInput,accountId);
    }

    @ApiOperation(value="修改商品信息", notes="")
    @PutMapping(value="/update")
    public BaseResult update(HttpServletRequest request,@Valid @RequestBody CommodityInput commodityInput,@RequestParam(required = false) Long accountId){
        return commodityService.updateCommodity(request,commodityInput,accountId);
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
    public  List<CommodityImportOutput> importExcel(@RequestParam(value = "excelFile") MultipartFile excelFile, HttpServletRequest request,@RequestParam(required = false) Long accountId ){
        return   commodityService.importExcel(excelFile,request,storageConfig,accountId);
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

    /**
     * 供应商信息模板下载
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @ApiOperation("商品信息模板下载")
    @GetMapping("/down")
    public void downLoadExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        URL save = Thread.currentThread().getContextClassLoader().getResource("");
        String str = save.toString()+"file/Commodity.xls";//Excel模板所在的路径。
        str = str.replaceAll("%20", " ");
        str = str.replaceAll("file:/", "");
        File f = new File(str);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String filename = "商品信息.xls";
        filename = new String(filename.getBytes("Utf-8"), "iso-8859-1");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/octet-stream");
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(f));
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }
}
