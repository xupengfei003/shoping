package so.sao.shop.supplier.pojo.input;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by tengfei.zhang on 2017/10/13.
 */
public class QualificationInput {
    /**
     * 法人代表或供应商名称
     */
    private String name;
    /**
     * 审核状态
     */
    private String status;
    /**
     * 开始更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE)
    private Date startUpdateTime;
    /**
     * 结束更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE)
    private Date endUpdateTime;
    /**
     * 提交审核开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE)
    private Date startCreateTime;
    /**
     * 提交审核结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE)
    private Date endCreateTime;
    /**
     * 页数
     */
    private Integer pageNum;
    /**
     * 每页的数量
     */
    private Integer pageSize;
    /**
     * 排序方式
     */
    @NotNull(message = "排序方式不能为空")
    private Integer sort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartUpdateTime() {
        return startUpdateTime;
    }

    public void setStartUpdateTime(Date startUpdateTime) {
        this.startUpdateTime = startUpdateTime;
    }

    public Date getEndUpdateTime() {
        return endUpdateTime;
    }

    public void setEndUpdateTime(Date endUpdateTime) {
        this.endUpdateTime = endUpdateTime;
    }

    public Date getStartCreateTime() {
        return startCreateTime;
    }

    public void setStartCreateTime(Date startCreateTime) {
        this.startCreateTime = startCreateTime;
    }

    public Date getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(Date endCreateTime) {
        this.endCreateTime = endCreateTime;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
