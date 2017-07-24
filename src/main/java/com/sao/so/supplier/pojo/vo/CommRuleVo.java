package com.sao.so.supplier.pojo.vo;

/**
 * Created by QuJunLong on 2017/7/20.
 */
public class CommRuleVo {
    /**
     * ID
     */
    private Long id;
    /**
     * 供应商与商品关系ID
     */
    private Long scId;
    /**
     * 规格名称
     */
    private String name;
    /**
     * 规格值
     */
    private String value;

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
     * 删除标记
     */
    private int deleted;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScId() {
        return scId;
    }

    public void setScId(Long scId) {
        this.scId = scId;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}
