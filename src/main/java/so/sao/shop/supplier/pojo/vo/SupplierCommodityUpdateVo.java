package so.sao.shop.supplier.pojo.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
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
     * 库存
     */
    @NotNull(message = "库存不能为空")
    @Min(value=0)
    @Max(value = 999999999)
    private Double inventory;

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

    public Double getInventory() {
        return inventory;
    }

    public void setInventory(Double inventory) {
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

    public List<CommImgeUpdateVo> getImgeList() {
        return imgeList;
    }

    public void setImgeList(List<CommImgeUpdateVo> imgeList) {
        this.imgeList = imgeList;
    }
}
