package so.sao.shop.supplier.pojo.input;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CartItemInput {

    /**
     * 记录ID
     */

    private Long id;
    /**
     * 用户ID
     */
    @NotNull
    private Long userId;
    /**
     * 供应商ID
     */
    @NotNull
    private Long supplierId;
    /**
     * 商品id
     */
    @NotNull
    private Long commodityId;
    /**
     * 价格
     */
    @DecimalMin(value="0.01", message = "价格必须大于0")
    private BigDecimal commodityPrice;

    /**
     * 商品名称
     */
    private String commodityName;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 商品图片路径
     */
    private String commodityPic;
    /**
     * 商品属性
     */
    private String commodityProperties;

    /**
     * 创建时间
     */
    private Long createdAt;
    /**
     * 更新时间
     */
    private Long updatedAt;
    /**
     * 数量
     */
    @Min(value=1,message = "不能小于1")
    private Integer count;


    public String getCommodityProperties() {
        return commodityProperties;
    }

    public void setCommodityProperties(String commodityProperties) {
        this.commodityProperties = commodityProperties;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}