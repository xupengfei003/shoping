package so.sao.shop.supplier.dao.app;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.AccountCoupon;

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
     * @return
     */
    List<AccountCoupon> findAccountCouponsByUserId(@Param("accountId") Long shopId);

    /**
     * 领取优惠券
     * @param accountCoupon
     * @return
     */
    Integer insertAccountCoupon(@Param("pojo") AccountCoupon accountCoupon);

    /**
     * 更新优惠券的状态为已使用
     * @param id
     * @return
     */
    Integer updateAccountCouponStatusById(@Param("id")Long id);
}
