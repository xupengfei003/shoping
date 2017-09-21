package so.sao.shop.supplier.pojo.input;

/**
 * Created by acer on 2017/9/20.
 */
public class CommAppInput {

    /**
     * 商品条码
     */
   private String code69;
    /**
     * 供应商ID
     */
   private Long supplierId;
    /**
     * 商品名称
     */
   private String commName;
    /**
     * 商品一级科属ID
     */
   private Long categoryOneId;
    /**
     * 商品二级科属ID
     */
   private Long categoryTwoId;
    /**
     * 商品三级科属ID
     */
   private Long categoryThreeId;
    /**
     * 商品品牌ID集合
     */
   private Long[] brandIds;
    /**
     * 当前页号
     */
   private Integer pageNum;
    /**
     * 页面大小
     */
   private Integer pageSize;

    public String getCode69() {
        return code69;
    }

    public void setCode69(String code69) {
        this.code69 = code69;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public Long getCategoryOneId() {
        return categoryOneId;
    }

    public void setCategoryOneId(Long categoryOneId) {
        this.categoryOneId = categoryOneId;
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

    public Long[] getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(Long[] brandIds) {
        this.brandIds = brandIds;
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
