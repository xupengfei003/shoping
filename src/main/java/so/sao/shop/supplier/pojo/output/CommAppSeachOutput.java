package so.sao.shop.supplier.pojo.output;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 店主APP列表查询输出
 */
public class CommAppSeachOutput {
    /**
     * ID
     */
    private Long id;
    /**
     * 缩略图
     */
    private String minImg;
    /**
     * SKU(商品ID)
     */
    private String sku;
    /**
     * 商品条码
     */
    private String code69;
    /**
     * 商品商家编码
     */
    private String code;
    /**
     * 商品产地
     */
    private String originPlace;
    /**
     * 商品品牌名称
     */
    private String brandName;
    /**
     * 商品名称
     */
    private String commName;
    /**
     * 商品单位名称
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
     * 成本价
     */
    private BigDecimal unitPrice;
    /**
     * 库存
     */
    private Double inventory;
    /**
     *  创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createdAt;
    /**
     *  更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updatedAt;

    /**
     *  是否失效，0-失效，1-正常
     */
    private int invalidStatus;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMinImg() {
        return minImg;
    }

    public void setMinImg(String minImg) {
        this.minImg = minImg;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public String getOriginPlace() {
        return originPlace;
    }

    public void setOriginPlace(String originPlace) {
        this.originPlace = originPlace;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
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

    public Double getInventory() {
        return inventory;
    }

    public void setInventory(Double inventory) {
        this.inventory = inventory;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getInvalidStatus() {
        return invalidStatus;
    }

    public void setInvalidStatus(int invalidStatus) {
        this.invalidStatus = invalidStatus;
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
}
