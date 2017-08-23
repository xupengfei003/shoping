package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommUnitUpdateInput;
import so.sao.shop.supplier.service.CommUnitService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Renzhiping on 2017/8/15.
 */
@RestController
@RequestMapping("/commUnit")
@Api(description = "商品单位管理接口")
public class CommUnitController {
@Autowired
   private CommUnitService commUnitService;

    /**
     * 查询商品单位集合（管理员与供应商自己的单位）
     * @param request
     * @return 商品单位集合
     */
    @ApiOperation(value = "查询商品单位集合",notes = "根据参数返回符合要求的结果集")
    @GetMapping(value="/search")
    public Result search(HttpServletRequest request ){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断user是否为空
        if(user == null) {
            Result result=new Result();
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage("请登录后再操作");
            return result;
        }
        Long supplierId = user.getAccountId();
        //判断登录用户是否是管理员,登录用户是管理员时设置supplierId为0
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            supplierId = 0L;
        }
        return commUnitService.searchCommUnit(supplierId);
    }


    /**
     * 商品单位添加
     * @param request
     * @param name 商品单位名称
     * @return 添加结果
     */
    @ApiOperation(value = "商品单位添加",notes = "添加商品单位信息")
    @PostMapping("/save")
    public Result save(HttpServletRequest request, String name ){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断user是否为空
        if(user == null) {
            Result result=new Result();
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage("请登录后再操作");
            return result;
        }

        Long supplierId = user.getAccountId();
        //判断登录用户是否是管理员,登录用户是管理员时设置supplierId为0
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            supplierId = 0L;
        }
        return commUnitService.saveCommUnit(supplierId,name);
   }

    /**
     * 商品单位删除
     * @param request
     * @param id 商品单位id
     * @return 删除结果
     */
    @ApiOperation(value = "商品单位删除" ,notes = "通过id删除商品单位")
    @DeleteMapping("/delete")
    public Result deleteById(HttpServletRequest request, Long id){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断user是否为空
        if(user == null) {
            Result result=new Result();
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage("请登录后再操作");
            return result;
        }
        Long supplierId = user.getAccountId();
        //判断登录用户是否是管理员,登录用户是管理员时设置supplierId为0
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            supplierId = 0L;
        }
        return commUnitService.deleteCommUnit(supplierId,id);
    }

    /**
     * 商品单位修改
     * @param request
     * @param commUnitUpdateInput 商品单位入参
     * @return 修改结果
     */
    @PutMapping("/update")
    @ApiOperation(value = "更新商品单位" , notes = "根据商品单位id修改商品单位")
    public Result update(HttpServletRequest request, @Validated @RequestBody CommUnitUpdateInput commUnitUpdateInput){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断user是否为空
        if(user == null) {
            Result result=new Result();
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage("请登录后再操作");
            return result;
        }
        Long supplierId = user.getAccountId();
        //判断登录用户是否非管理员,登录用户是管理员时设置supplierId为0
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            supplierId = 0L;
        }
        return commUnitService.updateCommunit(supplierId,commUnitUpdateInput);
    }

}
