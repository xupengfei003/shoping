package so.sao.shop.supplier.web.app;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.AppCartItem;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.app.AppCartService;
import so.sao.shop.supplier.util.Ognl;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Map;

/**
 * Created by fangzhou on 2017/9/15.
 */
@RestController
@RequestMapping(value = "/cart")
@Api(description = "购物车-所有接口【方洲】")
public class AppCartController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppCartService cartService;

    /**
     * 根据购物车记录ID删除购物车记录
     * @param cartitemId
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value="根据购物车ID删除购物车记录",notes = "根据购物车ID删除购物车记录")
    @GetMapping(value="/cartitem/d/{cartitemId}")
    public Result deleteCartItem(@PathVariable("cartitemId")Long cartitemId,
                                 @NotNull(message = "用户ID不能为空") Long userId) throws Exception{
        // 删除购物车记录
        Boolean flag = cartService.deleteCartItemById(cartitemId,userId);
        // 返回结果
        if(flag){
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }else{
            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
        }
    }

    /**
     * 更新购物车商品数量
     * @param cartitemId
     * @param number
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value="更新购物车商品数量",notes = "更新购物车商品数量")
    @PostMapping(value="/cartitem/u/{cartitemId}")
    public Result updateCartItem(@PathVariable("cartitemId") Long cartitemId,
                                 @NotNull(message = "更新数量不能为空")
                                 @Pattern(regexp = "^[1-9][0-9]*$", message = "更新数量有误") Integer number,
                                 @NotNull(message = "用户ID不能为空") Long userId ) throws Exception{
        // 更新数据
        Map<String,Object> map = cartService.updateCartItem(cartitemId,number,userId);
        // 获取提示信息
        String msg = (String)map.get("msg");
        // 获取信息码
        String code = (String)map.get("code");
        if ("0".equals(code)){
            return Result.fail(msg);
        }else{
            return Result.success(msg);
        }
    }

    /**
     * 向购物车添加购物记录
     * @param number
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value="向购物车添加购物记录",notes = "向购物车添加购物记录")
    @PostMapping(value ="/cartitem")
    public Result createCartItems(@NotNull(message = "商品ID不能为空") Long commodityId,
                                  @NotNull(message = "购物车添加商品数量不能为空")
                                  @Pattern(regexp = "^[1-9][0-9]*$", message = "购物车添加商品数量有误") Integer number,
                                  @NotNull(message = "用户ID不能为空") Long userId ) throws Exception{
        // 插入数据
        Map<String,Object> map = cartService.saveCartItem(commodityId,number,userId);
        // 获取更新后的数据
        AppCartItem appCartItem = (AppCartItem)map.get("appCartItem");
        // 获取提示信息
        String msg = (String)map.get("msg");
        if (Ognl.isNull(appCartItem)){
            return Result.fail(msg);
        }else{
            return Result.success(msg);
        }
    }


//    /**
//     * 根据用户id获取用户购物车信息
//     * @param userid
//     * @return
//     */
//    @ApiOperation(value="根据用户id获取用户购物车信息",notes = "根据用户id获取用户购物车信息【负责人：王翼云】")
//    @GetMapping(value ="/{userid}")
//    public Result getCartItemsByUser(@PathVariable("userid") Long userid){
//        logger.debug(" [userid] " + userid);
//
//        User user = (User)request.getAttribute(Constant.REQUEST_USER);
//        logger.debug("【当前用户id为】："+user+"  ;  【传入的用户ID为】："+userid);
//        if(!checkUser()){
//            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
//        }
//        if(!user.getId().equals(userid)){//如果传入的用户ID与登陆ID不同
//            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
//        }
//
//        List<AppCartItemOut> appCartItemOuts = cartService.findCartItemsByUserId(user.getId());
////        logger.debug("【购物车信息】 "+appCartItemOut.toString());
//        return Result.success(Constant.MessageConfig.MSG_SUCCESS,appCartItemOuts);
//    }
//
//    /**
//     * 根据用户id获取用户购物车信息，分页
//     * @param userid
//     * @param pageNum
//     * @param pageSize
//     * @return
//     */
//    public Result getCartItems(@PathVariable("userid") Long userid,
//                               @RequestParam("pageNum")
//                               @Min(value=1)
//                               @NotNull int pageNum,
//                               @RequestParam("pageSize")
//                               @Min(value=1)
//                               @NotNull int pageSize){
//        logger.debug(" [userid] " + userid);
//        logger.debug(" [pageNum] " + pageNum);
//        logger.debug(" [pageSize] " + pageSize);
//        User user = (User)request.getAttribute(Constant.REQUEST_USER);
//        logger.debug("【当前用户id为】："+user+"  ;  【传入的用户ID为】："+userid);
//        if(!checkUser()){
//            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
//        }
//        if(!user.getId().equals(userid)){//如果传入的用户ID与登陆ID不同
//            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
//        }
//        /**
//         *注意这里的数据格式 ==> PageInfo<AppCartItem>
//         */
//        PageInfo<AppCartItem> pageInfo = cartService.findCartItemByUserId(user.getId(),pageNum,pageSize);
//        logger.debug("【购物车信息】 "+pageInfo.toString());
//        return Result.success(Constant.MessageConfig.MSG_SUCCESS,pageInfo);
//    }
//
//    /**
//     * 向购物车添加纪录
//     * @param appCartItemInput
//     * @return
//     */
//    @ApiOperation(value="向购物车添加商品",notes = "向购物车添加商品【负责人：王翼云】")
//    @PostMapping(value ="/cartitem")
//    public Result createCartItems(@RequestBody @Validated AppCartItemInput appCartItemInput){
//
//        logger.debug("【传入的参数信息为】 "+ appCartItemInput.toString());
//        User user = (User)request.getAttribute(Constant.REQUEST_USER);
//        if(!checkUser()){
//            logger.debug("【user info 1】 "+ user);
//            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
//        }
//
//        appCartItemInput.setUserId(user.getId());
//        logger.debug("【user info 2】 "+ user);
//        return convertBoolean(cartService.saveCartItem(appCartItemInput));
//    }
//
//    /**
//     * 判断用户是否登陆
//     * @return
//     */
//    private boolean checkUser() {
//        User user = (User)request.getAttribute(Constant.REQUEST_USER);
//        if(user == null){
//            return false;
//        }else{
//            return true;
//        }
//    }
//
//    /**
//     * 成功失败的数据格式
//     * @param flag
//     * @return
//     */
//    private Result convertBoolean(boolean flag){
//        Result result = null;
//        if(flag){
//            result = Result.success(Constant.MessageConfig.MSG_SUCCESS);
//        }else{
//            result = Result.fail(Constant.MessageConfig.MSG_FAILURE);
//        }
//        return result;
//    }

}

