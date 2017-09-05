package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotBlank;
import so.sao.shop.supplier.config.Constant;

import javax.validation.constraints.Size;

/**
 * Created by Renzhiping on 2017/8/16.
 */
public class CommUnitUpdateInput {
    /**
     * id
     */
    private Long id;
    /**
     * 商品单位名称
     */
    @NotBlank(message = "商品单位名称不能为空")
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
