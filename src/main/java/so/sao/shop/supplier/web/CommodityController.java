package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.User;
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
    
    @ApiOperation(value="查询供应商商品信息集合（高级搜索）", notes="根据参数返回符合条件的商品信息集合（高级搜索）【责任人：唐文斌】")
    @GetMapping(value="/search")
    public Result search(HttpServletRequest request, CommSearchInput commSearchInput) throws Exception {
        return commodityService.searchCommodities(commSearchInput);
    }

    @ApiOperation(value="查询供应商商品信息集合（简单查询）", notes="根据参数返回符合条件的商品信息集合（简单查询）【责任人：唐文斌】")
    @GetMapping(value="/simplesearch")
    public Result simpleSearch(HttpServletRequest request, CommSimpleSearchInput commSimpleSearchInput) throws Exception {
        return commodityService.simpleSearchCommodities(commSimpleSearchInput,request);
    }

    @ApiOperation(value="查询商品详情信息", notes="根据ID返回相应的商品信息【责任人：唐文斌】")
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
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        boolean isAdmin = false;
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            isAdmin = true;
        }
        return commodityService.updateCommodity(commodityUpdateInput, supplierId, isAdmin);
    }

    @ApiOperation(value="上架商品", notes="【负责人：张瑞兵】")
    @PutMapping(value="/onShelves/{id}")
    public Result onShelves(HttpServletRequest request ,@PathVariable long id){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user == null){
            return Result.fail(so.sao.shop.supplier.config.Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        String isAdmin=user.getIsAdmin();
        return commodityService.onShelves(id,isAdmin);
    }

    @ApiOperation(value="批量商品上架", notes="【负责人：张瑞兵】")
    @PutMapping(value="/onShelves/batch")
    public Result onShelvesBatch(HttpServletRequest request, @RequestParam Long[] ids){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否非法登录
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        boolean isAdmin = false;
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())) {
            isAdmin = true;
        }
        return commodityService.onShelvesBatch(ids, isAdmin);
    }

    @ApiOperation(value="下架商品", notes="【负责人：张瑞兵】")
    @PutMapping(value="/offShelves/{id}")
    public Result offShelves(HttpServletRequest request, @PathVariable long id){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否非法登录
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        boolean isAdmin = false;
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())) {
            isAdmin = true;
        }
        return commodityService.offShelves(id,isAdmin);
    }

    @ApiOperation(value="批量商品下架", notes="【负责人：张瑞兵】")
    @PutMapping(value="/offShelves/batch")
    public Result offShelvesBatch(HttpServletRequest request, @RequestParam Long[] ids){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否非法登录
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        boolean isAdmin = false;
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())) {
            isAdmin = true;
        }
        return commodityService.offShelvesBatch(ids, isAdmin);
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
    public Result exportExcel(HttpServletRequest request , HttpServletResponse response , CommExportInput commExportInput)throws Exception {
        //校验供应商id
        commExportInput.setSupplierId(CheckUtil.supplierIdCheck(request,commExportInput.getSupplierId()));
        return  commodityService.exportExcel(request , response , commExportInput);
    }

    @ApiOperation(value="简单查询结果导出商品信息到Excel模板", notes="【责任人：张瑞兵】")
    @GetMapping(value="/simpleExportExcel")
    public Result simpleExportExcel(HttpServletRequest request , HttpServletResponse response , CommExportInput commExportInput)throws Exception {
        //校验供应商id
        commExportInput.setSupplierId(CheckUtil.supplierIdCheck(request,commExportInput.getSupplierId()));
        return  commodityService.exportExcel(request , response , commExportInput);
    }
    @ApiOperation(value="商品批量审核", notes="【责任人：【潘帅帅】")
    @PostMapping(value="/audit/bulk")
    public Result auditBatch(HttpServletRequest request , HttpServletResponse response , @Valid @RequestBody  CommAuditInput commAuditInput)throws Exception {
        //校验管理员
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if (!Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            return Result.fail("非管理员无操作权限！");
        }

        return  commodityService.auditBatch(request ,  commAuditInput);
    }
    @ApiOperation(value="查询商品审核列表", notes="【责任人：汪涛】")
    @GetMapping(value="/findApproval")
    public Result searchCommodityAudit(HttpServletRequest request, CommodityAuditInput commodityAuditInput){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user==null || !user.getIsAdmin().equals(Constant.ADMIN_STATUS)){
            return Result.fail(so.sao.shop.supplier.config.Constant.MessageConfig.ADMIN_AUTHORITY_EERO);
        }
        return commodityService.serachCommodityAudit(commodityAuditInput);
    }

    @ApiOperation(value="查询商品审核记录详情信息", notes="根据ID返回相应的审核记录详情信息【责任人：唐文斌】")
    @GetMapping(value="/getAudit/{id}")
    public Result getAuditDetail(@PathVariable Long id){
        return commodityService.findAuditDetail(id);
    }
}
