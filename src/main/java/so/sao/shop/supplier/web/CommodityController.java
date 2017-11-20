package so.sao.shop.supplier.web;

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
public class CommodityController {

    @Autowired
    private CommodityService commodityService;
    
    @GetMapping(value="/search")
    public Result search(HttpServletRequest request, CommSearchInput commSearchInput) throws Exception {
        return commodityService.searchCommodities(commSearchInput,request);
    }

    @GetMapping(value="/simplesearch")
    public Result simpleSearch(HttpServletRequest request, CommSimpleSearchInput commSimpleSearchInput) throws Exception {
        return commodityService.simpleSearchCommodities(commSimpleSearchInput,request);
    }

    @GetMapping(value="/get/{id}")
    public Result get(@PathVariable Long id){
        return commodityService.getCommodity(id);
    }

    @GetMapping(value = "/findByCode69/{code69}")
    public Result find(@PathVariable String code69) {
        return commodityService.findCommodity(code69);
    }

    @PostMapping(value="/save")
    public Result save(HttpServletRequest request,@Valid @RequestBody CommodityInput commodityInput,@RequestParam(required = false) Long supplierId) throws Exception {
        //校验供应商ID
        supplierId = CheckUtil.supplierIdCheck(request,supplierId);
        return commodityService.saveCommodity(commodityInput, supplierId);
    }

    @PutMapping(value="/update")
    public Result update(HttpServletRequest request, @Valid @RequestBody CommodityUpdateInput commodityUpdateInput, @RequestParam(required = false) Long supplierId) throws Exception {
        //校验供应商ID
        supplierId = CheckUtil.supplierIdCheck(request,supplierId);
        return commodityService.updateCommodity(commodityUpdateInput, supplierId);
    }

    @PutMapping(value="/onShelves/{id}")
    public Result onShelves(HttpServletRequest request ,@PathVariable long id){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user == null){
            return Result.fail(so.sao.shop.supplier.config.Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        return commodityService.onShelves(id);
    }

    @PutMapping(value="/onShelves/batch")
    public Result onShelvesBatch(HttpServletRequest request, @RequestParam Long[] ids){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否非法登录
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        Long supplierId = user.getAccountId();
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())) {
            //管理员批量上架
            return commodityService.onShelvesBatchByAdmin(ids);
        }
        //供应商批量上架
        return commodityService.onShelvesBatchBySupplier(ids, supplierId);
    }

    @PutMapping(value="/offShelves/{id}")
    public Result offShelves(HttpServletRequest request, @PathVariable long id){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否非法登录
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        return commodityService.offShelves(id);
    }

    @PutMapping(value="/offShelves/batch")
    public Result offShelvesBatch(HttpServletRequest request, @RequestParam Long[] ids){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否非法登录
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        return commodityService.offShelvesBatch(ids);
    }

    @DeleteMapping(value="/delete/{id}")
    public Result delete(@PathVariable Long id){
        return commodityService.deleteCommodity(id);
    }

    @DeleteMapping(value="/delete/bulk")
    public Result delete(@RequestParam Long[] id){
        return commodityService.deleteCommodities(id);
    }

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

    @GetMapping(value="/exportExcel")
    public Result exportExcel(HttpServletRequest request , HttpServletResponse response , CommExportInput commExportInput)throws Exception {
        //校验供应商id
        commExportInput.setSupplierId(CheckUtil.supplierIdCheck(request,commExportInput.getSupplierId()));
        return  commodityService.exportExcel(request , response , commExportInput);
    }

    @GetMapping(value="/simpleExportExcel")
    public Result simpleExportExcel(HttpServletRequest request , HttpServletResponse response , CommExportInput commExportInput)throws Exception {
        //校验供应商id
        commExportInput.setSupplierId(CheckUtil.supplierIdCheck(request,commExportInput.getSupplierId()));
        return  commodityService.exportExcel(request , response , commExportInput);
    }
    @PostMapping(value="/audit/bulk")
    public Result auditBatch(HttpServletRequest request , HttpServletResponse response , @Valid @RequestBody  CommAuditInput commAuditInput)throws Exception {
        //校验管理员
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if (!Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            return Result.fail("非管理员无操作权限！");
        }

        return  commodityService.auditBatch(request ,  commAuditInput);
    }
    @GetMapping(value="/findApproval")
    public Result searchCommodityAudit(HttpServletRequest request, CommodityAuditInput commodityAuditInput){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user==null || !user.getIsAdmin().equals(Constant.ADMIN_STATUS)){
            return Result.fail(so.sao.shop.supplier.config.Constant.MessageConfig.ADMIN_AUTHORITY_EERO);
        }
        return commodityService.serachCommodityAudit(commodityAuditInput);
    }

    @GetMapping(value="/getAudit/{id}")
    public Result getAuditDetail(@PathVariable Long id){
        return commodityService.findAuditDetail(id);
    }

    @GetMapping(value="/countDetail")
    public Result countCommDetail(HttpServletRequest request){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否非法登录
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        Long supplierId = user.getAccountId();
        return commodityService.countCommDetail(supplierId);
    }
}
