package so.sao.shop.supplier.domain.external;

import java.util.Date;

/**
 * <p>Version: shop-portal V0.9.0 </p>
 * <p>Title: Coupon</p>
 * <p>Description: 优惠券实体类</p>
 * @author: liugang
 * @Date: Created in 2017/10/23 16:23
 */
public class Coupon {
    /**
     * ID
     */
    private Long id;
    /**
     * 购物券名称
     */
    private String name;
    /**
     * 优惠券适用类型，0为全品类
     */
    private Long categoryId;
    /**
     * 优惠券类型 1、满减   2、打折
     */
    private Integer couponType;
    /**
     * 优惠券减免金额/折率
     */
    private Double couponValue;
    /**
     * 优惠券适用金额，满多少可用
     */
    private Integer couponUsePrice;
    /**
     * 优惠券生效时间
     */
    private Date couponEffectiveTime;
    /**
     * 优惠券失效时间
     */
    private Date couponInvalidTime;
    /**
     * 优惠券状态，0（可使用），1（已过期），2（未生效），3（已删除）
     */
    private Integer couponStatus;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Double getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(Double couponValue) {
        this.couponValue = couponValue;
    }

    public Integer getCouponUsePrice() {
        return couponUsePrice;
    }

    public void setCouponUsePrice(Integer couponUsePrice) {
        this.couponUsePrice = couponUsePrice;
    }

    public Date getCouponEffectiveTime() {
        return couponEffectiveTime;
    }

    public void setCouponEffectiveTime(Date couponEffectiveTime) {
        this.couponEffectiveTime = couponEffectiveTime;
    }

    public Date getCouponInvalidTime() {
        return couponInvalidTime;
    }

    public void setCouponInvalidTime(Date couponInvalidTime) {
        this.couponInvalidTime = couponInvalidTime;
    }

    public Integer getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(Integer couponStatus) {
        this.couponStatus = couponStatus;
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
