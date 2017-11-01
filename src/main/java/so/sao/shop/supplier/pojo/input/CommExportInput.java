package so.sao.shop.supplier.pojo.input;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Renzhiping on 2017/9/8.
 */
public class CommExportInput {
    @ApiModelProperty(value = "供应商id", required = true)
    private Long supplierId;

    @ApiModelProperty(value = "商品条码")
    private String commCode69;

    @ApiModelProperty(value = "商品sku")
    private String sku;

    @ApiModelProperty(value = "商品商家编码")
    private String code;

    @ApiModelProperty(value = "商品名称")
    private String commName;

    @ApiModelProperty(value = "商品状态")
    private Integer invalidStatus;
    @ApiModelProperty(value = "商品状态")
    private Integer status;
    @ApiModelProperty(value = "审核结果")
    private Integer auditResult;
    @ApiModelProperty(value = "商品科属id")
    private Long typeId;

    @ApiModelProperty(value = "app订货价最小价格")
    private BigDecimal minPrice;

    @ApiModelProperty(value = "app订货价最大价格")
    private BigDecimal maxPrice;
    @ApiModelProperty(value = "透云进货价最小价格")
    private BigDecimal minUnitPrice;

    @ApiModelProperty(value = "透云进货价最大价格")
    private BigDecimal maxUnitPrice;

    @ApiModelProperty(value = "更新开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE)
    private Date beginUpdateAt;

    @ApiModelProperty(value = "更新结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE)
    private Date endUpdateAt;

    @ApiModelProperty(value = "商品条码/商品名称模糊")
    private String inputvalue;
    @ApiModelProperty(value = "商品品牌")
    private String commBrand;

    @ApiModelProperty(value = "页数")
    private String pageNum;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;
    @ApiModelProperty(value = "排序")
    private Integer sortStatus;

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getInputvalue() {return inputvalue;}

    public void setInputvalue(String inputvalue) {this.inputvalue = inputvalue;}

    public String getCommBrand() {return commBrand;}

    public void setCommBrand(String commBrand) {this.commBrand = commBrand;}

    public String getCommCode69() {
        return commCode69;
    }

    public void setCommCode69(String commCode69) {
        this.commCode69 = commCode69;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCode() {return code;}

    public void setCode(String code) {this.code = code;}

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getInvalidStatus() {return invalidStatus;}

    public void setInvalidStatus(Integer invalidStatus) {this.invalidStatus = invalidStatus;}

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Date getBeginUpdateAt() {return beginUpdateAt;}

    public void setBeginUpdateAt(Date beginUpdateAt) {this.beginUpdateAt = beginUpdateAt;}

    public Date getEndUpdateAt() {return endUpdateAt;}

    public void setEndUpdateAt(Date endUpdateAt) {this.endUpdateAt = endUpdateAt;}

    public Integer getAuditResult() {return auditResult;}

    public void setAuditResult(Integer auditResult) {this.auditResult = auditResult;}

    public Integer getSortStatus() {return sortStatus;}

    public void setSortStatus(Integer sortStatus) {this.sortStatus = sortStatus;}

    public BigDecimal getMinUnitPrice() {return minUnitPrice;}

    public void setMinUnitPrice(BigDecimal minUnitPrice) {this.minUnitPrice = minUnitPrice;}

    public BigDecimal getMaxUnitPrice() {return maxUnitPrice;}

    public void setMaxUnitPrice(BigDecimal maxUnitPrice) {this.maxUnitPrice = maxUnitPrice;}
}
