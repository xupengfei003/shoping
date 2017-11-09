package so.sao.shop.supplier.pojo.input;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class HotCommoditySaveInput {

    private String scId;

    private Long supplierId;

    private String providerName;

    private String minImg;

    private String code;

    private String code69;

    private String cityName;

    private String commBrandName;

    private String  commName;

    private String commUnitName;

    private String  commMeasureName;

    private String ruleVal;

    @Min(value = 0, message = "库存不能小于0")
    private Double inventory;

    @Min(value = 0, message = "市场价不能小于0")
    private BigDecimal price;

    private int status;

    private int  sort;

    private String operator;

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
