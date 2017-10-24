package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommCartonUpdateInput;
import so.sao.shop.supplier.service.CommCartonService;
import so.sao.shop.supplier.util.Ognl;

import javax.servlet.http.HttpServletRequest;

/**
 * 商品箱规单位controller
 * @author chensha
 */
@RestController
@RequestMapping("/commCarton")
@Api(description = "辅助设置-箱规单位管理【责任人：陈沙】")
public class CommCartonController {
    @Autowired
    private CommCartonService commCartonService;

    @PostMapping(value = "/create")
    @ApiOperation(value = "新增箱规单位", notes = "新增箱规单位")
    public Result create(HttpServletRequest request, @RequestParam String name) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登录,如果未登录，提示请先登录
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        Long supplierId = user.getAccountId();
        //判断登录用户是否是管理员（admin_status=1),登录用户是管理员时设置supplierId为0
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())) {
            supplierId = 0L;
        }
        //判断箱规单位是否为空
        if (null == name || Ognl.isEmpty(name.trim())) {
            return Result.fail("箱规单位不能为空！");
        }
        //校验箱规单位名称长度(64位）
        if (name.trim().length() > Constant.CheckMaxLength.MAX_TAG_NAME_LENGTH) {
            return Result.fail("箱规单位不能超过" + Constant.CheckMaxLength.MAX_TAG_NAME_LENGTH + "位！");
        }
        return commCartonService.saveCommCarton(supplierId, name);
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "删除箱规单位", notes = "根据ID删除箱规单位")
    public Result delete(HttpServletRequest request, @RequestParam Long id) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否非法登录
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        Long supplierId = user.getAccountId();
        //判断登录用户是否是管理员,登录用户是管理员时设置supplierId为0
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())) {
            supplierId = 0L;
        }
        return commCartonService.deleteCommCarton(supplierId, id);
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "修改箱规单位", notes = "修改箱规单位信息")
    public Result update(HttpServletRequest request, @Validated @RequestBody CommCartonUpdateInput commCartonUpdateInput) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否非法登录
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        Long supplierId = user.getAccountId();
        //判断登录用户是否非管理员,登录用户是管理员时设置supplierId为0
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())) {
            supplierId = 0L;
        }
        return commCartonService.updateCommTag(supplierId, commCartonUpdateInput);
    }

    @GetMapping(value = "/search")
    @ApiOperation(value = "查询箱规单位集合", notes = "查询供应商及公用的箱规单位")
    public Result search(HttpServletRequest request, Long supplierId) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否非法登录
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        //判断登录用户是否是供应商（供应商登陆时，supplierId从request中取，第二个参数supplierId前台传回0）
        if (supplierId.equals(0L)) {
            supplierId = user.getAccountId();
        }
        return commCartonService.searchCommCartons(supplierId);
    }
}
