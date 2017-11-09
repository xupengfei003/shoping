package so.sao.shop.supplier.web.external;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.domain.external.Coupon;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.external.CouponService;

import javax.servlet.http.HttpServletRequest;


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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CouponService couponService;

    @ApiOperation(value = "添加优惠券",notes = "添加优惠券【负责人：王翼云】")
    @PostMapping(value = "/save" )
    public Result save(@RequestBody Coupon coupon) {

        return couponService.addCoupon(coupon);
    }

    @ApiOperation(value = "批量废弃优惠券",notes = "批量废弃优惠券【负责人：王翼云】")
    @DeleteMapping("/delete")
    public Result discardCoupons(@RequestParam("couponIds") Long[] couponIds) {
        return couponService.batchDiscardCouponsByIds(couponIds);
    }

    @ApiOperation(value = "搜索优惠券列表信息",notes = "查询优惠券信息【负责人：王翼云】")
    @GetMapping("/search")
    public Result searchCoupons(String name,Integer[] couponStatus,Integer pageNum,Integer pageSize, HttpServletRequest request){
        return couponService.searchCoupons(name,couponStatus,pageNum,pageSize);
    }


}
