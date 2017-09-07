package so.sao.shop.supplier.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.CartItem;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CartItemInput;
import so.sao.shop.supplier.service.CartService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by wyy on 2017/7/17.
 */
@RestController
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;


    /**
     * 根据购物车记录ID删除购物车记录
     * @param cartitemId
     * @return
     */
    @ApiOperation(value="根据购物车记录ID删除购物车记录",notes = "根据购物车记录ID删除购物车记录【负责人：王翼云】")
    @DeleteMapping(value="/cartitem/{cartitemid}")
    public Result deleteCartItem(@PathVariable("cartitemid")Long cartitemId){
        return convertBoolean(cartService.deleteCartItemById(cartitemId));
    }
    /**
     * 更新数量
     * @param cartitemId
     * @param number
     * @return
     */
    @ApiOperation(value="更新购物车商品数量",notes = "更新购物车商品数量【负责人：王翼云】")
    @PutMapping(value="/cartitem/{cartitemid}")
    public Result updateCartItem(@PathVariable("cartitemid") Long cartitemId,
                                 @NotNull(message = "物品i都不能为空") Long commodityId,
                                 @NotNull(message = "更新数量不能为空")
                                 @Min(value = 1,message = "更新数量必须大于1") Integer number){
        if(!checkUser()){
            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
        }

        Integer remaining = cartService.updateCartItem(cartitemId,commodityId,number);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS,remaining);
    }

    /**
     * 根据用户id获取用户购物车信息
     * @param userid
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value="根据用户id获取用户购物车信息",notes = "根据用户id获取用户购物车信息【负责人：王翼云】")
    @GetMapping(value ="/{userid}")
    public Result getCartItems(@PathVariable("userid") Long userid, @RequestParam("pageNum")int pageNum, @RequestParam("pageSize")int pageSize){
        if(!checkUser()){
            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
        }
        PageInfo<CartItem> pageInfo = cartService.findCartItemByUserId(userid,pageNum,pageSize);

        return Result.success(Constant.MessageConfig.MSG_SUCCESS,pageInfo);
    }

    /**
     * 向购物车添加纪录
     * @param cartItemInput
     * @return
     */
    @ApiOperation(value="向购物车添加商品",notes = "向购物车添加商品【负责人：王翼云】")
    @PostMapping(value ="/cartitem")
    public Result createCartItems(@RequestBody @Validated CartItemInput cartItemInput){
        if(!checkUser()){
            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
        }
        return convertBoolean(cartService.saveCartItem(cartItemInput));
    }

    /**
     * 判断用户是否登陆
     * @return
     */
    private boolean checkUser() {
        User user = (User)request.getAttribute(Constant.REQUEST_USER);
        if(user == null){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 成功失败的数据格式
     * @param flag
     * @return
     */
    private Result convertBoolean(boolean flag){
        Result result = null;
        if(flag){
            Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }else{
            Result.success(Constant.MessageConfig.MSG_FAILURE);
        }
        return result;
    }

}

