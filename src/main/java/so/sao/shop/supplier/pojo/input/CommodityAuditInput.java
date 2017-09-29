package so.sao.shop.supplier.pojo.input;

/**
 * Created by acer on 2017/9/19.
 * 查询商品状态入参实体类
 */
public class CommodityAuditInput {
    /**
     * 商品状态
     */
    private String status;
    /**
     * 审核结果
     */
    private String auditResult;
    /**
     * 页数
     */
    private int pageNum;
    /**
     *页面显示条数
     */
    private int pageSize;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
