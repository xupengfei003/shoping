package so.sao.shop.supplier.pojo.output;

import java.math.BigInteger;

/**
 * 库存输出数据结构
 * @author gxy on 2017/10/13.
 */
public class CommInventoryOutput {
    /**
     * 商品ID
     */
    private Long id;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 商品条码
     */
    private String code69;

    /**
     * 商品品牌名
     */
    private String brandName;

    /**
     * 商品名称
     */
    private String commName;

    /**
     * 库存单位
     */
    private String unitName;

    /**
     * 规格值
     */
    private String ruleVal;

    /**
     * 库存下限
     */
    private Long inventoryMinimum;

    /**
     * 库存量
     */
    private Long inventory;

    /**
     * 预警状态 0代表库存正常，1代表下限预警
     */
    private Integer inventoryStatus;

    /**
     * 计量规格
     */
    private String measureSpecName;

    /**
     * 箱规单位
     */
    private String cartonName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getRuleVal() {
        return ruleVal;
    }

    public void setRuleVal(String ruleVal) {
        this.ruleVal = ruleVal;
    }

    public Long getInventoryMinimum() {
        return inventoryMinimum;
    }

    public void setInventoryMinimum(Long inventoryMinimum) {
        this.inventoryMinimum = inventoryMinimum;
    }

    public Long getInventory() {
        return inventory;
    }

    public void setInventory(Long inventory) {
        this.inventory = inventory;
    }

    public Integer getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(Integer inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public String getMeasureSpecName() {
        return measureSpecName;
    }

    public void setMeasureSpecName(String measureSpecName) {
        this.measureSpecName = measureSpecName;
    }

    public String getCartonName() {
        return cartonName;
    }

    public void setCartonName(String cartonName) {
        this.cartonName = cartonName;
    }
}
