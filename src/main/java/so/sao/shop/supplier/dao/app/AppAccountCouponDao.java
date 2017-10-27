package so.sao.shop.supplier.dao.app;

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
    List<AccountCoupon> findAccountCouponsByUserId(Long shopId);

    Integer addAccountCoupon(AccountCoupon accountCoupon);
}
