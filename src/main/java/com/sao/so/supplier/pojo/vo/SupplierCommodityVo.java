package com.sao.so.supplier.pojo.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

/**
 * 供应商商品关系表
 * Created by QuJunLong on 2017/7/18.
 */
public class SupplierCommodityVo {
    /**
     * ID
     */
    private Long id;
    /**
     * 商家编码
     */
    @NotBlank(message = "商家编码不能为空")
    @Length(max = 20,message = "商家编码长度不能大于20")
    private String code;
    /**
     * 商品编码
     */
    @NotBlank(message = "商品编码不能为空")
    @Length(max = 20,message = "商品编码长度不能大于20")
    private String code69;
    /**
     * 规格名称
     */
    @NotBlank(message = "规格名称不能为空")
    @Length(max = 20,message = "规格名称长度不能大于20")
    private String ruleName;
    /**
     * 规格值
     */
    @NotBlank(message = "规格值不能为空")
    @Length(max = 20,message = "规格值长度不能大于20")
    private String ruleVal;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 库存
     */
    @Min(value=0)
    private Double inventory;
    /**
     * 缩略图
     */
    private String minImg;
    /**
     * 市场价
     */
    @Min(value=0)
    private Double price;
    /**
     * 售价
     */
    @Min(value=0)
    private Double unitPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode69() {
        return code69;
    }

    public void setCode69(String code69) {
        this.code69 = code69;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleVal() {
        return ruleVal;
    }

    public void setRuleVal(String ruleVal) {
        this.ruleVal = ruleVal;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMinImg() {
        return minImg;
    }

    public void setMinImg(String minImg) {
        this.minImg = minImg;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
