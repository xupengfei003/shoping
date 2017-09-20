package so.sao.shop.supplier.pojo.input;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by acer on 2017/9/19.
 */
public class AddHotCommInput {

    @ApiModelProperty(value = "商品条码/商品名称模糊")
    private String inputvalue ;

    @ApiModelProperty(value = "商品一级类别")
    Long categoryOneId ;

    @ApiModelProperty(value = "商品二级类别")
    Long categoryTwoId ;

    @ApiModelProperty(value = "商品三级类别")
    Long categoryThreeId ;

    @ApiModelProperty(value = "页码")
    private Integer pageNum ;

    @ApiModelProperty(value = "大小")
    private Integer pageSize ;

    public String getInputvalue() {
        return inputvalue;
    }

    public void setInputvalue(String inputvalue) {
        this.inputvalue = inputvalue;
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
