package so.sao.shop.supplier.dao.app;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.domain.AccountCoupon;
import so.sao.shop.supplier.domain.external.Coupon;
import so.sao.shop.supplier.pojo.output.CouponOutputVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>Version: shop-business V2.5.0 </p>
 * <p>Title: AppAccountCouponDao</p>
 * <p>Description: APP 用户优惠券DAO</p>
 * @author: liugang
 * @Date: Created in 2017/10/23 16:58
 */
public interface AppAccountCouponDao {
    /**
     * 更新表状态：
     * 是否过期
     */
    void updateAccountCouponStatus();

    /**
     * 查询用户的优惠券列表（所有状态）
     * @param shopId
     * @param usableValue
     * @return
     */
    List<CouponOutputVo> findAccountCouponsByUserId(@Param("accountId") Long shopId, @Param("usableValue") BigDecimal usableValue);

    /**
     * 领取优惠券
     * @param accountCoupon
     * @return
     */
    Integer insertAccountCoupon(@Param("pojo") AccountCoupon accountCoupon);

    /**
     * 更新优惠券的状态为已使用
     * @param shopId
     * @param couponId
     * @return
     */
    Integer updateAccountCouponStatusById(@Param("accountId") Long shopId, @Param("couponId") Long couponId);

    /**
     * 通过门店id和优惠券id查找用户优惠券
     * @param shopId
     * @param couponId
     * @return
     */
    List<AccountCoupon> findAccountCoupon(@Param("accountId") Long shopId, @Param("couponId") Long couponId);
}
