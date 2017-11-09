package so.sao.shop.supplier.pojo.input;


public class HotCommodityInput extends AddHotCommInput{

//    @ApiModelProperty(value = "排序方式")
    private Integer sortord ;

    public Integer getSortord() {
        return sortord;
    }

    public void setSortord(Integer sortord) {
        this.sortord = sortord;
    }

}
