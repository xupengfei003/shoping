package so.sao.shop.supplier.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.AppCartItem;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.AppCartItemInput;
import so.sao.shop.supplier.pojo.output.AppCartItemOut;
import so.sao.shop.supplier.service.AppCartService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by wyy on 2017/7/17.
 */
@RestController
@RequestMapping(value = "/cart")
public class AppCartController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppCartService cartService;

    @Autowired
    private HttpServletRequest request;


    /**
     * 根据购物车记录ID删除购物车记录
     * @param cartitemId
     * @return
     */
    @ApiOperation(value="根据购物车记录ID删除购物车记录",notes = "根据购物车记录ID删除购物车记录【负责人：王翼云】")
    @GetMapping(value="/cartitem/d/{cartitemid}")
    public Result deleteCartItem(@PathVariable("cartitemid")Long cartitemId){
        if(!checkUser()){
            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
        }
        return convertBoolean(cartService.deleteCartItemById(cartitemId));
    }
    /**
     * 更新数量
     * @param cartitemId
     * @param number
     * @return
     */
    @ApiOperation(value="更新购物车商品数量",notes = "更新购物车商品数量【负责人：王翼云】")
    @PostMapping(value="/cartitem/u/{cartitemid}")
    public Result updateCartItem(@PathVariable("cartitemid") Long cartitemId,
                                 @RequestParam("commodityId")
                                 @NotNull(message = "物品id不能为空") Long commodityId,
                                 @NotNull(message = "更新数量不能为空")
                                 @RequestParam("number")
                                 @Min(value = 1,message = "更新数量必须大于1") Integer number){
        logger.debug(" [cartitemId] " + cartitemId);
        logger.debug(" [commodityId] " + commodityId);
        logger.debug(" [number] " + number);

        if(!checkUser()){
            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
        }

        AppCartItem appCartItem= cartService.updateCartItem(cartitemId,commodityId,number);
        logger.debug("【更新后的数据】 "+appCartItem);
        if(appCartItem != null && appCartItem.getRemaining()){
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }else{
            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
        }

    }

    /**
     * 根据用户id获取用户购物车信息
     * @param userid
     * @return
     */
    @ApiOperation(value="根据用户id获取用户购物车信息",notes = "根据用户id获取用户购物车信息【负责人：王翼云】")
    @GetMapping(value ="/{userid}")
    public Result getCartItemsByUser(@PathVariable("userid") Long userid){
        logger.debug(" [userid] " + userid);

        User user = (User)request.getAttribute(Constant.REQUEST_USER);
        logger.debug("【当前用户id为】："+user+"  ;  【传入的用户ID为】："+userid);
        if(!checkUser()){
            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
        }
        if(!user.getId().equals(userid)){//如果传入的用户ID与登陆ID不同
            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
        }

        List<AppCartItemOut> appCartItemOuts = cartService.findCartItemsByUserId(user.getId());
//        logger.debug("【购物车信息】 "+appCartItemOut.toString());
        return Result.success(Constant.MessageConfig.MSG_SUCCESS,appCartItemOuts);
    }

    /**
     * 根据用户id获取用户购物车信息，分页
     * @param userid
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Result getCartItems(@PathVariable("userid") Long userid,
                               @RequestParam("pageNum")
                               @Min(value=1)
                               @NotNull int pageNum,
                               @RequestParam("pageSize")
                               @Min(value=1)
                               @NotNull int pageSize){
        logger.debug(" [userid] " + userid);
        logger.debug(" [pageNum] " + pageNum);
        logger.debug(" [pageSize] " + pageSize);
        User user = (User)request.getAttribute(Constant.REQUEST_USER);
        logger.debug("【当前用户id为】："+user+"  ;  【传入的用户ID为】："+userid);
        if(!checkUser()){
            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
        }
        if(!user.getId().equals(userid)){//如果传入的用户ID与登陆ID不同
            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
        }
        /**
         *注意这里的数据格式 ==> PageInfo<AppCartItem>
         */
        PageInfo<AppCartItem> pageInfo = cartService.findCartItemByUserId(user.getId(),pageNum,pageSize);
        logger.debug("【购物车信息】 "+pageInfo.toString());
        return Result.success(Constant.MessageConfig.MSG_SUCCESS,pageInfo);
    }

    /**
     * 向购物车添加纪录
     * @param appCartItemInput
     * @return
     */
    @ApiOperation(value="向购物车添加商品",notes = "向购物车添加商品【负责人：王翼云】")
    @PostMapping(value ="/cartitem")
    public Result createCartItems(@RequestBody @Validated AppCartItemInput appCartItemInput){

        logger.debug("【传入的参数信息为】 "+ appCartItemInput.toString());
        User user = (User)request.getAttribute(Constant.REQUEST_USER);
        if(!checkUser()){
            logger.debug("【user info 1】 "+ user);
            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
        }

        appCartItemInput.setUserId(user.getId());
        logger.debug("【user info 2】 "+ user);
        return convertBoolean(cartService.saveCartItem(appCartItemInput));
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
            result = Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }else{
            result = Result.fail(Constant.MessageConfig.MSG_FAILURE);
        }
        return result;
    }

}

