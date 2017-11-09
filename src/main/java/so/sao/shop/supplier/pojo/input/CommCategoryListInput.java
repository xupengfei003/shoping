package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

/**
 *<p>Version: supplier V1.1.0 </p>
 *<p>Title: CommCategoryListInput</p>
 *<p>Description: </p>
 *@author: hanchao
 *@Date: Created in 2017/10/30 15:26
 */
public class CommCategoryListInput {
    @Valid
    @NotEmpty(message = "集合不能为空！")
    List<CommCategoryPortalInput> commCategoryPortalInputs;

    public List<CommCategoryPortalInput> getCommCategoryPortalInputs() {
        return commCategoryPortalInputs;
    }

    public void setCommCategoryPortalInputs(List<CommCategoryPortalInput> commCategoryPortalInputs) {
        this.commCategoryPortalInputs = commCategoryPortalInputs;
    }
}