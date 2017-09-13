package so.sao.shop.supplier.pojo.output;

import java.math.BigDecimal;
import so.sao.shop.supplier.config.CommConstant;
import so.sao.shop.supplier.util.DateUtil;

/**
 * Created by renle on 2017/7/20.
 */
public class CommodityExportOutput {
    /**
     * 商品id
     */
    private Long id ;
    /**
     * 商品sku
     */
    private String sku;
    /**
     * 商品编码
     */
    private String code69;
    /**
     * 商品标签名称
     */
    private String tagName;

    /**
     * 品牌
     */
    private String brandName;
    /**
     * 商品名称
     */
    private String commName;
    /**
     * 商家编码
     */
    private String supplierCode;
    /**
     * 计量单位
     */
    private String unitName;
    /**
     * 计量规格名称
     */
    private String measureSpecName;
    /**
     * 规格值
     */
    private String ruleValue;
    /**
     * 市场价
     */
    private BigDecimal price;
    /**
     * 售价
     */
    private BigDecimal unitPrice;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 上市时间
     */
    private String marketTime;
    /**
     * 商品产地
     */
    private String originPlace;

    /**
     * 库存
     */
    private Double inventory;
    /**
     * 商品状态
     * 已上架 待上架 已下架
     */
    private int status;

    /**
     * 商品状态名称
     */
    private String statusName;

    /**
     *  创建时间
     */
    private String createdAt;
    /**
     *  更新时间
     */
    private String updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
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

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMarketTime() {
        return marketTime;
    }

    public void setMarketTime(String marketTime) {
        this.marketTime = DateUtil.subStringByIndex(marketTime , 10);
    }

    public String getOriginPlace() {
        return originPlace;
    }

    public void setOriginPlace(String originPlace) {
        this.originPlace = originPlace;
    }

    public String getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price == null ? new BigDecimal(0.0) : price;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice == null ? new BigDecimal(0.0) : unitPrice;
    }

    public Double getInventory() {
        return inventory;
    }

    public void setInventory(Double inventory) {
        this.inventory = inventory == null ? 0.0 : inventory;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = DateUtil.subStringByIndex(createdAt , 19);
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = DateUtil.subStringByIndex(updatedAt , 19);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.statusName = CommConstant.getStatus(status);
    }

    @Override
    public String toString() {
        return
                sku +
            "," + code69 +
            "," + brandName +
            "," + tagName +
            "," + commName  +
            "," + supplierCode +
            "," + unitName +
            "," + measureSpecName +
            "," + ruleValue +
            "," + price +
            "," + unitPrice +
            "," + inventory +
            "," + statusName +
            "," + createdAt +
            "," + updatedAt +
            "," + companyName +
            "," + originPlace +
            "," + marketTime ;

    }
}
