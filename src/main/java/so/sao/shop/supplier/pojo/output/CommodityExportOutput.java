package so.sao.shop.supplier.pojo.output;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import so.sao.shop.supplier.config.CommConstant;
import so.sao.shop.supplier.util.DateUtil;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by renle on 2017/7/20.
 */
public class CommodityExportOutput {
    /**
     * 序号
     */
    private  int num;
    /**
     * 商品id
     */
    private Long id ;
    /**
     * 商品sku
     */
    private String sku;
    /**
     * 商品编码
     */
    private String code69;
    /**
     * 商品标签名称
     */
    private String tagName;

    /**
     * 商品分类一级名称
     */
    private  String   categoryOneName;
    /**
     * 商品分类二级名称
     */
    private  String   categoryTwoName;
    /**
     * 商品分类三级名称
     */
    private  String   categoryThreeName;

    /**
     * 品牌
     */
    private String brandName;
    /**
     * 商品名称
     */
    private String commName;
    /**
     * 商家编码
     */
    private String supplierCode;
    /**
     * 库存单位
     */
    private String unitName;
    /**
     * 计量规格名称
     */
    private String measureSpecName;
    /**
     * 规格值
     */
    private String ruleValue;
    /**
     * 箱规单位
     */
    private String cartonName;

    /**
     * 箱规数值
     */
    private Long cartonVal;
    /**
     * 市场价
     */
    private BigDecimal price;
    /**
     * 售价
     */
    private BigDecimal unitPrice;
    /**
     * 生产日期
     */

    private String  productionDate;

    /**
     * 有效期
     */

    private int guaranteePeriod;

    /**
     * 有效期单位
     */
    private String guaranteePeriodUnit;

    /**
     * 计量规格数值
     */
    private String measureSpecVal;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 商品产地
     */
    private String originPlace;
    /**
     * 供应商id
     */
    private Long supplierId;
    /**
     * 商品简介
     */
    private String remark;
    /**
     * 图片
     */
    private String img;

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
     * 商品状态名称
     */
    private String statusName;

    /**
     *  创建时间
     */
    private String createdAt;
    /**
     *  更新时间
     */
    private String updatedAt;

    /**
     *
     * 最小起订量
     */
    private  int minOrderQuantity;
    /**
     * 是否失效
     */
    private  int invalidStatus;

    private  String invalidStatusName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCode69() {
        return code69;
    }

    public void setCode69(String code69) {
        this.code69 = code69;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getMeasureSpecName() {
        return measureSpecName;
    }

    public void setMeasureSpecName(String measureSpecName) {
        this.measureSpecName = measureSpecName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOriginPlace() {
        return originPlace;
    }

    public void setOriginPlace(String originPlace) {
        this.originPlace = originPlace;
    }

    public String getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price == null ? new BigDecimal(0.0) : price;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice == null ? new BigDecimal(0.0) : unitPrice;
    }

    public Double getInventory() {
        return inventory;
    }

    public void setInventory(Double inventory) {
        this.inventory = inventory == null ? 0.0 : inventory;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = DateUtil.subStringByIndex(createdAt , 19);
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = DateUtil.subStringByIndex(updatedAt , 19);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.statusName = CommConstant.getStatus(status);
    }

    public int getMinOrderQuantity() {return minOrderQuantity;}

    public void setMinOrderQuantity(int minOrderQuantity) {this.minOrderQuantity = minOrderQuantity;}

    public int getInvalidStatus() {return invalidStatus;}

    public void setInvalidStatus(int invalidStatus) {this.invalidStatusName =  CommConstant.getinvalidStatus(invalidStatus);}

    public int getNum() {return num;}

    public void setNum(int num) {this.num = num;}

    public String getCategoryOneName() {return categoryOneName;}

    public void setCategoryOneName(String categoryOneName) {this.categoryOneName = categoryOneName;}

    public String getCategoryTwoName() {return categoryTwoName;}

    public void setCategoryTwoName(String categoryTwoName) {this.categoryTwoName = categoryTwoName;}

    public String getCategoryThreeName() {return categoryThreeName;}

    public void setCategoryThreeName(String categoryThreeName) {this.categoryThreeName = categoryThreeName;}

    public String getCartonName() {return cartonName;}

    public void setCartonName(String cartonName) {this.cartonName = cartonName;}

    public Long getCartonVal() {return cartonVal;}

    public void setCartonVal(Long cartonVal) {this.cartonVal = cartonVal;}

    public String getProductionDate() {return productionDate;}

    public void setProductionDate(String productionDate) {this.productionDate = productionDate;}

    public int getGuaranteePeriod() {return guaranteePeriod;}

    public void setGuaranteePeriod(int guaranteePeriod) {this.guaranteePeriod = guaranteePeriod;}

    public String getGuaranteePeriodUnit() {return guaranteePeriodUnit;}

    public void setGuaranteePeriodUnit(String guaranteePeriodUnit) {this.guaranteePeriodUnit = guaranteePeriodUnit;}

    public String getMeasureSpecVal() {return measureSpecVal;}

    public void setMeasureSpecVal(String measureSpecVal) {this.measureSpecVal = measureSpecVal;}

    public Long getSupplierId() {return supplierId;}

    public void setSupplierId(Long supplierId) {this.supplierId = supplierId;}

    public String getRemark() {return remark;}

    public void setRemark(String remark) {this.remark = remark;}

    public String getImg() {return img;}

    public void setImg(String img) {this.img = img;}

    @Override
    public String toString() {

        return
                num +
                        "," +code69 +
                        "," +supplierCode +
                        "," +commName +
                        "," +img +
                        "," + brandName +
                        "," + categoryOneName +
                        "," + categoryTwoName +
                        "," + categoryThreeName +
                        "," + originPlace +
                        "," + unitName +
                        "," + measureSpecName +
                        "," +measureSpecVal+measureSpecName+"/"+  unitName+
                        "," +cartonVal+unitName+"/"+  cartonName+
                        ",￥" + price +
                        ",￥" + unitPrice +
                        "," + productionDate +
                        "," + guaranteePeriodData(guaranteePeriod,guaranteePeriodUnit) +
                        "," + minOrderQuantity+
                        "," + companyName +
                        "," + supplierId +
                        "," + tagName +
                        "," + remark  ;

    }
    /**
     * 天
     */
    public static final String  DAY ="天";
    /**
     * 月
     */
    public static final String  MONTH ="月";
    /**
     * 年
     */
    public static final String  YEEAR="年";
    public static int guaranteePeriodData(int guaranteePeriod,String guaranteePeriodUnit){
        int  st=guaranteePeriod;
        switch (guaranteePeriodUnit){
            case DAY:
                st=guaranteePeriod;
                break;
            case MONTH:
                st=guaranteePeriod*30;
                break;
            case YEEAR:
                st=guaranteePeriod*365;
                break;
        }
        return st;
    }
}
