package so.sao.shop.supplier.web.app;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.AccountCouponInputVo;
import so.sao.shop.supplier.service.app.AppAccountCouponService;
import so.sao.shop.supplier.service.external.CouponService;

import java.math.BigDecimal;

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


    /**
     *
     * @param inputVo
     * @return
     */
    @ApiOperation(value = "领取优惠券",notes = "用户优惠券添加【负责人：王翼云】")
    @PostMapping("/save")
    public Result save(@RequestBody @Validated AccountCouponInputVo inputVo) {
        return appAccountCouponService.addAccountCoupon(inputVo.getShopId(),inputVo.getCouponId());
    }

    @ApiOperation(value = "查询用户优惠券信息",notes = "查询用户优惠券信息【负责人：王翼云】")
    @GetMapping("/list")
    public Result getAccountCoupons(Long shopId, BigDecimal usableValue, Integer pageNum, Integer pageSize) {
        return appAccountCouponService.getAccountCoupons(shopId,usableValue,pageNum,pageSize);
    }

    @ApiOperation(value = "领券中心",notes = "查询用户领券中心信息【负责人：王翼云】")
    @GetMapping("/couponCenter")
    public Result getCouponCenter(@RequestParam("shopId") Long shopId,Integer pageNum, Integer pageSize) {
        return appAccountCouponService.getCouponCenter(shopId,pageNum,pageSize);
    }

}
