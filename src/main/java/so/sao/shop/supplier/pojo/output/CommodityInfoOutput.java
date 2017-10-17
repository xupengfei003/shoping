package so.sao.shop.supplier.pojo.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import so.sao.shop.supplier.domain.TyCommImge;

import java.util.Date;
import java.util.List;

/**
 * Created by acer on 2017/8/15.
 */
public class CommodityInfoOutput {

    /**
     * ID
     */
    private Long id;

    /**
     * 商品条码
     */
    private String code69;

    /**
     * 品牌名
     */
    private String brandName;

    /**
     * 商品一级分类
     */
    private Long categoryOneId;

    /**
     * 商品一级名称
     */
    private String categoryOneName;

    /**
     * 商品二级分类
     */
    private Long categoryTwoId;

    /**
     * 商品二级名称
     */
    private String categoryTwoName;

    /**
     * 商品三级分类
     */
    private Long categoryThreeId;

    /**
     * 商品三级名称
     */
    private String categoryThreeName;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 企业名称
     */
    private String companyName;
    

    /**
     * 商品产地
     */
    private String originPlace;

    /**
     * 公共图片集合
     */
    private List<TyCommImge> imgeList;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode69() {
        return code69;
    }

    public void setCode69(String code69) {
        this.code69 = code69;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOriginPlace() {
        return originPlace;
    }

    public void setOriginPlace(String originPlace) {
        this.originPlace = originPlace;
    }

    public List<TyCommImge> getImgeList() {
        return imgeList;
    }

    public void setImgeList(List<TyCommImge> imgeList) {
        this.imgeList = imgeList;
    }

    public String getCategoryOneName() {
        return categoryOneName;
    }

    public void setCategoryOneName(String categoryOneName) {
        this.categoryOneName = categoryOneName;
    }

    public String getCategoryTwoName() {
        return categoryTwoName;
    }

    public void setCategoryTwoName(String categoryTwoName) {
        this.categoryTwoName = categoryTwoName;
    }

    public String getCategoryThreeName() {
        return categoryThreeName;
    }

    public void setCategoryThreeName(String categoryThreeName) {
        this.categoryThreeName = categoryThreeName;
    }
}
