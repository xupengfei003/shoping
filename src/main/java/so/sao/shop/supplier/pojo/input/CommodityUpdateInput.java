package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import so.sao.shop.supplier.pojo.vo.SupplierCommodityUpdateVo;

import javax.validation.Valid;
import java.util.List;

public class CommodityUpdateInput {

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
     * 商品规格集合
     */
    @NotEmpty(message = "商品规格不能为空")
    @Valid
    private List<SupplierCommodityUpdateVo> commodityList;

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

    public List<SupplierCommodityUpdateVo> getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(List<SupplierCommodityUpdateVo> commodityList) {
        this.commodityList = commodityList;
    }
}
