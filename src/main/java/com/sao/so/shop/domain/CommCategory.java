package com.sao.so.shop.domain;

/**
 * 商品品类实体类
 * Created by QuJunLong on 2017/7/17.
 */
public class CommCategory{
    /**
     * ID
     */
    private Long id;
    /**
     *  PID
     */
    private Long pid;
    /**
     *  类型名称
     */
    private String name;
    /**
     *  类型级别
     */
    private int level;
    /**
     *  备注
     */
    private String remark;

    private int sort;
    /**
     *  产生时间
     */
    private Long createdAt;
    /**
     *  更新时间
     */
    private Long updatedAt;

    /**
     * 删除标记
     */
    private int deleted;

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}
