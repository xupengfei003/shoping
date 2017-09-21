package so.sao.shop.supplier.pojo.input;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by fangzhou on 2017/9/21.
 */
public class AppCartItemBatchInput {
    @Valid
    private List<AppCartItemInput> list;

    public List<AppCartItemInput> getList() {
        return list;
    }

    public void setList(List<AppCartItemInput> list) {
        this.list = list;
    }
}
