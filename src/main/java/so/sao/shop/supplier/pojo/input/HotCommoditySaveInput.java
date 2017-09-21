package so.sao.shop.supplier.pojo.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;

public class HotCommoditySaveInput {

    @ApiModelProperty(value = "商品id")
    private String scId;

    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;

    @ApiModelProperty(value = "供应商名称")
    private String providerName;

    @ApiModelProperty(value = "缩略图")
    private String minImg;

    @ApiModelProperty(value = "商家编码")
    private String code;

    @ApiModelProperty(value = "商品编码")
    private String code69;

    @ApiModelProperty(value = "合同注册地（市）")
    private String cityName;

    @ApiModelProperty(value = "商品品牌名称")
    private String commBrandName;

    @ApiModelProperty(value = "商品名称")
    private String  commName;

    @ApiModelProperty(value = "商品单位名称")
    private String commUnitName;

    @ApiModelProperty(value = "计量规格名称")
    private String  commMeasureName;

    @ApiModelProperty(value = "计量规格值")
    private String ruleVal;

    @ApiModelProperty(value = "库存")
    @Min(value = 0, message = "库存不能小于0")
    private Double inventory;

    @ApiModelProperty(value = "市场价")
    @Min(value = 0, message = "市场价不能小于0")
    private BigDecimal price;

    @ApiModelProperty(value = "商品状态")
    private int status;

    @ApiModelProperty(value = "顺序")
    private int  sort;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "商品销量")
    @Min(value = 0, message = "商品销量不能小于0")
    private int  salesVolume;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }

    @Override
    public String toString() {
        return
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
                price +"," +
                status +"," +
                sort +"," +
                operator + "," +
                salesVolume;

    }
}
