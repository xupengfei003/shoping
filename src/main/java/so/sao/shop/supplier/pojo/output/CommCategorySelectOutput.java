package so.sao.shop.supplier.pojo.output;

/**商品品类对象
 * 用于商品品类查询结果显示
 * Created by Renzhiping on 2017/7/21.
 */

public class CommCategorySelectOutput {
    private Long id;
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
