package so.sao.shop.supplier.domain;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.codehaus.jackson.map.ObjectMapper;
import so.sao.shop.supplier.pojo.output.CommodityOutput;

public class AppCartItem {

    /**
     * 记录ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 商品id
     */
    private Long commodityId;
    /**
     * 商品sku
     */
    private String sku;
    /**
     * 价格
     */
    private BigDecimal commodityPrice;

    /**
     * 商品名称
     */
    private String commodityName;
    /**
     * 商品图片路径
     */
    private String commodityPic;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 商品属性
     */
    private String commodityProperties;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createdAt;
    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updatedAt;
    /**
     * 商品库存数量
     */
    private Double inventory;

    /**
     * 计量规格ID
     */
    private Long measureSpecId;
    /**
     * 计量规格名称
     */
    private String measureSpecName;
    /**
     * 规格值
     */
    private String ruleVal;
    /**
     * 计量单位ID
     */
    private Long unitId;
    /**
     * 计量单位名称
     */
    private String unitName;




    /**
     * 供应商对应的商品
     */
    private SupplierCommodity supplierCommodity;
    /**
     * 商品
     */
    private Commodity commodity;
    /**
     * 用户
     */
    private User user;

    public String getCommodityProperties() {
        return commodityProperties;
    }

    public void setCommodityProperties(String commodityProperties) {
        //如果无法解析为json字符串则不存值
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readValue(commodityProperties, Map.class);
        } catch (IOException e) {
            return;
        }
        this.commodityProperties = commodityProperties;
    }

    public SupplierCommodity getSupplierCommodity() {
        return supplierCommodity;
    }

    public void setSupplierCommodity(SupplierCommodity supplierCommodity) {
        this.supplierCommodity = supplierCommodity;
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public BigDecimal getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(BigDecimal commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName == null ? null : commodityName.trim();
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }

    public String getCommodityPic() {
        return commodityPic;
    }

    public void setCommodityPic(String commodityPic) {
        this.commodityPic = commodityPic == null ? null : commodityPic.trim();
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Double getInventory() {
        return inventory;
    }

    public void setInventory(Double inventory) {
        this.inventory = inventory;
    }

    public Long getMeasureSpecId() {
        return measureSpecId;
    }

    public void setMeasureSpecId(Long measureSpecId) {
        this.measureSpecId = measureSpecId;
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

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public String toString() {
        return "AppCartItem{" +
                "id=" + id +
                ", userId=" + userId +
                ", supplierId=" + supplierId +
                ", supplierName='" + supplierName + '\'' +
                ", commodityId=" + commodityId +
                ", sku=" + sku +
                ", commodityPrice=" + commodityPrice +
                ", commodityName='" + commodityName + '\'' +
                ", commodityPic='" + commodityPic + '\'' +
                ", count=" + count +
                ", commodityProperties='" + commodityProperties + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", inventory=" + inventory +
                ", measureSpecId=" + measureSpecId +
                ", measureSpecName='" + measureSpecName + '\'' +
                ", ruleVal='" + ruleVal + '\'' +
                ", unitId=" + unitId +
                ", unitName='" + unitName + '\'' +
                ", supplierCommodity=" + supplierCommodity +
                ", commodity=" + commodity +
                ", user=" + user +
                '}';
    }

    /**
     * 复制属性
     * @param supplierCommodity
     */
    public void copySupplierCommodity(SupplierCommodity supplierCommodity) {
        if(supplierCommodity == null){
            return ;
        }
        this.setCommodityId(supplierCommodity.getId());
        this.setCommodityName(supplierCommodity.getRemark());
        this.setCommodityPic(supplierCommodity.getMinImg());
        this.setCommodityPrice(supplierCommodity.getUnitPrice());
        this.setSku(supplierCommodity.getSku());
        this.setSupplierId(supplierCommodity.getSupplierId());
    }

    /**
     * 复制属性
     * @param commodityOutput
     */
    public void copyCommodityOutput(CommodityOutput commodityOutput) {
        if(supplierCommodity == null){
            return ;
        }
        this.setMeasureSpecId(commodityOutput.getMeasureSpecId());
        this.setMeasureSpecName(commodityOutput.getMeasureSpecName());
        this.setRuleVal(commodityOutput.getRuleVal());
        this.setUnitId(commodityOutput.getUnitId());
        this.setUnitName(commodityOutput.getUnitName());

    }
}