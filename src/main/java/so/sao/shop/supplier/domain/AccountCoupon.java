package so.sao.shop.supplier.domain;

import java.util.Date;

/**
 * <p>Version: shop-portal V0.9.0 </p>
 * <p>Title: AccountCoupon</p>
 * <p>Description: 用户优惠券实体类</p>
 * @author: liugang
 * @Date: Created in 2017/10/23 16:23
 */
public class AccountCoupon {
    /**
     * id
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long accountId;
    /**
     * 优惠券ID
     */
    private Long couponId;
    /**
     * 用户优惠券状态，0（可使用），1（已过期），2（已使用），3（已删除），4(已失效)
     */
    private Integer accountCouponStatus;
    /**
     * 创建时间
     */
    private Date createAt;
    /**
     * 更新时间
     */
    private Date updateAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Integer getAccountCouponStatus() {
        return accountCouponStatus;
    }

    public void setAccountCouponStatus(Integer accountCouponStatus) {
        this.accountCouponStatus = accountCouponStatus;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
