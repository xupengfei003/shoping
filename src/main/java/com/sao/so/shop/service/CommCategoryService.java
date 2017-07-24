package com.sao.so.shop.service;

import com.sao.so.shop.pojo.BaseResult;
import com.sao.so.shop.pojo.input.CommCategoryInput;
import com.sao.so.shop.pojo.input.CommCategoryUpdateInput;
import com.sao.so.shop.pojo.output.CommCategorySelectOutput;

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
     * 根据ID删除相应的品类
     * @param id
     * @return
     */
    BaseResult deleteCommCategory(Long id) ;
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
