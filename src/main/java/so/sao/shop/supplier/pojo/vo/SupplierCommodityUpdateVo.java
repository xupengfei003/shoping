package so.sao.shop.supplier.pojo.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class SupplierCommodityUpdateVo {

    /**
     * ID
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 商家编码
     */
    @NotBlank(message = "商品商家编码不能为空")
    @Length(max = 50,message = "商品商家编码长度不能大于50")
    private String code;

    /**
     * 计量规格ID
     */
    @NotNull(message = "计量规格不能为空")
    private Long measureSpecId;

    /**
     * 规格值
     */
    @NotBlank(message = "规格值不能为空")
    @Length(max = 256,message = "规格值长度不能大于256")
    private String ruleVal;

    /**
     * 包装单位ID
     */
    @NotNull(message = "包装单位不能为空")
    private Long unitId;

    /**
     * 缩略图
     */
    @NotBlank(message = "缩略图不能为空")
    @Length(max = 500,message = "缩略图长度不能大于500")
    private String minImg;

    /**
     * 市场价
     */
    @NotNull(message = "市场价不能为空")
    @Min(value=0)
    @Max(value = 99999999)
    private BigDecimal price;

    /**
     * 成本价
     */
    @NotNull(message = "成本价不能为空")
    @Min(value=0)
    @Max(value = 99999999)
    private BigDecimal unitPrice;

    /**
     *
     * 最小起订量
     */
    @Min(value=0)
    @Max(value = 999)
    private  int minOrderQuantity;

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
    private Long cartonVal;

    /**
     * 计量规格数值
     */
    @NotNull(message = "计量规格数值不能为空")
    @Min(value = 0)
    @Max(value = 99999999)
    private Long measureSpecVal;

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
    private List<CommImgeUpdateVo> imgeList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public List<CommImgeUpdateVo> getImgeList() {
        return imgeList;
    }

    public void setImgeList(List<CommImgeUpdateVo> imgeList) {
        this.imgeList = imgeList;
    }

    public int getMinOrderQuantity() {
        return minOrderQuantity;
    }

    public void setMinOrderQuantity(int minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

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

    public Long getCartonVal() {
        return cartonVal;
    }

    public void setCartonVal(Long cartonVal) {
        this.cartonVal = cartonVal;
    }

    public Long getMeasureSpecVal() {
        return measureSpecVal;
    }

    public void setMeasureSpecVal(Long measureSpecVal) {
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
