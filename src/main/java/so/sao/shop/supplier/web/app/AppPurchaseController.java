package so.sao.shop.supplier.web.app;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.FreightRules;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.output.AppPurchaseOutput;
import so.sao.shop.supplier.service.AccountService;
import so.sao.shop.supplier.service.FreightRulesService;
import so.sao.shop.supplier.service.app.AppPurchaseService;
import so.sao.shop.supplier.util.Ognl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by acer on 2017/9/6.
 */
@RestController
@RequestMapping("/order")
@Api(description = "App订单类-所有接口")
public class AppPurchaseController {
    @Resource
    private AppPurchaseService appPurchaseService;

    @Resource
    private FreightRulesService freightRulesService;

    @Resource
    private AccountService accountService;

    @GetMapping(value = "/appOrderList")
    @ApiOperation(value = "门店端获取订单列表", notes = "负责人【白治华】")
    public Result appOrderList(Integer pageNum, Integer rows,String userId, String orderStatus) throws Exception{
        if (rows == null || rows <= 0) {
            rows = 5;
        }
        PageInfo<AppPurchaseOutput> appPurchasesVoList = appPurchaseService.findOrderList(pageNum, rows, userId,orderStatus);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS,appPurchasesVoList);
    }

    /**
     * 分页获取供应商运费规则列表
     * @param accountId accountId

     * @return Result
     * @throws Exception Exception
     */
    @GetMapping("/freightQueryAll")
    @ApiOperation(value = "分页获取供应商运费规则列表", notes = "分页获取供应商运费规则列表 【负责人：郑振海】")
    public Result queryAll(Long accountId,Integer rulesType) throws Exception {

        List<FreightRules> dataList = freightRulesService.queryAll(accountId, 0, 0,rulesType);
        Map<String,Object> map = new HashMap<>();
        map.put("data",new PageInfo<>(dataList));
        Integer rules = accountService.findRulesById(accountId);
        map.put("freightRules",rules);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, map);
    }


}
