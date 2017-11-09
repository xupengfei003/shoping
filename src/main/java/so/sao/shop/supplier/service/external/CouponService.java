package so.sao.shop.supplier.service.external;

import so.sao.shop.supplier.domain.external.Coupon;
import so.sao.shop.supplier.pojo.Result;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>Version: shop-portal V0.9.0 </p>
 * <p>Title: CouponService</p>
 * <p>Description: 优惠券业务实现接口</p>
 * @author: liugang
 * @Date: Created in 2017/10/23 16:55
 */
public interface CouponService {

    /**
     * 每日更新账户表的状态，看是否过期
     */
    void dailyUpdateStatus();

    /**
     * 添加优惠券
     * @param coupon

     * @return
     */
    Result addCoupon(Coupon coupon);

    /**
     * 根据id批量废弃优惠券
     * @param couponIds
     * @return
     */
    Result batchDiscardCouponsByIds(Long[] couponIds);

    /**
     * 搜索优惠券列表
     * @param name
     * @param couponStatus
     * @param pageNum
     * @param pageSize
     * @return
     */
    Result searchCoupons(String name, Integer[] couponStatus, Integer pageNum, Integer pageSize);
}
