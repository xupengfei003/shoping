package so.sao.shop.supplier.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 费用规则实体
 * @author gxy on 2017/9/18.
 */
public class FreightRules {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 规则类型:0-通用规则,1-配送地区物流费用规则
     */
    private Integer rulesType;

    /**
     * 省
     */
    private String addressProvince;

    /**
     * 市
     */
    private String addressCity;

    /**
     * 区
     */
    private String addressDistrict;

    /**
     * 是否包邮:0-不包,1-包邮
     */
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

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    /**
     * 更改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getRulesType() {
        return rulesType;
    }

    public void setRulesType(Integer rulesType) {
        this.rulesType = rulesType;
    }

    public String getAddressProvince() {
        return addressProvince;
    }

    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressDistrict() {
        return addressDistrict;
    }

    public void setAddressDistrict(String addressDistrict) {
        this.addressDistrict = addressDistrict;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}