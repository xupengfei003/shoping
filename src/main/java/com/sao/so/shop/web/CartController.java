package com.sao.so.shop.web;

import com.github.pagehelper.PageInfo;
import com.sao.so.shop.config.Constant;
import com.sao.so.shop.domain.CartItem;
import com.sao.so.shop.pojo.BaseResult;
import com.sao.so.shop.pojo.input.CartItemInput;
import com.sao.so.shop.service.CartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by wyy on 2017/7/17.
 */
@RestController
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    private CartService cartService;



    /**
     * 根据购物车记录ID删除购物车记录
     * @param cartitemId
     * @return
     */
    @ApiOperation(value="根据购物车记录ID删除购物车记录",notes = "")
    @DeleteMapping(value="/cartitem/{cartitemid}")
    public BaseResult deleteCartItem(@PathVariable("cartitemid")Long cartitemId){
        boolean flag = cartService.deleteCartItemById(cartitemId);
        return convertBoolean(flag);
    }

    /**
     * 更新数量
     * @param cartitemId
     * @param number
     * @return
     */
    @ApiOperation(value="更新购物车商品数量",notes = "")
    @PutMapping(value="/cartitem/{cartitemid}")
    public BaseResult updateCartItem(@PathVariable("cartitemid") Long cartitemId,Integer number){
        boolean flag = cartService.updateCartItem(cartitemId,number);
       return convertBoolean(flag);
    }

    /**
     * 根据用户id获取用户购物车信息
     * @param userid
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value="根据用户id获取用户购物车信息",notes = "")
    @GetMapping(value ="/{userid}")
    public PageInfo<CartItem> getCartItems(@PathVariable("userid") Long userid, @RequestParam("pageNum")int pageNum, @RequestParam("pageSize")int pageSize){
        return  cartService.findCartItemByUserId(userid,pageNum,pageSize);
    }

    /**
     * 向购物车添加纪录
     * @param cartItemInput
     * @return
     */
    @ApiOperation(value="向购物车添加商品",notes = "")
    @PostMapping(value ="/cartitem")
    public BaseResult createCartItems(@RequestBody @Validated CartItemInput cartItemInput){
        boolean flag = cartService.saveCartItem(cartItemInput);
        return convertBoolean(flag);
    }

    /**
     * 成功失败的数据格式
     * @param flag
     * @return
     */
    private BaseResult convertBoolean(boolean flag){
        BaseResult result = new BaseResult();
        if(flag){
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
        }else{
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage(Constant.MessageConfig.MSG_FAILURE);
        }
        return result;
    }

}

