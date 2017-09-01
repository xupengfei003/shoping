package so.sao.shop.supplier.pojo.input;

import java.util.Date;

public class CommSimpleSearchInput {

    /**
     * 供应商id
     */

    private Long supplierId;

    /**
     * 商品条码/商品名称模糊
     */
    private String inputvalue;

    /**
     * 创建开始时间
     */
    private Date beginCreateAt;

    /**
     * 创建结束时间
     */
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

    public String getInputvalue() {
        return inputvalue;
    }

    public void setInputvalue(String inputvalue) {
        this.inputvalue = inputvalue;
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
