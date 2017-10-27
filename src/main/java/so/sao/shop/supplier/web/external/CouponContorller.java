package so.sao.shop.supplier.web.external;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.domain.external.Coupon;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.external.CouponService;


/**
 * <p>Version: shop-portal V0.9.0 </p>
 * <p>Title: CouponContorller</p>
 * <p>Description: 优惠券Controller</p>
 * @author: liugang
 * @Date: Created in 2017/10/23 16:43
 */
@RestController
@RequestMapping("/external/coupon")
@Api(description = "优惠券相关类")
public class CouponContorller {

    @Autowired
    private CouponService couponService;


    @ApiOperation(value = "添加优惠券",notes = "添加优惠券【负责人：】")
    @PostMapping("/save")
    public Result save(Coupon coupon) {
        return couponService.addCoupon(coupon);
    }

    @ApiOperation(value = "批量废弃优惠券",notes = "批量废弃优惠券【负责人：】")
    @DeleteMapping("/delete/{ids}")
    public Result discardCoupons(Long[] couponIds) {
        return couponService.batchDiscardCouponsByIds(couponIds);
    }

    @ApiOperation(value = "搜索优惠券列表信息",notes = "查询优惠券信息【负责人：】")
    @PostMapping("/search")
    public Result searchCoupons(String name,Integer[] couponStatus,Integer pageNum,Integer pageSize) {
        return couponService.searchCoupons(name,couponStatus,pageNum,pageSize);
    }
}
