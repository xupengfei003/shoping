package com.sao.so.shop.pojo.output;

import com.sao.so.shop.pojo.vo.CommImgeVo;
import com.sao.so.shop.pojo.vo.CommRuleVo;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * 商品对象 出参
 * 用于商品新增修改 使用
 * Created by QuJunLong on 2017/7/19.
 */
public class CommodityOutput {

    /**
     *ID
     */
    private Long id;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 一级类型Id
     */
    private Long categoryOneId;
    /**
     * 二级类型Id
     */
    private Long categoryTwoId;
    /**
     * 三级类型Id
     */
    private Long categoryThreeId;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品描述
     */
    private String remark;
    /**
     * 商品介绍
     */
    private String description;
    /**
     * 商家编码
     */
    private String code;
    /**
     * 商品编码
     */
    private String code69;
    /**
     * 规格名称
     */
    private String ruleName;
    /**
     * 规格值
     */
    private String ruleVal;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 库存
     */
    private Double inventory;
    /**
     * 缩略图
     */
    private String minImg;
    /**
     * 市场价
     */
    private Double price;
    /**
     * 售价
     */
    private Double unitPrice;
    /**
     * 图片集合
     */
    private List<CommImgeVo> imgeList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getCategoryOneId() {
        return categoryOneId;
    }

    public void setCategoryOneId(Long categoryOneId) {
        this.categoryOneId = categoryOneId;
    }

    public Long getCategoryTwoId() {
        return categoryTwoId;
    }

    public void setCategoryTwoId(Long categoryTwoId) {
        this.categoryTwoId = categoryTwoId;
    }

    public Long getCategoryThreeId() {
        return categoryThreeId;
    }

    public void setCategoryThreeId(Long categoryThreeId) {
        this.categoryThreeId = categoryThreeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Double getInventory() {
        return inventory;
    }

    public void setInventory(Double inventory) {
        this.inventory = inventory;
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

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public List<CommImgeVo> getImgeList() {
        return imgeList;
    }

    public void setImgeList(List<CommImgeVo> imgeList) {
        this.imgeList = imgeList;
    }
}
