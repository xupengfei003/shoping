package so.sao.shop.supplier.pojo.output;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import so.sao.shop.supplier.pojo.BaseResult;

/**
 * Created by renle on 2017/7/20.
 */
public class CommodityExportOutput extends BaseResult{
    /**
     * 商品id
     */
    private Long commId;
    /**
     * 商品编码
     */
    private String code69;
    /**
     * 品牌
     */
    private String brandName;
    /**
     * 商品名称
     */
    private String commName;
    /**
     * 商家编码
     */
    private String supplierCode;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 规格名称
     */
    private String ruleName;
    /**
     * 规格值
     */
    private String ruleValue;
    /**
     * 市场价
     */
    private Double price;
    /**
     * 售价
     */
    private Double unitPrice;
    /**
     * 库存
     */
    private Double inventory;
    /**
     * 商品状态
     * 已上架 待上架 已下架
     */
    private int status;
    /**
     *  创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createdAt;
    /**
     *  更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updatedAt;

    public Long getCommId() {
        return commId;
    }

    public void setCommId(Long commId) {
        this.commId = commId;
    }

    public String getCode69() {
        return code69;
    }

    public void setCode69(String code69) {
        this.code69 = code69;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getInventory() {
        return inventory;
    }

    public void setInventory(Double inventory) {
        this.inventory = inventory;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
