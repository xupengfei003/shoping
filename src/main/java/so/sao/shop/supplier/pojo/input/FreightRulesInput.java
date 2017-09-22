package so.sao.shop.supplier.pojo.input;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 费用规则入参类
 * </p>
 *
 * @author 透云-中软-西安项目组-zhenhai.zheng
 * @since 2017年9月19日 15:02:09
 **/
public class FreightRulesInput {
    /**
     * 规则类型:0-通用规则,1-配送地区物流费用规则
     */
    @Min(value = 0,message = "规则类型错误")
    @Max(value = 1,message = "规则类型错误")
    private Integer rulesType;

    /**
     * 是否包邮:0-不包,1-包邮
     */
    @Min(value = 0,message = "包邮类型错误")
    @Max(value = 1,message = "包邮类型错误")
    private Integer whetherShipping;

    /**
     * 起送金额
     */
    private BigDecimal sendAmount;

    /**
     * 默认计件
     */
    private Integer defaultPiece;

    /**
     * 超量计件
     */
    private Integer excessPiece;

    /**
     * 运费基础金额
     */
    private BigDecimal defaultAmount;

    /**
     * 运费增加金额
     */
    private BigDecimal excessAmount;

    /**
     * 备注
     */
    private String remark;

    public Integer getRulesType() {
        return rulesType;
    }

    public void setRulesType(Integer rulesType) {
        this.rulesType = rulesType;
    }

    public Integer getWhetherShipping() {
        return whetherShipping;
    }

    public void setWhetherShipping(Integer whetherShipping) {
        this.whetherShipping = whetherShipping;
    }

    public BigDecimal getSendAmount() {
        return sendAmount;
    }

    public void setSendAmount(BigDecimal sendAmount) {
        this.sendAmount = sendAmount;
    }

    public Integer getDefaultPiece() {
        return defaultPiece;
    }

    public void setDefaultPiece(Integer defaultPiece) {
        this.defaultPiece = defaultPiece;
    }

    public Integer getExcessPiece() {
        return excessPiece;
    }

    public void setExcessPiece(Integer excessPiece) {
        this.excessPiece = excessPiece;
    }

    public BigDecimal getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(BigDecimal defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public BigDecimal getExcessAmount() {
        return excessAmount;
    }

    public void setExcessAmount(BigDecimal excessAmount) {
        this.excessAmount = excessAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
