package com.sao.so.shop.dao;

import com.sao.so.shop.domain.CommBrand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by QuJunLong on 2017/7/18.
 */
@Mapper
public interface CommBrandDao {
    /**
     * 增加品牌
     * @param commBrand 品牌信息
     * @return 商品对象
     */
    boolean save(CommBrand commBrand);
    /**
     * 查询品牌集合
     * @param commBrand 品牌对象
     * @return 查询结果结合
     */
    List<CommBrand> find(CommBrand commBrand);
    /**
     * 查询品牌
     * @param name 品牌名称
     * @return  商品类型对象
     */
    CommBrand findByName(@Param("name")String name);
}
