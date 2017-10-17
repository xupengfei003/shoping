package so.sao.shop.supplier.pojo.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import so.sao.shop.supplier.pojo.BaseResult;

import java.util.Date;

/**
 * Created by acer on 2017/7/25.
 */
public class CommodityImportOutput extends BaseResult {
    /**
     * Excel 行号
     */
    private  int rowNum;
    /**
     * 商品编码
     */
    private String code69;
    /**
     * 商家编码
     */
    private String sjcode;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 规格名称
     */
    private String measureSpecName;
    /**
     * 规格值
     */
    private String ruleVal;

    /**
     * 计量单位
     */
    private String unit;
    /**
     * 库存
     */
    private Long inventory;
    /**
     * 商品标签名称
     */
    private String tagName;
    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 商品产地
     */
    private String originPlace;

    /**
     *
     * 最小起订量
     */
    private  int minOrderQuantity;

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public String getSjcode() {
        return sjcode;
    }

    public String getBrand() {
        return brand;
    }

    public String getName() {
        return name;
    }

    public String getRuleVal() {
        return ruleVal;
    }

    public String getUnit() {
        return unit;
    }

    public Long getInventory() {
        return inventory;
    }

    public String getCode69() {
        return code69;
    }

    public void setSjcode(String sjcode) {
        this.sjcode = sjcode;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasureSpecName() {
        return measureSpecName;
    }

    public void setMeasureSpecName(String measureSpecName) {
        this.measureSpecName = measureSpecName;
    }

    public void setRuleVal(String ruleVal) {
        this.ruleVal = ruleVal;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setInventory(Long inventory) {
        this.inventory = inventory;
    }

    public void setCode69(String code69) {
        this.code69 = code69;
    }

    public String getTagName() {return tagName;}

    public void setTagName(String tagName) {this.tagName = tagName;}

    public String getCompanyName() {return companyName;}

    public void setCompanyName(String companyName) {this.companyName = companyName;}

    public String getOriginPlace() {return originPlace;}

    public void setOriginPlace(String originPlace) {this.originPlace = originPlace;}

    public int getMinOrderQuantity() {return minOrderQuantity;}

    public void setMinOrderQuantity(int minOrderQuantity) {this.minOrderQuantity = minOrderQuantity;}
}
