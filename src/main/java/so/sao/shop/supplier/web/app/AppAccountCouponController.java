package so.sao.shop.supplier.web.app;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.app.AppAccountCouponService;
import so.sao.shop.supplier.service.external.CouponService;

/**
 * <p>Version: shop-business V2.5.0 </p>
 * <p>Title: AppAccountCouponController</p>
 * <p>Description: APP 用户优惠券Controller</p>
 * @author: liugang
 * @Date: Created in 2017/10/23 16:47
 */
@RestController
@RequestMapping("/app/accountCoupon")
@Api(description = "App用户优惠券相关类")
public class AppAccountCouponController {

    @Autowired
    private AppAccountCouponService appAccountCouponService;
    @Autowired
    private CouponService couponService;

    @ApiOperation(value = "用户优惠券添加",notes = "用户优惠券添加【负责人：】")
    @PostMapping("/save")
    public Result save() {
        return null;
    }

    @ApiOperation(value = "查询用户优惠券信息",notes = "查询用户优惠券信息【负责人：】")
    @PostMapping("/get")
    public Result getAccountCoupons() {
        return null;
    }
}
