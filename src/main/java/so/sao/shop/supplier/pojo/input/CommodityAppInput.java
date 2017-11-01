package so.sao.shop.supplier.pojo.input;



public class CommodityAppInput {
    /**
     * supplierId
     */
    private Long supplierId;
    /**
     * 品牌id
     */
    private String[] brandIds;

    /**
     * 一级类型Id
     */
    private Long categoryOneId;

    /**
     * 二级类型Id
     */
    private Long categoryTwoId;

    /**
     * 三级类型Id
     */
    private Long categoryThreeId;

    /**
     * 页数
     */
    private Integer pageNum;

    /**
     * 每个页的大小
     */
    private Integer pageSize;

    /**
     * 排序方式
     */
    private String orderPriceOrSalesNum;


    public String[] getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(String[] brandIds) {
        this.brandIds = brandIds;
    }

    public Long getCategoryTwoId() {
        return categoryTwoId;
    }

    public void setCategoryTwoId(Long categoryTwoId) {
        this.categoryTwoId = categoryTwoId;
    }

    public Long getCategoryThreeId() {
        return categoryThreeId;
    }

    public void setCategoryThreeId(Long categoryThreeId) {
        this.categoryThreeId = categoryThreeId;
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

    public String getOrderPriceOrSalesNum() {
        return orderPriceOrSalesNum;
    }

    public void setOrderPriceOrSalesNum(String orderPriceOrSalesNum) {
        this.orderPriceOrSalesNum = orderPriceOrSalesNum;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getCategoryOneId() {
        return categoryOneId;
    }

    public void setCategoryOneId(Long categoryOneId) {
        this.categoryOneId = categoryOneId;
    }
}
