package so.sao.shop.supplier.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommUnitUpdateInput;
import so.sao.shop.supplier.service.CommUnitService;
import so.sao.shop.supplier.util.Ognl;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Renzhiping on 2017/8/15.
 */
@RestController
@RequestMapping("/commUnit")
public class CommUnitController {
@Autowired
   private CommUnitService commUnitService;

    /**
     * 查询库存单位集合（管理员与供应商自己的单位）
     * @param request
     * @return 库存单位集合
     */
    @GetMapping(value="/search")
    public Result search(HttpServletRequest request,Long supplierId ){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断user是否为空
       if(user == null ) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        //判断登录用户是否是供应商（供应商登陆时，supplierId从request中取，第二个参数supplierId前台传回0）
        if (null==supplierId || supplierId.equals(0L)) {
            supplierId = user.getAccountId();
        }
        return commUnitService.searchCommUnit(supplierId);
    }


    /**
     * 商品库存单位添加
     * @param request
     * @param name 库存单位名称
     * @return 添加结果
     */
    @PostMapping("/save")
    public Result create(HttpServletRequest request, @RequestParam(required=false) String name ){
        //校验输入name是否为空
        if( null == name || Ognl.isEmpty(name.trim())){
            return Result.fail("库存单位不能为空！");
        }else {
            name = name.trim();
            if (name.length()> Constant.CheckMaxLength.MAX_UNIT_NAME_LENGTH){
                return Result.fail("库存单位不能超过"+ Constant.CheckMaxLength.MAX_UNIT_NAME_LENGTH+"位！");
            }
        }
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断user是否为空
       if(user == null ) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }

        Long supplierId = user.getAccountId();
        //判断登录用户是否是管理员,登录用户是管理员时设置supplierId为0
       if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            supplierId = 0L;
        }
        return commUnitService.saveCommUnit(supplierId,name);
   }

    /**
     * 库存单位删除
     * @param request
     * @param id 库存单位id
     * @return 删除结果
     */
    @DeleteMapping("/delete")
    public Result delete(HttpServletRequest request, @RequestParam Long id){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断user是否为空
        if(user == null ) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        Long supplierId = user.getAccountId();
        //判断登录用户是否是管理员,登录用户是管理员时设置supplierId为0
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            supplierId = 0L;
        }
        return commUnitService.deleteCommUnit(supplierId,id);
    }

    /**
     * 库存单位修改
     * @param request
     * @param commUnitUpdateInput 库存单位入参
     * @return 修改结果
     */
    @PutMapping("/update")
    public Result update(HttpServletRequest request, @Validated @RequestBody CommUnitUpdateInput commUnitUpdateInput) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断user是否为空
       if(user == null) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        Long supplierId = user.getAccountId();
        //判断登录用户是否非管理员,登录用户是管理员时设置supplierId为0
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            supplierId = 0L;
        }
        return commUnitService.updateCommunit(supplierId,commUnitUpdateInput);
    }

}
