package so.sao.shop.supplier.pojo.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import so.sao.shop.supplier.domain.FreightRules;
import so.sao.shop.supplier.pojo.vo.CommImgeVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品对象 出参
 * 用于商品新增修改 使用
 * Created by QuJunLong on 2017/7/19.
 */
public class CommodityOutput {

    /**
     *ID
     */
    private Long id;
    /**
     *ID
     */
    private Long scaId;
    /**
     * 供应商ID

     */
    private Long supplierId;
    /**
     * 供应商名称
     */
    private String providerName;
    /**
     * 供应商合同所在市
     */
    private String contractCity;
    /**
     * 销量
     */
    private Integer salesNumber;
    /**
     * 品牌
     */
    private String brandName;
    /**
     * 一级类型Id
     */
    private Long categoryOneId;
    /**
     * 一级类型Name
     */
    private String categoryOneName;
    /**
     * 二级类型Id
     */
    private Long categoryTwoId;
    /**
     * 二级类型Name
     */
    private String categoryTwoName;
    /**
     * 三级类型Id
     */
    private Long categoryThreeId;
    /**
     * 三级类型Name
     */
    private String categoryThreeName;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 标签ID
     */
    private Long tagId;
    /**
     * 标签名称
     */
    private String tagName;
    /**
     * 商品产地
     */
    private String originPlace;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 上市时间
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date marketTime;
    /**
     * 商品描述
     */
    private String remark;
    /**
     * 商品介绍
     */
    private String description;
    /**
     * 商家编码
     */
    private String code;
    /**
     * 商品编码
     */
    private String code69;
    /**
     * 计量规格ID
     */
    private Long measureSpecId;
    /**
     * 计量规格名称
     */
    private String measureSpecName;
    /**
     * 规格值
     */
    private String ruleVal;
    /**
     * 计量单位ID
     */
    private Long unitId;
    /**
     * 计量单位名称
     */
    private String unitName;
    /**
     * 库存
     */
    private Double inventory;
    /**
     * 缩略图
     */
    private String minImg;
    /**
     * 市场价
     */
    private BigDecimal price;
    /**
     * 售价
     */
    private BigDecimal unitPrice;
    /**
     * 商品状态
     */
    private int status;
    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createdAt;
    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updatedAt;

    /**
     * 是否失效 0失效，1正常
     */
    private int invalidStatus;


    /**
     *  最小起订量
     */
    private int minOrderQuantity;
    /**
     * 图片集合
     */
    private List<CommImgeVo> imgeList;

    /**
     * 审核意见
     */
    private String advice;

    /**
     * 商品运费规则
     */
    private List<FreightRules> freightRulesList;

    /**
     * 库存下限
     */
    private Long inventoryMinimum;

    /**
     * 库存状态：0代表库存正常，1代表下限预警
     */
    private Integer inventoryStatus;

    /**
     * 生产日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date productionDate;

    /**
     * 计量规格数值
     */
    private Integer measureSpecVal;

    /**
     *有效期
     */
    private Integer guaranteePeriod;

    /**
     * 有效期单位
     */
    private String guaranteePeriodUnit;

    /**
     *箱规id
     */
    private Long cartonId;

    /**
     * 箱规单位
     */
    private String cartonName;

    /**
     * 箱规数值
     */
    private Integer cartonVal;

    /**
     *是否超出配送范围  1超出配送范围,0没有超出
     */
    private Integer OutOfDeliveryRange;

    /**
     * 供应商发票状态  1:支持,0:不支持
     */
    private int isOpen;
    /**
     * 增值税普通发票状态  1:支持,0:不支持
     */
    private int plainInvoice;
    /**
     * 增值税专用发票状态   1:支持,0:不支持
     */
    private int specialInvoice;



    public Long getInventoryMinimum() {
        return inventoryMinimum;
    }

    public void setInventoryMinimum(Long inventoryMinimum) {
        this.inventoryMinimum = inventoryMinimum;
    }

    public Integer getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(Integer inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public Integer getMeasureSpecVal() {
        return measureSpecVal;
    }

    public void setMeasureSpecVal(Integer measureSpecVal) {
        this.measureSpecVal = measureSpecVal;
    }

    public Integer getGuaranteePeriod() {
        return guaranteePeriod;
    }

    public void setGuaranteePeriod(Integer guaranteePeriod) {
        this.guaranteePeriod = guaranteePeriod;
    }

    public String getGuaranteePeriodUnit() {
        return guaranteePeriodUnit;
    }

    public void setGuaranteePeriodUnit(String guaranteePeriodUnit) {
        this.guaranteePeriodUnit = guaranteePeriodUnit;
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

    public Integer getCartonVal() {
        return cartonVal;
    }

    public void setCartonVal(Integer cartonVal) {
        this.cartonVal = cartonVal;
    }

    public List<FreightRules> getFreightRulesList() {
        return freightRulesList;
    }

    public void setFreightRulesList(List<FreightRules> freightRulesList) {
        this.freightRulesList = freightRulesList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getContractCity() {
        return contractCity;
    }

    public void setContractCity(String contractCity) {
        this.contractCity = contractCity;
    }

    public Integer getSalesNumber() {
        return salesNumber;
    }

    public void setSalesNumber(Integer salesNumber) {
        this.salesNumber = salesNumber;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
    public Long getCategoryOneId() {
        return categoryOneId;
    }

    public void setCategoryOneId(Long categoryOneId) {
        this.categoryOneId = categoryOneId;
    }

    public Long getCategoryTwoId() {
        return categoryTwoId;
    }

    public void setCategoryTwoId(Long categoryTwoId) {
        this.categoryTwoId = categoryTwoId;
    }

    public Long getCategoryThreeId() {
        return categoryThreeId;
    }

    public void setCategoryThreeId(Long categoryThreeId) {
        this.categoryThreeId = categoryThreeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getRuleVal() {
        return ruleVal;
    }

    public void setRuleVal(String ruleVal) {
        this.ruleVal = ruleVal;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public String getCategoryOneName() {
        return categoryOneName;
    }

    public void setCategoryOneName(String categoryOneName) {
        this.categoryOneName = categoryOneName;
    }

    public String getCategoryTwoName() {
        return categoryTwoName;
    }

    public void setCategoryTwoName(String categoryTwoName) {
        this.categoryTwoName = categoryTwoName;
    }

    public String getCategoryThreeName() {
        return categoryThreeName;
    }

    public void setCategoryThreeName(String categoryThreeName) {
        this.categoryThreeName = categoryThreeName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<CommImgeVo> getImgeList() {
        return imgeList;
    }

    public void setImgeList(List<CommImgeVo> imgeList) {
        this.imgeList = imgeList;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getMeasureSpecName() {
        return measureSpecName;
    }

    public void setMeasureSpecName(String measureSpecName) {
        this.measureSpecName = measureSpecName;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getOriginPlace() {
        return originPlace;
    }

    public void setOriginPlace(String originPlace) {
        this.originPlace = originPlace;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getMarketTime() {
        return marketTime;
    }

    public void setMarketTime(Date marketTime) {
        this.marketTime = marketTime;
    }

    public Long getMeasureSpecId() {
        return measureSpecId;
    }

    public void setMeasureSpecId(Long measureSpecId) {
        this.measureSpecId = measureSpecId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public int getInvalidStatus() {
        return invalidStatus;
    }

    public void setInvalidStatus(int invalidStatus) {
        this.invalidStatus = invalidStatus;
    }

    public int getMinOrderQuantity() {
        return minOrderQuantity;
    }

    public void setMinOrderQuantity(int minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public Long getScaId() {
        return scaId;
    }

    public void setScaId(Long scaId) {
        this.scaId = scaId;
    }

    public Integer getOutOfDeliveryRange() {
        return OutOfDeliveryRange;
    }

    public void setOutOfDeliveryRange(Integer outOfDeliveryRange) {
        OutOfDeliveryRange = outOfDeliveryRange;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public int getPlainInvoice() {
        return plainInvoice;
    }

    public void setPlainInvoice(int plainInvoice) {
        this.plainInvoice = plainInvoice;
    }

    public int getSpecialInvoice() {
        return specialInvoice;
    }

    public void setSpecialInvoice(int specialInvoice) {
        this.specialInvoice = specialInvoice;
    }
}
