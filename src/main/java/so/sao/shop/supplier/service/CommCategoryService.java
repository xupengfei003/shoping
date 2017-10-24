package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.output.CategoryOutput;

import java.util.List;
import java.util.Map;

/**
 * Created by QuJunLong on 2017/7/17.
 */
public interface CommCategoryService {

    /**
     * 查询商品类型列表
     * @param pid
     * @return CommCategorySelectOutput
     */
    List<CategoryOutput> searchCommCategory(Long pid);

    /**
     * 递归查询商品类型列表
     * @return
     */
    Map<String,Object> searchCommCategorys ();
}
