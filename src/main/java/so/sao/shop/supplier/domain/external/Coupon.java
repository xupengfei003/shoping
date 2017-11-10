package so.sao.shop.supplier.domain.external;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
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
    @NotEmpty
    @Size(min = 0,max = 20,message = "名称不能超过20个字符")
    private String name;
    /**
     * 优惠券适用类型，0为全品类
     */
    private Long categoryId;
    /**
     * 优惠券类型 1、满减   2、打折
     */
    private Integer discountWay;
    /**
     * 优惠券减免金额/折率
     */
    @NotNull
    @DecimalMin(value = "0.01" ,message = "最小金额为0.01")
    private BigDecimal couponValue;
    /**
     * 优惠券适用金额，满多少可用
     */
    @DecimalMin(value = "0.01" ,message = "最小金额为0.01")
    private BigDecimal usableValue;
    /**
     * 优惠券生效时间
     */

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date sendStartTime;
    /**
     * 优惠券失效时间
     */

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date sendEndTime;
    /**
     * 使用开始时间
     */
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date useStartTime;
    /**
     * 使用结束时间
     */
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date useEndTime;
    /**
     * 优惠券发放总数
     */
    @NotNull
    @Min(value = 1,message = "发放数量不能小于1")
    private Integer createNum;
    /**
     * 优惠券领取量
     */

    private Integer sendNum;
    /**
     * 优惠券使用量
     */
    private Integer useNum;
    /**
     * 优惠券状态，0（已生效），1（未生效），2（已过期），3（已废弃）
     */
    private Integer status;
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

    private String operator;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

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

    public Integer getDiscountWay() {
        return discountWay;
    }

    public void setDiscountWay(Integer discountWay) {
        this.discountWay = discountWay;
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

    public Date getSendStartTime() {
        return sendStartTime;
    }

    public void setSendStartTime(Date sendStartTime) {
        this.sendStartTime = sendStartTime;
    }

    public Date getSendEndTime() {
        return sendEndTime;
    }

    public void setSendEndTime(Date sendEndTime) {
        this.sendEndTime = sendEndTime;
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

    public Integer getCreateNum() {
        return createNum;
    }

    public void setCreateNum(Integer createNum) {
        this.createNum = createNum;
    }

    public Integer getSendNum() {
        return sendNum;
    }

    public void setSendNum(Integer sendNum) {
        this.sendNum = sendNum;
    }

    public Integer getUseNum() {
        return useNum;
    }

    public void setUseNum(Integer useNum) {
        this.useNum = useNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", discountWay=" + discountWay +
                ", couponValue=" + couponValue +
                ", usableValue=" + usableValue +
                ", sendStartTime=" + sendStartTime +
                ", sendEndTime=" + sendEndTime +
                ", useStartTime=" + useStartTime +
                ", useEndTime=" + useEndTime +
                ", createNum=" + createNum +
                ", sendNum=" + sendNum +
                ", useNum=" + useNum +
                ", status=" + status +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", operator='" + operator + '\'' +
                '}';
    }
}
