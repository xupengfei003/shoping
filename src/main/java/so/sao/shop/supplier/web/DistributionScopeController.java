package so.sao.shop.supplier.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.DistributionScope;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.DistributionScopeInput;
import so.sao.shop.supplier.service.DistributionScopeService;
import so.sao.shop.supplier.util.Ognl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author gxy on 2017/9/18.
 */
@RestController
@RequestMapping("/distributionscope")
@Api(description = "配送范围-所有接口【负责人：郑振海】")
public class DistributionScopeController {
    @Autowired
    DistributionScopeService distributionScopeService;

    /**
     * 增加配送范围
     * @param request request
     * @param distributionScopeInput 配送范围入参信息
     * @return Result
     * @throws Exception Exception
     */
    @PostMapping("/create")
    @ApiOperation(value = "创建配送范围", notes = "创建配送范围 【负责人：郑振海】")
    public Result create(HttpServletRequest request, @RequestBody @Valid DistributionScopeInput distributionScopeInput) throws Exception {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (Ognl.isNull(user)) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        //增加配送范围
        boolean isCreate = distributionScopeService.createDistributionScope(user.getAccountId(), distributionScopeInput);
        if (!isCreate){
            return Result.fail("该配送范围已存在或添加失败!");
        }
        return Result.success(Constant.MessageConfig.MSG_SUCCESS);
    }

    /**
     * 分页获取供应商配送范围列表
     * @param request request
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return Result
     * @throws Exception Exception
     */
    @GetMapping("/queryAll")
    @ApiOperation(value = "分页获取供应商配送范围列表", notes = "分页获取供应商配送范围列表 【负责人：郑振海】")
    public Result queryAll(HttpServletRequest request, Integer pageNum, Integer pageSize) throws Exception {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (Ognl.isNull(user)) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        List<DistributionScope> dataList = distributionScopeService.queryAll(user.getAccountId(), pageNum, pageSize);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, new PageInfo<>(dataList));
    }

    /**
     * 获取单个配送范围信息
     * @param id id
     * @return Result
     * @throws Exception Exception
     */
    @GetMapping("/query/{id}")
    @ApiOperation(value = "获取单个配送范围信息", notes = "获取单个配送范围信息")
    public Result query(@PathVariable Integer id) throws Exception{
        DistributionScope distributionScope = distributionScopeService.query(id);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, distributionScope);
    }

    /**
     * 更新某条配送范围信息
     * @param distributionScopeInput 配送范围实体
     * @return Result
     * @throws Exception Exception
     */
    @PostMapping("/update/{id}")
    @ApiOperation(value = "更新某条配送范围信息", notes = "更新某条配送范围信息")
    public Result update(@PathVariable Integer id, @RequestBody @Valid DistributionScopeInput distributionScopeInput) throws Exception {
        distributionScopeService.update(id,distributionScopeInput);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS);
    }

    /**
     * 删除某条记录
     * @param id id
     * @return Result
     * @throws Exception Exception
     */
    @PostMapping("/delete/{id}")
    @ApiOperation(value = "删除某条记录", notes = "删除某条记录")
    public Result delete(HttpServletRequest request,@PathVariable Integer id) throws Exception {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (Ognl.isNull(user)) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        return distributionScopeService.delete(id,user.getAccountId()) == true ? Result.success(Constant.MessageConfig.MSG_SUCCESS) : Result.fail("配送规则正在使用中!");
    }
}
