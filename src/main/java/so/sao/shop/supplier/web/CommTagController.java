package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommTagUpdateInput;
import so.sao.shop.supplier.service.CommTagService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 商品标签controller
 *
 * @author zhaoyan
 * @create 2017/8/14 17:50
 */
@RestController
@RequestMapping("/commTag")
@Api(description = "辅助设置-商品标签管理")
public class CommTagController {
    @Autowired
    private CommTagService commTagService;

    @PostMapping(value = "save")
    @ApiOperation(value = "新增商品标签", notes = "新增商品标签")
    public Result create(HttpServletRequest request, @RequestParam String name) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否非法登录
        if (null == user) {
            Result result = new Result();
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
            return result;
        }
        Long supplierId = user.getAccountId();
        //判断登录用户是否是管理员,登录用户是管理员时设置supplierId为0
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())) {
            supplierId = 0L;
        }
        return commTagService.saveCommTag(supplierId, name);
    }

    @DeleteMapping(value = "delete")
    @ApiOperation(value = "删除商品标签", notes = "根据商品标签ID删除商品标签")
    public Result delete(HttpServletRequest request, @RequestParam Long id) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否非法登录
        if (null == user) {
            Result result = new Result();
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
            return result;
        }
        Long supplierId = user.getAccountId();
        //判断登录用户是否是管理员,登录用户是管理员时设置supplierId为0
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            supplierId = 0L;
        }
        return commTagService.deleteCommTag(supplierId, id);
    }

    @PutMapping(value = "update")
    @ApiOperation(value = "修改商品标签", notes = "修改商品标签信息")
    public Result update(HttpServletRequest request, @Validated @RequestBody CommTagUpdateInput commTagUpdateInput, BindingResult bindingResult) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否非法登录
        if (null == user) {
            Result result = new Result();
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
            return result;
        }
        //校验商品标签修改对象的
        if (bindingResult.hasErrors()) {
            Result result = new Result();
            List<ObjectError> list = bindingResult.getAllErrors();
            for (ObjectError error : list) {
                result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                result.setMessage(error.getDefaultMessage());
            }
            return result;
        }
        Long supplierId = user.getAccountId();
        //判断登录用户是否非管理员,登录用户是管理员时设置supplierId为0
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            supplierId = 0L;
        }
        return commTagService.updateCommTag(supplierId, commTagUpdateInput);
    }

    @GetMapping(value = "search")
    @ApiOperation(value = "查询商品标签集合", notes = "查询供应商及公用的商品标签")
    public Result search(HttpServletRequest request) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否非法登录
        if (null == user) {
            Result result = new Result();
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
            return result;
        }
        Long supplierId = user.getAccountId();
        //判断登录用户是否是管理员,登录用户是管理员时设置supplierId为0
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            supplierId = 0L;
        }
        return commTagService.searchCommTag(supplierId);
    }
}
