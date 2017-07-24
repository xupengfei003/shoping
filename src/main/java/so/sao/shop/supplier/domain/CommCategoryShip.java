package so.sao.shop.supplier.domain;

/**
 * 商品与品类关系实体类
 * Created by QuJunLong on 2017/7/17.
 */
public class CommCategoryShip {
    /**
     * ID
     */
    private Long id;
    /**
     * 商品Id
     */
    private Long commId;
    /**
     * 商品名称
     */
    private String commName;
    /**
     * 类型Id
     */
    private Long categoryId;
    /**
     * 类型名称
     */
    private String categoryName;

    public Long getCommId() {
        return commId;
    }

    public void setCommId(Long commId) {
        this.commId = commId;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
