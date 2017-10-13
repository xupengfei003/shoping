package so.sao.shop.supplier.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liugang on 2017/7/20.
 * 商品查询响应体
 */
public class SuppCommSearchVo {
    /**
     * ID
     */
    private Long id;
    /**
     * 缩略图
     */
    private String minImg;
    /**
     * SKU(商品ID)
     */
//    private String sku;
    /**
     * sc表的id
     */
    private Long commId;
    /**
     * 供应商id
     */
//    private Long supplierId;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 商品编码
     */
    private String code69;
    /**
     * 商家编码
     */
//    private String code;
    /**
     * 商品品牌名称
     */
    private String brandName;
    /**
     * 商品名称
     */
    private String commName;
    /**
     * 商品单位名称
     */
    private String unitName;
    /**
     * 计量规格名称
     */
    private String measureSpecName;
    /**
     * 规格值
     */
    private String ruleVal;
    /**
     * 库存
     */
    private String inventory;
    /**
     * 商品状态
     * 0:待上架 ，1:已废弃 ，2：上架 3：下架
     */
    private String status;
    /**
     * 商品状态数值
     */
    private int statusNum;

    /**
     * 商品是否失效
     * 0 失效  1正常
     */
    private int invalidStatus;

    /**
     * 市场价
     */
    private String price;
    /**
     * 售价
     */
    private String unitPrice;
    /**
     * 最小起订量
     */

    private Integer minOrderQuantity;

    /**
     * 审核意见
     */
    private String auditOpinion;
    /**
     * 审核结果
     */
    private Integer auditResult;

    /**
     * 创建时间
     */
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
//    private Date createdAt;
    /**
     *  更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updatedAt;

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Long getCommId() {
        return commId;
    }

    public void setCommId(Long commId) {
        this.commId = commId;
    }

    public int getInvalidStatus() {
        return invalidStatus;
    }

    public void setInvalidStatus(int invalidStatus) {
        this.invalidStatus = invalidStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMinImg() {
        return minImg;
    }

    public void setMinImg(String minImg) {
        this.minImg = minImg;
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

    public String getMeasureSpecName() {
        return measureSpecName;
    }

    public void setMeasureSpecName(String measureSpecName) {
        this.measureSpecName = measureSpecName;
    }

    public String getRuleVal() {
        return ruleVal;
    }

    public void setRuleVal(String ruleVal) {
        this.ruleVal = ruleVal;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusNum() {
        return statusNum;
    }

    public void setStatusNum(int statusNum) {
        this.statusNum = statusNum;
    }



    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getMinOrderQuantity() {
        return minOrderQuantity;
    }

    public void setMinOrderQuantity(Integer minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    public String getUnitPrice() {
            return unitPrice;
        }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }

    public Integer getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(Integer auditResult) {
        this.auditResult = auditResult;
    }

    @Override
    public String toString() {
        return "SuppCommSearchVo{" +
                "id=" + id +
                ", minImg='" + minImg + '\'' +
                ", commId=" + commId +
                ", supplierName='" + supplierName + '\'' +
                ", code69='" + code69 + '\'' +
                ", brandName='" + brandName + '\'' +
                ", commName='" + commName + '\'' +
                ", unitName='" + unitName + '\'' +
                ", measureSpecName='" + measureSpecName + '\'' +
                ", ruleVal='" + ruleVal + '\'' +
                ", inventory='" + inventory + '\'' +
                ", status='" + status + '\'' +
                ", statusNum=" + statusNum +
                ", invalidStatus=" + invalidStatus +
                ", price='" + price + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", minOrderQuantity=" + minOrderQuantity +
                ", auditOpinion='" + auditOpinion + '\'' +
                ", auditResult=" + auditResult +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
