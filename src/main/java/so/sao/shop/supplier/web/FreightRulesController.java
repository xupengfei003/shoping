package so.sao.shop.supplier.web;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.FreightRules;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.FreightRulesInput;
import so.sao.shop.supplier.service.AccountService;
import so.sao.shop.supplier.service.FreightRulesService;
import so.sao.shop.supplier.util.Ognl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gxy on 2017/9/18.
 */
@RestController
@RequestMapping("/freightrules")
public class FreightRulesController {
    @Autowired
    FreightRulesService freightRulesService;
    @Autowired
    AccountService accountService;

    /**
     * 分页获取供应商运费规则列表(返回汉字)
     * @param request request
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return Result
     * @throws Exception Exception
     */
    @GetMapping("/queryAll")
    public Result queryAll(HttpServletRequest request, Integer pageNum, Integer pageSize, Integer rulesType) throws Exception {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (Ognl.isNull(user)) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        List<FreightRules> dataList = freightRulesService.queryAll(user.getAccountId(), pageNum, pageSize,rulesType);
        Map<String,Object> map = new HashMap<>();
        map.put("data",new PageInfo<>(dataList));
        Integer rules = accountService.findRulesById(user.getAccountId());
        map.put("freightRules",rules);
        int isAll = freightRulesService.count(user.getAccountId());
        map.put("isAll",isAll);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, map);
    }

    /**
     * 获取单个运费规则
     * @param id id
     * @return Result
     * @throws Exception Exception
     */
    @GetMapping("/query/{id}")
    public Result query(@PathVariable Integer id) throws Exception{
        FreightRules freightRules = freightRulesService.query(id);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, freightRules);
    }

    /**
     * 更新某条运费规则
     * @param freightRulesInput 运费规则实体
     * @return Result
     * @throws Exception Exception
     */
    @PostMapping("/update/{id}")
    public Result update(HttpServletRequest request,@PathVariable Integer id, @RequestBody @Valid FreightRulesInput freightRulesInput) throws Exception {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (Ognl.isNull(user)) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        if (check(freightRulesInput)){
            return  freightRulesService.update(user.getAccountId(),id,freightRulesInput) == true ?  Result.success(Constant.MessageConfig.MSG_SUCCESS) :  Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }else {
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }

    }

    /**
     * 新增通用运费规则
     * @param freightRulesInput
     * @return
     */
    @PostMapping("/insert")
    public Result insert(HttpServletRequest request,@RequestBody @Valid FreightRulesInput freightRulesInput){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (Ognl.isNull(user)) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        if (check(freightRulesInput)) {
           return freightRulesService.insert(user.getAccountId(), freightRulesInput) == true ? Result.success(Constant.MessageConfig.MSG_SUCCESS) : Result.fail("通⽤物流费⽤规则只能添加⼀次!");
        }else {
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }
    }

    /**
     * 删除通用配送规则记录
     * @param id id
     * @return Result
     * @throws Exception Exception
     */
    @PostMapping("/delete/{id}")
    public Result delete(HttpServletRequest request,@PathVariable Integer id) throws Exception {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (Ognl.isNull(user)) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        return freightRulesService.deleteByPrimaryKey(id,user.getAccountId()) == true ? Result.success(Constant.MessageConfig.MSG_SUCCESS) : Result.fail(Constant.MessageConfig.MSG_FAILURE);

    }
    /**
     * 若配送方式为不包邮，检验入参合法性
     * @param freightRulesInput
     * @return
     */
    private boolean check(FreightRulesInput freightRulesInput){
        Integer defaultPiece = freightRulesInput.getDefaultPiece();
        BigDecimal defaultAmount = freightRulesInput.getDefaultAmount();
        Integer excessPiece = freightRulesInput.getExcessPiece();
        BigDecimal excessAmount = freightRulesInput.getExcessAmount();
        if (null != freightRulesInput){
            if (1 == freightRulesInput.getWhetherShipping()){//不包邮
                return Ognl.isEmpty(defaultPiece) && Ognl.isEmpty(defaultAmount) && Ognl.isEmpty(excessPiece) && Ognl.isEmpty(excessAmount) ? false : true;
            }
        }
        return true;
    }

    /**
     * 根据省市区code码查询配送规则
     *  1.若当前商户正在使用的配送规则为通用规则，直接返回通用规则
     *  2.若当前商户使用的是区域配送规则，根据省市区code码匹配配送规则
     * @param accountId
     * @param provinceCode
     * @param cityCode
     * @param districtCode
     * @return
     */
    @GetMapping("/getFreightRule")
    public Result getFreightRule(Long accountId,String provinceCode,String cityCode,String districtCode){
        /**
         * 1.根据accountID查询商户当前正在使用的运费规则类型
         * 2.根据运费规则类型查询列表
         *  ①.若当前商户正在使用的配送规则为通用规则，直接返回通用规则
         *  ②.若当前商户使用的是区域配送规则，根据省市区code码匹配配送规则
         */
        //根据accountID查询商户当前正在使用的运费规则类型
        Integer rules = accountService.findRulesById(accountId);
        if (null == rules){
            return Result.fail("当前商户没有设置运费规则");
        }
        List<FreightRules> list = freightRulesService.queryAll0(accountId,rules);
        if (null == list && list.isEmpty()){
            return Result.fail(Constant.MessageConfig.MSG_NO_DATA);
        }
        //通用规则
        if (0 == rules){
            return Result.success(Constant.MessageConfig.MSG_SUCCESS,list.get(0));
        }
        //配送规则
        if (1 == rules && null != list && !list.isEmpty()){
            //省市区匹配商家设置的匹配规则
            FreightRules freightRules = freightRulesService.matchAddress(provinceCode,cityCode,districtCode,list);
            if(freightRules == null){
                return Result.fail(Constant.MessageConfig.MSG_NO_DATA);
            }else{
                return Result.success(Constant.MessageConfig.MSG_SUCCESS,freightRules);
            }
        }
        return Result.fail(Constant.MessageConfig.MSG_FAILURE);
    }
}
