package so.sao.shop.supplier.pojo.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import so.sao.shop.supplier.domain.AppCartItem;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wyy on 2017/9/12.
 */
public class AppCartItemOutSub {

    /**
     * 记录ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
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
     * 是否还有库存
     */
    private boolean remaining;

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

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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
        this.commodityName = commodityName;
    }

    public String getCommodityPic() {
        return commodityPic;
    }

    public void setCommodityPic(String commodityPic) {
        this.commodityPic = commodityPic;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCommodityProperties() {
        return commodityProperties;
    }

    public void setCommodityProperties(String commodityProperties) {
        this.commodityProperties = commodityProperties;
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

    public boolean getRemaining() {
        return remaining;
    }

    public void setRemaining(boolean remaining) {
        this.remaining = remaining;
    }

    @Override
    public String toString() {
        return "AppCartItemOutSub{" +
                "id=" + id +
                ", userId=" + userId +
                ", commodityId=" + commodityId +
                ", sku='" + sku + '\'' +
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
                ", remaining=" + remaining +
                '}';
    }

    public void copyAppCartItem(AppCartItem appCartItem){

        this.setCommodityId(appCartItem.getCommodityId());
        this.setCommodityName(appCartItem.getCommodityName());
        this.setCommodityPic(appCartItem.getCommodityPic());
        this.setCommodityPrice(appCartItem.getCommodityPrice());
        this.setCommodityProperties(appCartItem.getCommodityProperties());
        this.setCount(appCartItem.getCount());
        this.setCreatedAt(appCartItem.getCreatedAt());
        this.setId(appCartItem.getId());
        this.setInventory(appCartItem.getInventory());
        this.setMeasureSpecId(appCartItem.getMeasureSpecId());
        this.setMeasureSpecName(appCartItem.getMeasureSpecName());
        this.setRemaining(appCartItem.getRemaining());
        this.setInventory(appCartItem.getInventory());
        this.setRuleVal(appCartItem.getRuleVal());
        this.setSku(appCartItem.getSku());
        this.setUnitId(appCartItem.getUnitId());
        this.setUnitName(appCartItem.getUnitName());
        this.setUpdatedAt(appCartItem.getUpdatedAt());
        this.setUserId(appCartItem.getUserId());

    }



}
