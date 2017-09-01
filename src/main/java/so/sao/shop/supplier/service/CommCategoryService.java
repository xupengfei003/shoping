package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommCategoryInput;
import so.sao.shop.supplier.pojo.input.CommCategoryUpdateInput;
import so.sao.shop.supplier.pojo.output.CommCategorySelectOutput;

import java.util.List;

/**
 * Created by QuJunLong on 2017/7/17.
 */
public interface CommCategoryService {

    /**
     * 保存新增加的商品品类-service
     * @param commCategory
     * @return BaseResult
     */
    BaseResult saveCommCategory(CommCategoryInput commCategory);
    /**
     * 根据ID删除科属
     * @param id
     * @return
     */
    Result deleteCommCategory(Long id);
	/**
     * 根据ID修改商品类型信息
     * @param commCategoryUpdateInput 商品类型对象入参
     * @return
     */
    BaseResult updateCommCategory(CommCategoryUpdateInput commCategoryUpdateInput);
    /**
     * 查询商品类型列表
     * @param pid
     * @return CommCategorySelectOutput
     */
    List<CommCategorySelectOutput> searchCommCategory(Long pid);
}
