package so.sao.shop.supplier.pojo.input;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class AppCartItemInput {


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
     * 数量
     */
    @Min(value=1,message = "不能小于1")
    @NotNull(message = "数量不能为空")
    private Integer count;


    public String getCommodityProperties() {
        return commodityProperties;
    }

    public void setCommodityProperties(String commodityProperties) {
        this.commodityProperties = commodityProperties;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AppCartItemInput{" +
                "userId=" + userId +
                ", supplierId=" + supplierId +
                ", commodityId=" + commodityId +
                ", commodityPrice=" + commodityPrice +
                ", commodityName='" + commodityName + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", commodityPic='" + commodityPic + '\'' +
                ", commodityProperties='" + commodityProperties + '\'' +
                ", count=" + count +
                '}';
    }
}