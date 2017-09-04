package so.sao.shop.supplier.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

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
    /**
     * 商品条码
     */
    private String code69;
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
     * 企业名称
     */
    private String companyName;
    /**
     * 产地
     */
    private String originPlace;
    /**
     * 上市时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date marketTime;
    /**
     *  创建人
     */
    private Long createdBy;
    /**
     *  创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createdAt;
    /**
     * 更新人
     */
    private Long updatedBy;
    /**
     *  更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updatedAt;

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

    public String getCode69() {
        return code69;
    }

    public void setCode69(String code69) {
        this.code69 = code69;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOriginPlace() {
        return originPlace;
    }

    public void setOriginPlace(String originPlace) {
        this.originPlace = originPlace;
    }

    public Date getMarketTime() {
        return marketTime;
    }

    public void setMarketTime(Date marketTime) {
        this.marketTime = marketTime;
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

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
