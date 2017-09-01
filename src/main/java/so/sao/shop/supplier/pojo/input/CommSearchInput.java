package so.sao.shop.supplier.pojo.input;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class CommSearchInput {

    /**
     * 供应商id
     */
    private Long supplierId;

    /**
     * 商品条码
     */
    private String commCode69;

    /**
     * sku
     */
    private String sku;

    /**
     * 商品商家编码
     */
    private String suppCommCode;

    /**
     * 商品名称
     */
    private String commName;

    /**
     * 商品状态
     */
    private Integer status;

    /**
     * 商品科属id
     */
    private Long typeId;

    /**
     * 最小价格
     */
    private BigDecimal minPrice;

    /**
     * 最大价格
     */
    private BigDecimal maxPrice;

    /**
     * 创建开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date beginCreateAt;

    /**
     * 创建结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date endCreateAt;

    /**
     * 页数
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
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

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
