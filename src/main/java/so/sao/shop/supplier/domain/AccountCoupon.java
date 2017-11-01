package so.sao.shop.supplier.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * <p>Version: shop-portal V0.9.0 </p>
 * <p>Title: AccountCoupon</p>
 * <p>Description: 用户优惠券实体类</p>
 *
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
     * 用户优惠券状态，0（可使用），1（已使用），2（未生效），3（已废弃），4(已过期)
     */
    private Integer status;
    /**
     * 使用时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date useTime;
    /**
     * 领取时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date getTime;
    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createAt;
    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateAt;

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets account id.
     *
     * @return the account id
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * Sets account id.
     *
     * @param accountId the account id
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    /**
     * Gets coupon id.
     *
     * @return the coupon id
     */
    public Long getCouponId() {
        return couponId;
    }

    /**
     * Sets coupon id.
     *
     * @param couponId the coupon id
     */
    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Gets use time.
     *
     * @return the use time
     */
    public Date getUseTime() {
        return useTime;
    }

    /**
     * Sets use time.
     *
     * @param useTime the use time
     */
    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    /**
     * Gets get time.
     *
     * @return the get time
     */
    public Date getGetTime() {
        return getTime;
    }

    /**
     * Sets get time.
     *
     * @param getTime the get time
     */
    public void setGetTime(Date getTime) {
        this.getTime = getTime;
    }

    /**
     * Gets create at.
     *
     * @return the create at
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * Sets create at.
     *
     * @param createAt the create at
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * Gets update at.
     *
     * @return the update at
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * Sets update at.
     *
     * @param updateAt the update at
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public String toString() {
        return "AccountCoupon{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", couponId=" + couponId +
                ", status=" + status +
                ", useTime=" + useTime +
                ", getTime=" + getTime +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }




}
