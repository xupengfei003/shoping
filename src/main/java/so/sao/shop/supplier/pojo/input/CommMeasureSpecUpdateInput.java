package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import so.sao.shop.supplier.config.Constant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by XuPengFei on 2017/8/15.
 */
public class CommMeasureSpecUpdateInput {
    /**
     * ID
     */
    @NotNull(message = "计量规格ID不能为空！")
    private Long id;
    /**
     * 商品计量规格名称
     */
    @NotBlank(message = "计量规格名不能为空")
    @Size(min = 1,max = Constant.CheckMaxLength.MAX_MEASURESPEC_NAME_LENGTH,message = "名称至少一位，不能超过"+ Constant.CheckMaxLength.MAX_MEASURESPEC_NAME_LENGTH +"位")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }
}
