package so.sao.shop.supplier.pojo.output;/**
 * Created by wyy on 2017/10/30.
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import so.sao.shop.supplier.domain.external.Coupon;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>Version: supplier V1.1.0 </p>
 * <p>Title: CouponOutputVo</p>
 * <p>Description: </p>
 *
 * @author: yiyun.wang
 * @Date: Created in 2017/10/30 17:46
 */
public class CouponOutputVo {
    /**
     * ID
     */
    private Long couponId;
    /**
     * 购物券名称
     */
    private String name;
    /**
     * 优惠券减免金额/折率
     */
    private BigDecimal couponValue;
    /**
     * 优惠券适用金额，满多少可用
     */
    private BigDecimal usableValue;

    /**
     * 使用开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date useStartTime;
    /**
     * 使用结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date useEndTime;

    /**
     * 优惠券领取状态，0（已生效），1（未生效），2（已过期）
     */
    private Integer status;


    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(BigDecimal couponValue) {
        this.couponValue = couponValue;
    }

    public BigDecimal getUsableValue() {
        return usableValue;
    }

    public void setUsableValue(BigDecimal usableValue) {
        this.usableValue = usableValue;
    }

    public Date getUseStartTime() {
        return useStartTime;
    }

    public void setUseStartTime(Date useStartTime) {
        this.useStartTime = useStartTime;
    }

    public Date getUseEndTime() {
        return useEndTime;
    }

    public void setUseEndTime(Date useEndTime) {
        this.useEndTime = useEndTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
