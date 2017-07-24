package com.sao.so.supplier.domain;

/**
 * 商品实体类
 * Created by QuJunLong on 2017/7/17.
 */
public class Commodity{
    /**
     * ID
     */
    private Long id;
    /**
     * 品牌
     */
    private Long brandId;
    /**
     * 商品名称
     */
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
