package so.sao.shop.supplier.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.output.AppPurchaseOutput;
import so.sao.shop.supplier.pojo.vo.AppPurchasesVo;
import so.sao.shop.supplier.pojo.vo.PurchasesVo;
import so.sao.shop.supplier.service.AppPurchaseService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by acer on 2017/9/6.
 */
@RestController
@RequestMapping("/appOrder")
@Api(description = "App订单类-所有接口")
public class AppPurchaseController {
    @Resource
    private AppPurchaseService appPurchaseService;
    @GetMapping(value = "/appOrderList")
    @ApiOperation(value = "门店端获取订单列表", notes = "负责人【白治华】")
    public Result appOrderList(HttpServletRequest request,Integer pageNum, Integer rows, Integer orderStatus) throws Exception{
        //获取当前登陆账户
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        if (rows == null || rows <= 0) {
            rows = 5;
        }
        if (pageNum == null || pageNum <= 0) {
            pageNum = 1;
        }
        PageInfo<AppPurchaseOutput> appPurchasesVoList = appPurchaseService.findOrderList(pageNum, rows, BigInteger.valueOf(user.getId()),orderStatus);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS,appPurchasesVoList);
    }
}
