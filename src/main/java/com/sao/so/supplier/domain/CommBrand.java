package com.sao.so.supplier.domain;

/**
 * 品牌
 * Created by QuJunLong on 2017/7/18.
 */
public class CommBrand {
    /**
     * ID
     */
    private Long id;
    /**
     * 品牌名称
     */
    private String name;
    /**
     * 删除标记
     */
    private int deleted;
    /**
     *  创建人
     */
    private Long createdBy;
    /**
     *  创建时间
     */
    private Long createdAt;
    /**
     * 更新人
     */
    private Long updatedBy;
    /**
     *  更新时间
     */
    private Long updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
