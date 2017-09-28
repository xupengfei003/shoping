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
    private String suppCommCode;

    @ApiModelProperty(value = "商品名称")
    private String commName;

    @ApiModelProperty(value = "商品状态")
    private Integer invalidStatus;
    @ApiModelProperty(value = "商品状态")
    private Integer status;
    @ApiModelProperty(value = "商品科属id")
    private Long typeId;

    @ApiModelProperty(value = "最小价格")
    private BigDecimal minPrice;

    @ApiModelProperty(value = "最大价格")
    private BigDecimal maxPrice;

    @ApiModelProperty(value = "创建开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE)
    private Date beginCreateAt;

    @ApiModelProperty(value = "创建结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE)
    private Date endCreateAt;

    @ApiModelProperty(value = "商品条码/商品名称模糊")
    private String commCodeOrName;

    @ApiModelProperty(value = "页数")
    private String pageNum;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;


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

    public String getCommCodeOrName() {
        return commCodeOrName;
    }

    public void setCommCodeOrName(String commCodeOrName) {
        this.commCodeOrName = commCodeOrName;
    }

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

    public String getSuppCommCode() {
        return suppCommCode;
    }

    public void setSuppCommCode(String suppCommCode) {
        this.suppCommCode = suppCommCode;
    }

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

    public Date getBeginCreateAt() {
        return beginCreateAt;
    }

    public void setBeginCreateAt(Date beginCreateAt) {
        this.beginCreateAt = beginCreateAt;
    }

    public Date getEndCreateAt() {
        return endCreateAt;
    }

    public void setEndCreateAt(Date endCreateAt) {
        this.endCreateAt = endCreateAt;
    }

}
