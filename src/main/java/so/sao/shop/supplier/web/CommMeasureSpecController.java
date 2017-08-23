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
import so.sao.shop.supplier.pojo.input.CommMeasureSpecUpdateInput;
import so.sao.shop.supplier.service.CommMeasureSpecService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by XuPengFei on 2017/8/14.
 */
@RestController
@RequestMapping("/commMeasureSpec")
@Api(description ="计量规格管理接口")
public class CommMeasureSpecController {

    @Autowired
    private CommMeasureSpecService commMeasureSpecService;

    /**
     * 查询计量规格，查询出自己的和管理员前期添加的公共的
     * @param request 请求对象
     * @return Result - List<CommMeasureSpec> 集合对象
     */
    @GetMapping(value = "/searchCommMeasureSpec")
    @ApiOperation(value = "查询计量规格", notes = "查询出自己的和管理员前期添加的公共的计量规格")
    public Result search(HttpServletRequest request){
        // 1. 先从请求对象中拿到供应商(用户)的ID
        User user = (User)request.getAttribute(Constant.REQUEST_USER);
        if (null == user) {   //验证用户是否登陆
            Result result = new Result();
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
            return result;
        }
        long supplierId = user.getAccountId();
        if(Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            supplierId = 0L;
        }
        // 2. 根据用户ID(supplierId),查询到自己和管理员添加的所有的计量规格
        return commMeasureSpecService.getCommMeasureSpe(supplierId);
    }

    /**
     * 新增计量规格
     * @param request 请求对象
     * @param name 计量规格名称
     * @return Result 结果对象
     */
    @PostMapping("/save")
    @ApiOperation(value = "新添加计量规格", notes ="新添加一个计量规格名称" )
    public Result create(HttpServletRequest request, String name){

        // 1. 先从请求对象中拿到供应商(用户)的ID
        User user = (User)request.getAttribute(Constant.REQUEST_USER);
        if (null == user) {   //验证用户是否登陆
            Result result = new Result();
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
            return result;
        }
        long supplierId = user.getAccountId();
        if(Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            supplierId = 0L;
        }
        // 2. 去Service层执行新增操作
        return  commMeasureSpecService.saveCommMeasureSpec(supplierId,name);
    }

    /**
     * 删除计量规格
     * @param id  计量规格ID
     *@param request 请求对象
     * @return Result 结果对象
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除计量规格", notes = "根据计量规格的ID - 删除计量规格")
    public Result delete(HttpServletRequest request, Long id){
        User user = (User)request.getAttribute(Constant.REQUEST_USER);
        if (null == user) {   //验证用户是否登陆
            Result result = new Result();
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
            return result;
        }
        long supplierId = user.getAccountId();
        if(Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            supplierId = 0L;
        }
        return commMeasureSpecService.deleteOneById(supplierId, id);
    }

    /**
     * 根据计量规格的CommMeasureSpecUpdateInput更新计量规格
     * @param request
     * @param commMeasureSpecUpdateInput 计量规格入参对象
     * @return Result结果对象
     */
    @PutMapping("/update")
    @ApiOperation(value = "更新计量规格" , notes = "根据计量规格的CommMeasureSpecUpdateInput更新计量规格")
    public Result update(HttpServletRequest request, @Validated @RequestBody CommMeasureSpecUpdateInput commMeasureSpecUpdateInput,  BindingResult bindingResult){
        User user = (User)request.getAttribute(Constant.REQUEST_USER);
        //验证用户是否登陆
        if (null == user) {
            Result result = new Result();
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
            return result;
        }
        //校验商品计量规格修改对象属性
        if(bindingResult.hasErrors()){
            Result result = new Result();
            List<ObjectError> objectErrors = bindingResult.getAllErrors();
            for (ObjectError error: objectErrors) {
                result.setCode(Constant.CodeConfig.CODE_FAILURE);
                result.setMessage(error.getDefaultMessage());
            }
            return result;
        }
        long supplierId = user.getAccountId();
        if(Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            supplierId = 0L;
        }
        return  commMeasureSpecService.update(supplierId,commMeasureSpecUpdateInput);
    }


}
