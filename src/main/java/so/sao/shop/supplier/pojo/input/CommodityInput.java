package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.Length;
import so.sao.shop.supplier.pojo.vo.CommImgeVo;
import so.sao.shop.supplier.pojo.vo.SupplierCommodityVo;

import javax.validation.Valid;
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
    @Length(max = 20,message = "品牌长度不能大于20")
    private String brand;
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
     * 商品名称
     */
    @Length(max = 200,message = "商品名称长度不能大于200")
    private String name;
    /**
     * 商品描述
     */
    private String remark;
    /**
     * 商品介绍
     */
    private String description;
    /**
     * 商品集合
     */
    @Valid
    private List<SupplierCommodityVo> commodityList;

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SupplierCommodityVo> getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(List<SupplierCommodityVo> commodityList) {
        this.commodityList = commodityList;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
