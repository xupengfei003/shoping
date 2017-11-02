package so.sao.shop.supplier.service.external;

import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommCategoryListInput;

import java.util.Map;

/**
 *<p>Version: supplier V1.1.0 </p>
 *<p>Title: CommCategoryPortalService</p>
 *<p>Description: </p>
 *@author: hanchao
 *@Date: Created in 2017/10/27 11:52
 */
public interface CommCategoryPortalService {
    /**
     * 批量修改商品分类名称和隐藏状态
     * @param commCategoryListInput
     * @throws Exception
     */
    Result updateCommCategorys(CommCategoryListInput commCategoryListInput) throws Exception;


    /**
     * 递归查询商品分类列表
     * @return
     */
    Map<String,Object> searchCommCategorys();

}
