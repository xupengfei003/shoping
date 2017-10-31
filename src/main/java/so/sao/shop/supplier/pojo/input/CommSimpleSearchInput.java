package so.sao.shop.supplier.pojo.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class CommSimpleSearchInput {

    /**
     * 供应商id
     */

    private Long supplierId;
    /**
     *审核结果：0代表未通过，1代表通过
     */
    private Integer auditResult;
    /**
     * 商品状态（2:上架、3:下架、4:上架待审核、5:下架待审核、6: 编辑待审核）
     */
    private Integer status;

    /**
     * 商品条码/商品名称模糊
     */
    private String inputvalue;

    /**
     * 创建开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE)
    private Date beginCreateAt;

    /**
     * 创建结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE)
    private Date endCreateAt;

    /**
     * 页数
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;
    /**
     *排序字段
     */
    private Integer sortStatus;

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

    public Integer getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(Integer auditResult) {
        this.auditResult = auditResult;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSortStatus() {return sortStatus;}

    public void setSortStatus(Integer sortStatus) {this.sortStatus = sortStatus;}

    @Override
    public String toString() {
        return "CommSimpleSearchInput{" +
                "supplierId=" + supplierId +
                ", auditResult=" + auditResult +
                ", status=" + status +
                ", inputvalue='" + inputvalue + '\'' +
                ", beginCreateAt=" + beginCreateAt +
                ", endCreateAt=" + endCreateAt +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", sortStatus=" + sortStatus +
                '}';
    }
}
