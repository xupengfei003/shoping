package so.sao.shop.supplier.pojo.input;

/**
 * 库存入参
 * @author gxy on 2017/10/13.
 */
public class CommInventoryInput {
    /**
     * 供应商id
     */
    private Long supplierId;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 库存预警(0代表库存正常,1代表下限预警)
     */
    private Integer inventoryStatus;
    /**
     * 简单查询输入值
     */
    private String inputvalue;
    /**
     * 商品条码
     */
    private String commCode69;
    /**
     * 商品名称
     */
    private String commName;
    /**
     * 商品一级分类ID
     */
    private Long categoryOneId;
    /**
     * 商品二级分类ID
     */
    private Long categoryTwoId;
    /**
     * 商品三级分类ID
     */
    private Long categoryThreeId;
    /**
     * 商品状态(2:上架,3:下架,4:上架待审核,5:下架待审核,6:编辑待审核)
     */
    private Integer status;
    /**
     * 当前页号
     */
    private Integer pageNum;
    /**
     * 页面大小
     */
    private Integer pageSize;

    /**
     * 区分角色 1管理员，0供应商
     */
    private Integer flag;

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Integer getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(Integer inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public String getInputvalue() {
        return inputvalue;
    }

    public void setInputvalue(String inputvalue) {
        this.inputvalue = inputvalue;
    }

    public String getCommCode69() {
        return commCode69;
    }

    public void setCommCode69(String commCode69) {
        this.commCode69 = commCode69;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
