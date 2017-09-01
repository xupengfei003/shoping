package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import so.sao.shop.supplier.config.Constant;

/**
 * 商品标签修改入参
 * @author zhaoyan
 * @create 2017/8/14 16:01
 */
public class CommTagUpdateInput {
    /**
     * ID
     */
    private Long id;
    /**
     * 商品标签名称
     */
    @NotBlank(message = "标签名不能为空")
    @Length(max = Constant.CheckMaxLength.MAX_TAG_NAME_LENGTH,message = "商品标签名称长度不能超过" + Constant.CheckMaxLength.MAX_TAG_NAME_LENGTH + "位！")
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

    public CommTagUpdateInput() {
    }
}
