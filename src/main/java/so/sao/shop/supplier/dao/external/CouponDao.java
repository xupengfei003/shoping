package so.sao.shop.supplier.dao.external;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.external.Coupon;
import so.sao.shop.supplier.pojo.output.CouponOutputVo;

import java.util.List;

/**
 * <p>Version: shop-portal V0.9.0 </p>
 * <p>Title: CouponDao</p>
 * <p>Description: 优惠券DAO</p>
 * @author: liugang
 * @Date: Created in 2017/10/23 16:59
 */
public interface CouponDao {
    /**
     * 查询优惠券列表
     * @param name
     * @param couponStatus
     * @return
     */
    List<Coupon> findCoupons(@Param("name")String name, @Param("status") Integer[] couponStatus);

    Integer insertCoupon(@Param("pojo")Coupon coupon);

    Integer findCouponsByName(@Param("name")String name);

    /**
     * 批量更改优惠券状态为废弃
     * @param couponIds
     * @return
     */
    Integer updateCouponStatusById(@Param("ids")Long[] couponIds,@Param("status")Integer status);

    /**
     * 查找某个用户优惠券列表
     * @param shopId
     * @return
     */
    List<CouponOutputVo> findCouponsByShopId(@Param("shopId") Long shopId);

    /**
     * 更新优惠券数量
     * @param couponId
     * @param i
     */
    Integer updateCouponNum(Long couponId, Integer i);
}
