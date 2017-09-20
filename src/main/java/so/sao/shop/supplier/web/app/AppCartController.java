package so.sao.shop.supplier.web.app;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.output.AppCartItemOut;
import so.sao.shop.supplier.service.app.AppCartService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
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
     *
     * @param cartitemId
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据购物车ID删除购物车记录", notes = "根据购物车ID删除购物车记录")
    @GetMapping(value = "/cartitem/d/{cartitemId}")
    public Result deleteCartItem(@PathVariable("cartitemId") Long cartitemId,
                                 @NotNull(message = "用户ID不能为空") Long userId) throws Exception {
        // 删除购物车记录
        Boolean flag = cartService.deleteCartItemById(cartitemId, userId);
        // 返回结果
        if (flag) {
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        } else {
            return Result.fail(Constant.MessageConfig.MSG_FAILURE);
        }
    }

    /**
     * 更新购物车商品数量
     *
     * @param cartitemId
     * @param number
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "更新购物车商品数量", notes = "更新购物车商品数量")
    @PostMapping(value = "/cartitem/u/{cartitemId}")
    public Result updateCartItem(@PathVariable("cartitemId") Long cartitemId,
                                 @NotNull(message = "更新数量不能为空")
                                 @Pattern(regexp = "^[1-9][0-9]*$", message = "更新数量有误") Integer number,
                                 @NotNull(message = "用户ID不能为空") Long userId) throws Exception {
        // 更新数据
        Map<String, Object> map = cartService.updateCartItem(cartitemId, number, userId);
        // 获取提示信息
        String msg = (String) map.get("msg");
        // 获取信息码
        String code = (String) map.get("code");
        if ("0".equals(code)) {
            return Result.fail(msg);
        } else {
            return Result.success(msg);
        }
    }

    /**
     * 向购物车添加购物记录
     *
     * @param number
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "向购物车添加购物记录", notes = "向购物车添加购物记录")
    @PostMapping(value = "/cartitem")
    public Result createCartItems(@NotNull(message = "商品ID不能为空") Long commodityId,
                                  @NotNull(message = "购物车添加商品数量不能为空")
                                  @Pattern(regexp = "^[1-9][0-9]*$", message = "购物车添加商品数量有误") Integer number,
                                  @NotNull(message = "用户ID不能为空") Long userId) throws Exception {
        // 插入数据
        Map<String, Object> map = cartService.saveCartItem(commodityId, number, userId);
        // 获取提示信息
        String msg = (String) map.get("msg");
        // 获取信息码
        String code = (String) map.get("code");
        if ("0".equals(code)) {
            return Result.fail(msg);
        } else {
            return Result.success(msg);
        }
    }


    /**
     * 根据用户id获取用户购物车信息
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "根据用户id获取用户购物车信息", notes = "根据用户id获取用户购物车信息【负责人：王翼云】")
    @GetMapping(value = "/{userId}")
    public Result getCartItemsByUser(@PathVariable("userId") Long userId) throws Exception {
        // 查询数据
        Map<String, Object> map = cartService.findCartItemsByUserId(userId);
        // 获取信息码
        String code = (String) map.get("code");
        // 获取提示信息
        String msg = (String) map.get("msg");
        // 获取查询数据
        List<AppCartItemOut> outList = (List<AppCartItemOut>) map.get("collection");
        if ("1".equals(code)) {
            return Result.success(msg, outList);
        } else {
            return Result.fail(msg, outList);
        }
    }
}

