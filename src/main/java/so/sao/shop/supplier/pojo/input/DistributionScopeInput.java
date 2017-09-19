package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * @author gxy on 2017/9/18.
 */
public class DistributionScopeInput {

    /**
     * 省
     */
    @NotBlank(message = "省不能为空")
    private String addressProvince;

    /**
     * 市
     */
    @NotBlank(message = "市不能为空")
    private String addressCity;

    /**
     * 区
     */
    @NotBlank(message = "区不能为空")
    private String addressDistrict;

    /**
     * 描述
     */
//    @NotBlank(message = "备注不能为空")
    @Size(max = 100, min = 1, message = "备注字数不能超过100")
    private String remark;

    public String getAddressProvince() {
        return addressProvince;
    }

    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressDistrict() {
        return addressDistrict;
    }

    public void setAddressDistrict(String addressDistrict) {
        this.addressDistrict = addressDistrict;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
