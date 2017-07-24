package com.sao.so.shop.pojo.input;

import com.sao.so.shop.pojo.vo.CommImgeVo;
import com.sao.so.shop.pojo.vo.CommRuleVo;
import com.sao.so.shop.pojo.vo.SupplierCommodityVo;

import java.util.List;

/**
 * 商品对象 出参
 * 用于商品新增修改 使用
 * Created by QuJunLong on 2017/7/19.
 */
public class CommodityInput {

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
     * 图片集合
     */
    private List<CommImgeVo> imgeList;
    /**
     * 商品集合
     */
    private List<SupplierCommodityVo> commodityList;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CommImgeVo> getImgeList() {
        return imgeList;
    }

    public void setImgeList(List<CommImgeVo> imgeList) {
        this.imgeList = imgeList;
    }

    public List<SupplierCommodityVo> getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(List<SupplierCommodityVo> commodityList) {
        this.commodityList = commodityList;
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
}
