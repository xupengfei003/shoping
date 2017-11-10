package so.sao.shop.supplier.web.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.AccountCouponInputVo;
import so.sao.shop.supplier.service.app.AppAccountCouponService;

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
public class AppAccountCouponController {

    @Autowired
    private AppAccountCouponService appAccountCouponService;


    /**
     *
     * @param inputVo
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody @Validated AccountCouponInputVo inputVo) {
        return appAccountCouponService.addAccountCoupon(inputVo.getShopId(),inputVo.getCouponId());
    }

    @GetMapping("/list")
    public Result getAccountCoupons(Long shopId, BigDecimal usableValue, Integer pageNum,Integer pageSize) {
        return appAccountCouponService.getAccountCoupons(shopId,usableValue,pageNum,pageSize);
    }

    @GetMapping("/couponCenter")
    public Result getCouponCenter(@RequestParam("shopId") Long shopId,Integer pageNum, Integer pageSize) {
        return appAccountCouponService.getCouponCenter(shopId,pageNum,pageSize);
    }

    @GetMapping("/useCoupon")
    public Result getCouponCenter(@RequestParam("shopId") Long shopId,@RequestParam("couponId") Long couponId) {
        return appAccountCouponService.useAccountCoupon(shopId,couponId);
    }

}
