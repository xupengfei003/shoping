package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.QualificationService;

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
     * 资质审核 - 更新资质状态和时间
     * @param accountId
     * @param qualificationStatus
     * @return Result
     */
    @ApiOperation(value = "资质审核", notes = "更新资质状态和时间【负责人：许鹏飞】")
    @PostMapping("/updateQualificationStatus")
    public Result updateQualificationStatus(@RequestParam("accountId") Integer accountId, @RequestParam("qualificationStatus") Integer qualificationStatus ){
        Result result = qualificationService.updateQualificationStatus( accountId, qualificationStatus );
        return result;
    }

    /**
     * 查询登录供应商的资质状态
     * @param accountId
     * @return Result
     */
    @ApiOperation(value = "查询登录供应商的资质状态", notes = "查询登录供应商的资质状态【负责人：许鹏飞】")
    @GetMapping("getAccountQualificationStatus")
    public Result getAccountQualificationStatus(@RequestParam("accountId") Integer accountId){
        Result result = qualificationService.getAccountQualificationStatus( accountId );
        return result;
    }


}
