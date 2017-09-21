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

/**
 * 供应商商品关系表
 * Created by QuJunLong on 2017/7/18.
 */
public class SupplierCommodityVo {

    /**
     * 商家编码
     */
    @NotBlank(message = "商品商家编码不能为空")
    @Length(max = 50,message = "商品商家编码长度不能大于50")
    private String code;

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
     * 包装单位ID
     */
    @NotNull(message = "包装单位不能为空")
    private Long unitId;

    /**
     * 包装单位
     */
    private String unitName;

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
     *
     * 最小起订量
     */
    private  int minOrderQuantity;

    /**
     * 图片集合
     */
    @NotEmpty(message = "图片不能为空")
    @Valid
    private List<CommImgeVo> imgeList;

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
}
