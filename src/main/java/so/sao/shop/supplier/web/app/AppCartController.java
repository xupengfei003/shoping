package so.sao.shop.supplier.web.app;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.AppCartItemBatchInput;
import so.sao.shop.supplier.pojo.input.AppCartItemInput;
import so.sao.shop.supplier.pojo.input.AppCartItemSaveInput;
import so.sao.shop.supplier.pojo.output.AppCartItemOut;
import so.sao.shop.supplier.service.app.AppCartService;
import so.sao.shop.supplier.util.Ognl;

import javax.validation.Valid;
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
    @GetMapping(value = "/cartitem/delete")
    public Result deleteCartItem(Long cartitemId, Long userId) throws Exception {
        if (Ognl.isNull(cartitemId)) {
            return Result.fail("购物车记录ID不能为空");
        }
        if (Ognl.isNull(userId)) {
            return Result.fail("用户ID不能为空");
        }
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
     * @param input
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "更新购物车商品数量", notes = "更新购物车商品数量")
    @PostMapping(value = "/cartitem/update")
    public Result updateCartItem(@RequestBody @Valid AppCartItemInput input) throws Exception {
        Integer number = Integer.valueOf(input.getNumber());
        // 更新数据
        Map<String, Object> map = cartService.updateCartItem(input.getCartitemId(), number, input.getUserId());
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

    @ApiOperation(value = "批量更新购物车商品数量", notes = "批量更新购物车商品数量")
    @PostMapping(value = "/cartitem/updateBatch")
    public Result updateCartItemBatch(@RequestBody @Valid AppCartItemBatchInput inputList) throws Exception {
        List<AppCartItemInput> list = inputList.getList();
        // 更新数据
        Map<String, Object> map = cartService.updateCartItemBatch(list);
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
     * @param input
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "向购物车添加购物记录", notes = "向购物车添加购物记录")
    @PostMapping(value = "/cartitem/save")
    public Result createCartItems(@RequestBody @Valid AppCartItemSaveInput input) throws Exception {
        // 转化为Integer
        Integer newNumber = Integer.valueOf(input.getNumber());
        // 插入数据
        Map<String, Object> map = cartService.saveCartItem(input.getCommodityId(), newNumber, input.getUserId());
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
    @GetMapping(value = "/cartitem/select")
    public Result getCartItemsByUser(Long userId) throws Exception {
        if (Ognl.isNull(userId)) {
            return Result.fail("用户ID不能为空");
        }
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

