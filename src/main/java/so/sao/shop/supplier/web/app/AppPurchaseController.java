package so.sao.shop.supplier.web.app;

import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.FreightRules;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.output.AppPurchaseOutput;
import so.sao.shop.supplier.service.AccountService;
import so.sao.shop.supplier.service.FreightRulesService;
import so.sao.shop.supplier.service.app.AppPurchaseService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by acer on 2017/9/6.
 */
@RestController
@RequestMapping("/order")
public class AppPurchaseController {
    @Resource
    private AppPurchaseService appPurchaseService;

    @Resource
    private FreightRulesService freightRulesService;

    @Resource
    private AccountService accountService;

    @GetMapping(value = "/appOrderList")
    public Result appOrderList(Integer pageNum, Integer rows,String userId, String orderStatus) throws Exception{
        if (rows == null || rows <= 0) {
            rows = 5;
        }
        PageInfo<AppPurchaseOutput> appPurchasesVoList = appPurchaseService.findOrderList(pageNum, rows, userId,orderStatus);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS,appPurchasesVoList);
    }

    /**
     * 获取供应商当前使用的运费规则列表
     * @param accountId accountId

     * @return Result
     * @throws Exception Exception
     */
    @GetMapping("/freightQueryAll")
    public Result queryAll(@RequestParam("accountId") Long accountId ) throws Exception {
        //根据accountID查询商户当前正在使用的运费规则类型
        Integer rules = accountService.findRulesById(accountId);
        if (null == rules){
            return Result.fail("当前商户没有设置运费规则");
        }
        //根据运费规则类型查询运费规则列表
        List<FreightRules> list = freightRulesService.queryAll(accountId,0,0,rules);
        if (null == list || list.isEmpty()){
            return Result.fail(Constant.MessageConfig.MSG_NO_DATA);
        }else {
            //过滤掉匹配规则没有设置的
           for (int i = 0;i < list.size();i++){
                   if (null == list.get(i).getWhetherShipping()){
                       list.remove(i);
                   }
        }

    }
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, list);
    }


}
