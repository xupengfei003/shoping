package so.sao.shop.supplier.pojo.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 供应商商品关系表
 * Created by QuJunLong on 2017/7/18.
 */
public class SupplierCommodityVo {

    /**
     * 商品条码
     */
    @Length(max = 20,message = "商品条码长度不能大于20")
    private String code69;

    /**
     * 计量规格ID
     */
    @NotNull(message = "计量规格不能为空")
    private Long measureSpecId;

    /**
     * 计量规格
     */
    private String measureSpecName;

    /**
     * 规格值
     */
    @NotBlank(message = "规格值不能为空")
    @Length(max = 256,message = "规格值长度不能大于256")
    private String ruleVal;

    /**
     * 库存单位ID
     */
    @NotNull(message = "库存单位不能为空")
    private Long unitId;

    /**
     * 库存单位
     */
    private String unitName;

    /**
     * 库存
     */
    @NotNull(message = "库存不能为空")
    @Min(value = 0)
    @Max(value = 999999999)
    private int inventory;

    /**
     * 缩略图
     */
    @NotBlank(message = "缩略图不能为空")
    @Length(max = 500,message = "缩略图长度不能大于500")
    private String minImg;

    /**
     * 批发价
     */
    @NotNull(message = "批发价不能为空")
    @Min(value = 0)
    @Max(value = 99999999)
    private BigDecimal price;

    /**
     * 供货价
     */
    @NotNull(message = "供货价不能为空")
    @Min(value = 0)
    @Max(value = 99999999)
    private BigDecimal unitPrice;

    /**
     *
     * 最小起订量
     */
    @NotNull(message = "最小起订量不能为空")
    @Min(value = 0)
    @Max(value = 999)
    private int minOrderQuantity;

    /**
     * 箱规单位ID
     */
    @NotNull(message = "箱规单位不能为空")
    private Long cartonId;

    /**
     * 箱规单位
     */
    private String cartonName;

    /**
     * 箱规数值
     */
    @NotNull(message = "箱规数值不能为空")
    @Min(value = 0)
    @Max(value = 99999999)
    private int cartonVal;

    /**
     * 计量规格数值
     */
    @NotNull(message = "箱规数值不能为空")
    @Min(value = 0)
    @Max(value = 99999999)
    private int measureSpecVal;

    /**
     * 生产日期
     */
    @NotNull(message = "生产日期不能为空")
    private Date productionDate;

    /**
     * 有效期
     */
    @NotNull(message = "有效期不能为空")
    @Min(value = 0)
    @Max(value = 99999999)
    private int guaranteePeriod;

    /**
     * 有效期单位
     */
    @NotBlank(message = "有效期单位不能为空")
    @Length(max = 20,message = "有效期单位长度不能大于20")
    private String guaranteePeriodUnit;

    /**
     * 图片集合
     */
    @NotEmpty(message = "图片不能为空")
    @Valid
    private List<CommImgeVo> imgeList;

    public String getCode69() {
        return code69;
    }

    public void setCode69(String code69) {
        this.code69 = code69;
    }

    public Long getMeasureSpecId() {
        return measureSpecId;
    }

    public void setMeasureSpecId(Long measureSpecId) {
        this.measureSpecId = measureSpecId;
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

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public String getMinImg() {
        return minImg;
    }

    public void setMinImg(String minImg) {
        this.minImg = minImg;
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

    public List<CommImgeVo> getImgeList() {
        return imgeList;
    }

    public void setImgeList(List<CommImgeVo> imgeList) {
        this.imgeList = imgeList;
    }

    public String getMeasureSpecName() {
        return measureSpecName;
    }

    public void setMeasureSpecName(String measureSpecName) {
        this.measureSpecName = measureSpecName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getMinOrderQuantity() {return minOrderQuantity;}

    public void setMinOrderQuantity(int minOrderQuantity) {this.minOrderQuantity = minOrderQuantity;}

    public Long getCartonId() {
        return cartonId;
    }

    public void setCartonId(Long cartonId) {
        this.cartonId = cartonId;
    }

    public String getCartonName() {
        return cartonName;
    }

    public void setCartonName(String cartonName) {
        this.cartonName = cartonName;
    }

    public int getCartonVal() {
        return cartonVal;
    }

    public void setCartonVal(int cartonVal) {
        this.cartonVal = cartonVal;
    }

    public int getMeasureSpecVal() {
        return measureSpecVal;
    }

    public void setMeasureSpecVal(int measureSpecVal) {
        this.measureSpecVal = measureSpecVal;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public int getGuaranteePeriod() {
        return guaranteePeriod;
    }

    public void setGuaranteePeriod(int guaranteePeriod) {
        this.guaranteePeriod = guaranteePeriod;
    }

    public String getGuaranteePeriodUnit() {
        return guaranteePeriodUnit;
    }

    public void setGuaranteePeriodUnit(String guaranteePeriodUnit) {
        this.guaranteePeriodUnit = guaranteePeriodUnit;
    }
}
