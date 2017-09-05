package so.sao.shop.supplier.pojo.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import so.sao.shop.supplier.pojo.vo.SupplierCommodityVo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 商品对象 出参
 * 用于商品新增修改 使用
 * Created by QuJunLong on 2017/7/19.
 */
public class CommodityInput {

    /**
     *
     * Excel 行号
     */
    private  int rowNum;

    /**
     * 品牌
     */
    @NotBlank(message = "商品品牌不能为空")
    @Length(max = 256,message = "商品品牌长度不能大于256")
    private String brandName;

    /**
     * 一级类型Id
     */
    @NotNull(message = "商品科属一级类型不能为空")
    private Long categoryOneId;

    /**
     * 二级类型Id
     */
    @NotNull(message = "商品科属二级类型不能为空")
    private Long categoryTwoId;

    /**
     * 三级类型Id
     */
    @NotNull(message = "商品科属三级类型不能为空")
    private Long categoryThreeId;

    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    @Length(max = 256,message = "商品名称长度不能大于256")
    private String name;

    /**
     * 商品说明
     */
    @NotBlank(message = "商品说明不能为空")
    @Length(max = 65535,message = "商品说明长度不能大于65535")
    private String remark;

    /**
     * 商品标签Id
     */
    private Long tagId;
    /**
     * 商品标签Name
     */
    private String tagName;

    /**
     * 企业名称
     */
    @Length(max = 256,message = "企业名称长度不能大于256")
    private String companyName;

    /**
     * 上市时间
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date marketTime;

    /**
     * 商品产地
     */
    @Length(max = 256,message = "商品产地长度不能大于256")
    private String originPlace;

    /**
     * 商品集合
     */
    @NotEmpty(message = "商品规格不能为空")
    @Valid
    private List<SupplierCommodityVo> commodityList;

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getMarketTime() {
        return marketTime;
    }

    public void setMarketTime(Date marketTime) {
        this.marketTime = marketTime;
    }

    public String getOriginPlace() {
        return originPlace;
    }

    public void setOriginPlace(String originPlace) {
        this.originPlace = originPlace;
    }

    public List<SupplierCommodityVo> getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(List<SupplierCommodityVo> commodityList) {
        this.commodityList = commodityList;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
