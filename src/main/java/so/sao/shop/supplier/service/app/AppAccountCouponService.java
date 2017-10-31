package so.sao.shop.supplier.service.app;

import so.sao.shop.supplier.pojo.Result;

import java.math.BigDecimal;

/**
 * <p>Version: shop-business V2.5.0 </p>
 * <p>Title: AppAccountCouponService</p>
 * <p>Description: APP 用户优惠券业务实现接口</p>
 * @author: liugang
 * @Date: Created in 2017/10/23 16:54
 */

public interface AppAccountCouponService {

    /**
     * 每日更新账户表的状态，看是否过期
     */
    void dailyUpdateStatus();

    /**
     * 获取我的优惠券列表
     * @param shopId
     * @param usableValue
     *@param pageNum
     * @param pageSize   @return
     */
    Result getAccountCoupons(Long shopId, BigDecimal usableValue, Integer pageNum, Integer pageSize);

    /**
     * 用户领取优惠券
     * @param shopId
     * @param couponId
     * @return
     */
    Result addAccountCoupon(Long shopId, Long couponId);

    /**
     * 查看用户领券中心列表
     * @param shopId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Result getCouponCenter(Long shopId, Integer pageNum, Integer pageSize);

    Integer useAccountCoupon(Long accountCouponId);
}
