package so.sao.shop.supplier.pojo.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import so.sao.shop.supplier.config.CommConstant;

import java.math.BigDecimal;
import java.util.Date;

public class HotCommodityOutput {

    /**
     * ID
     */
    private Long id;

    /**
     * SKU
     */
    private String sku;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 供应商名称
     */
    private String providerName;

    /**
     * 缩略图
     */
    private String minImg;

    /**
     * 商家编码
     */
    private String code;

    /**
     * 商品编码
     */
    private String code69;

    /**
     *合同注册地（市）
     */
    private String cityName;

    /**
     *商品品牌名称
     */
    private String commBrandName;

    /**
     * 商品名称
     */
    private String  commName;

    /**
     * 商品单位名称
     */
    private String commUnitName;

    /**
     * 计量规格名称
     */
    private String  commMeasureName;

    /**
     * 计量规格值
     */
    private String ruleVal;

    /**
     * 库存
     */
    private Double inventory;

    /**
     * 市场价
     */
    private BigDecimal price;

    /**
     * 商品状态
     * 已上架 待上架 已下架
     */
    private int status;

    /**
     * 商品状态名
     */
    private String statusName;

    /**
     * 顺序
     */
    private int  sort;

    /**
     *操作人
     */
    private String operator;

    /**
     *  更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updatedAt;

    /**
     *  创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createdAt;

    /**
     * 商品销量
     */
    private int  salesVolume;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getMinImg() {
        return minImg;
    }

    public void setMinImg(String minImg) {
        this.minImg = minImg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode69() {
        return code69;
    }

    public void setCode69(String code69) {
        this.code69 = code69;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCommBrandName() {
        return commBrandName;
    }

    public void setCommBrandName(String commBrandName) {
        this.commBrandName = commBrandName;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public String getCommUnitName() {
        return commUnitName;
    }

    public void setCommUnitName(String commUnitName) {
        this.commUnitName = commUnitName;
    }

    public String getCommMeasureName() {
        return commMeasureName;
    }

    public void setCommMeasureName(String commMeasureName) {
        this.commMeasureName = commMeasureName;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return this.statusName = CommConstant.getStatus(status);
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return
                id + "," +
                sku + "," +
                supplierId +"," +
                providerName + "," +
                minImg + "," +
                code + "," +
                code69 + "," +
                cityName + "," +
                commBrandName + "," +
                commName + "," +
                commUnitName + "," +
                commMeasureName + "," +
                ruleVal + "," +
                inventory +"," +
                price +","+
                status +"," +
                statusName +"," +
                sort +"," +
                operator + "," +
                updatedAt +"," +
                createdAt +"," +
                salesVolume;

    }
}
