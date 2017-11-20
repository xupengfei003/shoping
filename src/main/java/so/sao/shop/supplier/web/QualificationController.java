package so.sao.shop.supplier.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.QualificationInput;
import so.sao.shop.supplier.pojo.input.QualificationSaveInput;
import so.sao.shop.supplier.service.QualificationService;
import so.sao.shop.supplier.util.CheckUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 供应商资质Controller
 */
@RestController
@RequestMapping("/account/qualification")
public class QualificationController {

    @Autowired
    private QualificationService qualificationService;

    /**
     * 资质审核 - 更新资质状态和时间,拒绝原因
     * @param accountId
     * @param qualificationStatus
     * @param reason
     * @return Result
     */
    @PutMapping("/updateQualificationStatus")
    public Result updateQualificationStatus(@RequestParam(value = "accountId" ) Long accountId,
                                            @RequestParam(value = "qualificationStatus") Integer qualificationStatus,
                                            @RequestParam(value = "reason", required = false ) String reason){
        Result result = qualificationService.updateQualificationStatus( accountId, qualificationStatus, reason );
        return result;
    }

    /**
     * 查询登录供应商的资质状态
     * @return Result
     */
    @GetMapping("getAccountQualificationStatus")
    public Result getAccountQualificationStatus( HttpServletRequest request){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user==null){
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        Long accountId = user.getAccountId();
        Result result = qualificationService.getAccountQualificationStatus( accountId );
        return result;
    }

    /**
     * 根据供应商id查询资质信息
     * @param request
     * @param accountId
     * @return
     */
    @GetMapping("/getByAccountId")
    public Result getQualification(HttpServletRequest request, @RequestParam Long accountId){
        return qualificationService.findByAccountId(accountId);
    }

    /**
     * 根据条件查询资质列表
     * @param request
     * @param qualificationInput 查询条件
     * @return 返回查询结果
     */
    @GetMapping("/search")
    public Result search(HttpServletRequest request,@Validated QualificationInput qualificationInput){
        return qualificationService.searchQualifications(qualificationInput);
    }

    /**
     * 供应商资质文件上传
     * @param multipartFile 供应商资质文件
     * @return
     */
    @PostMapping(value = "/upload")
    public Result uploadQualificationFile(@RequestPart("file") MultipartFile multipartFile) {

        return qualificationService.uploadQualificationFile(multipartFile);
    }

    /**
     * 添加供应商资质图片
     * @param qualificationInput 资质入参
     * @return
     */
    @PostMapping(value = "/save")
    public Result saveQualification(HttpServletRequest request, @RequestBody QualificationSaveInput qualificationInput) throws Exception{
        //供应商ID校验
        qualificationInput.setAccountId(CheckUtil.supplierIdCheck(request,qualificationInput.getAccountId()));
        return qualificationService.saveQualification(qualificationInput);
    }

    /**
     * 供应商资质删除
     * @param accountID  供应商ID
     * @return
     */
    @PutMapping(value = "/delete/{accountID}")
    public Result deleteQualification(HttpServletRequest request,@PathVariable Long accountID) throws Exception{
        //供应商ID校验
        accountID = CheckUtil.supplierIdCheck(request,accountID);
        return qualificationService.deleteQualification(accountID);
    }

    /**
     * 判断资质消息是否已读, 更新资质状态消息已读状态
     * @param accountId
     * @return result
     */
    @GetMapping("/updateQualificationMessageRead")
    public Result updateQualificationMessageRead(@RequestParam(value = "accountId" ) Long accountId ){
        Result result = qualificationService.updateQualificationMessageRead( accountId );
        return result;
    }


}
