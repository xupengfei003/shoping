package com.sao.so.shop.domain;

/**
 * Created by acer on 2017/7/22.
 */
public class SysRegion {
    /**
     * 区划id
     */
    private Integer srId;
    /**
     * 区划名称
     */
    private String name;
    /**
     * 区划级别
     */
    private Integer level;

    public Integer getSrId() {
        return srId;
    }

    public void setSrId(Integer srId) {
        this.srId = srId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
