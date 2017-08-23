package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotEmpty;

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
    @NotEmpty(message = "商品单位名称不能为空")
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
        this.name = name;
    }
}
