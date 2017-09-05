package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.output.CommCategorySelectOutput;

import java.util.List;

/**
 * Created by QuJunLong on 2017/7/17.
 */
public interface CommCategoryService {

    /**
     * 查询商品类型列表
     * @param pid
     * @return CommCategorySelectOutput
     */
    List<CommCategorySelectOutput> searchCommCategory(Long pid);
}
