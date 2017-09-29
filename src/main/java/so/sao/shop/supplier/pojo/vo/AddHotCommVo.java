package so.sao.shop.supplier.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by acer on 2017/9/19.
 */
public class AddHotCommVo  {
    /**
     * ID
     */
    private Long id;

    /**
     * 商品id
     */
    private String scId;

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
     * 商品状态
     * 已上架 待上架 已下架
     */
    private int status;

    /**
     * 市场价
     */
    private BigDecimal price;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScId() {
        return scId;
    }

    public void setScId(String scId) {
        this.scId = scId;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    @Override
    public String toString() {
        return
                id + "," +
                scId + "," +
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
                price + "," +
                status +"," +
                updatedAt +"," +
                createdAt ;
    }
}
