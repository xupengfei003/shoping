package so.sao.shop.supplier.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommInventoryInfoInput;
import so.sao.shop.supplier.pojo.input.CommInventoryInput;
import so.sao.shop.supplier.pojo.output.CommInventoryInfoOutput;
import so.sao.shop.supplier.pojo.output.CommInventoryOutput;
import so.sao.shop.supplier.service.CommInventoryService;
import so.sao.shop.supplier.util.Ognl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author gxy on 2017/10/13.
 */
@RestController
@RequestMapping("/commInventory")
@Api(description = "库存管理")
public class CommInventoryController {
    @Autowired
    private CommInventoryService commInventoryService;

    /**
     * 商品库存查询接口
     * @param commInventoryInput 检索条件
     * @return Result
     */
    @GetMapping("search")
    @ApiOperation(value = "商品库存查询接口", notes = "商品库存查询接口")
    public Result search(HttpServletRequest request, CommInventoryInput commInventoryInput) throws Exception {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if (Ognl.isNull(user)) { //判断是否登陆
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        if (!Constant.ADMIN_STATUS.equals(user.getIsAdmin())) { //非管理员
            commInventoryInput.setSupplierId(user.getAccountId());
            commInventoryInput.setFlag(0);
        } else { //管理员
            commInventoryInput.setFlag(1);
        }
        List<CommInventoryOutput> dataList = commInventoryService.search(commInventoryInput);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, new PageInfo<>(dataList));
    }

    /**
     * 获取某商品库存信息
     * @param id 商品Id
     * @return Result
     */
    @GetMapping("/getInventoryById/{id}")
    @ApiOperation(value = "获取某商品库存信息", notes = "获取某商品库存信息")
    public Result getInventoryById(@PathVariable Long id) throws Exception {
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, commInventoryService.getInventoryById(id));
    }

    /**
     * 更新某商品库存信息
     * @param commInventoryInfoInput 验证参数
     * @return Result
     * @throws Exception Exception
     */
    @PostMapping("/updateInventory")
    @ApiOperation(value = "更新某商品库存信息", notes = "更新某商品库存信息")
    public Result updateInventoryById(@RequestBody @Valid CommInventoryInfoInput commInventoryInfoInput) throws Exception {
        Long inventoryIncreasement = (commInventoryInfoInput.getInventoryIncreasement() != null ? commInventoryInfoInput.getInventoryIncreasement() : 0);
        CommInventoryInfoOutput commInventoryInfoOutput = commInventoryService.getInventoryById(commInventoryInfoInput.getId());
        if (commInventoryInfoOutput.getInventory() + inventoryIncreasement < 0) {
            return Result.fail("库存量不能小于0");
        } else {
            commInventoryInfoInput.setInventory(commInventoryInfoOutput.getInventory() + inventoryIncreasement);
        }
        commInventoryService.updateInventoryById(commInventoryInfoInput);
        return Result.success("该商品库存设置成功");
    }
}
