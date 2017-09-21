package so.sao.shop.supplier.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import so.sao.shop.supplier.pojo.output.CommodityAuditinputOutput;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by acer on 2017/9/19.
 */
public class CommodityAuditinputVo {
    /**
     * 审核表ID
     */
    private Long id;
    /**
     * 商品ID
     */
    private Long scId;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 缩略图地址
     */
    private String minImg;

    /**
     * 商品条码
     */
    private String code69;
    /**
     * 商品商家编码
     */
    private String code;
    /**
     * 品牌名称
     */
    private String  brandName;
    /**
     * 包装单位名称
     */
    private String unitName;
    /**
     * 计量规格名称
     */
    private String measureSpecName;
    /**
     * 规格值
     */
    private String ruleVal;
    /**
     * 市场价
     */
    private BigDecimal price;
    /**
     * 售价
     */
    private BigDecimal unitPrice;
    /**
     * 库存
     */
    private Double inventory;
    /**
     * 起订量
     */
    private int minOrderQuantity;
    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updatedAt;
    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createdAt;
    /**
     * 商品状态
     */
    private int status;
    /**
     * 审核结果
     */
    private int auditResult;
    /**
     * 审核意见
     */
    private String auditOpinion;

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

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinImg() {
        return minImg;
    }

    public void setMinImg(String minImg) {
        this.minImg = minImg;
    }

    public String getCode69() {
        return code69;
    }

    public void setCode69(String code69) {
        this.code69 = code69;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getMeasureSpecName() {
        return measureSpecName;
    }

    public void setMeasureSpecName(String measureSpecName) {
        this.measureSpecName = measureSpecName;
    }

    public String getRuleVal() {
        return ruleVal;
    }

    public void setRuleVal(String ruleVal) {
        this.ruleVal = ruleVal;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getInventory() {
        return inventory;
    }

    public void setInventory(Double inventory) {
        this.inventory = inventory;
    }

    public int getMinOrderQuantity() {
        return minOrderQuantity;
    }

    public void setMinOrderQuantity(int minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(int auditResult) {
        this.auditResult = auditResult;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }
}
