package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.QualificationInput;
import so.sao.shop.supplier.service.QualificationService;
import so.sao.shop.supplier.util.Ognl;

import javax.servlet.http.HttpServletRequest;

/**
 * 供应商资质Controller
 */
@RestController
@RequestMapping("/account/qualification")
@Api(description = "供应商资质相关接口")
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
    @ApiOperation(value = "资质审核", notes = "更新资质状态,时间,拒绝原因【负责人：许鹏飞】")
    @PutMapping("/updateQualificationStatus")
    public Result updateQualificationStatus(@RequestParam(value = "accountId" ) Integer accountId,
                                            @RequestParam(value = "qualificationStatus") Integer qualificationStatus,
                                            @RequestParam(value = "reason", required = false ) String reason){
        Result result = qualificationService.updateQualificationStatus( accountId, qualificationStatus, reason );
        return result;
    }

    /**
     * 查询登录供应商的资质状态
     * @return Result
     */
    @ApiOperation(value = "查询登录供应商的资质状态", notes = "查询登录供应商的资质状态【负责人：许鹏飞】")
    @GetMapping("getAccountQualificationStatus")
    public Result getAccountQualificationStatus( HttpServletRequest request){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user==null || user.getIsAdmin().equals(Constant.ADMIN_STATUS)){
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
    @ApiOperation(value = "根据供应商id查询资质详情",notes = "根据供应商id查询资质详情【负责人：张腾飞】")
    @GetMapping("/getByAccountId")
    public Result getQualification(HttpServletRequest request, @RequestParam Long accountId){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if (null == user || Ognl.isEmpty(user.getAccountId())) {   //验证用户是否登陆
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        return qualificationService.findByAccountId(accountId);
    }

    /**
     * 根据条件查询资质列表
     * @param request
     * @param qualificationInput 查询条件
     * @return 返回查询结果
     */
    @ApiOperation(value = "查询资质列表",notes = "查询资质列表【负责人：张腾飞】")
    @GetMapping("/search")
    public Result search(HttpServletRequest request,QualificationInput qualificationInput){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if (null == user || Ognl.isEmpty(user.getAccountId())) {   //验证用户是否登陆
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        return qualificationService.searchQualifications(qualificationInput);
    }

}
