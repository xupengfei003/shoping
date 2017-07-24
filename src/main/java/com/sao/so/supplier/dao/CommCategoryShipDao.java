package com.sao.so.supplier.dao;

import com.sao.so.supplier.domain.CommCategoryShip;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by QuJunLong on 2017/7/17.
 */
@Mapper
public interface CommCategoryShipDao {
    /**
     * 增加商品类型关系
     * @param commCategoryShip 商品类型关系信息
     * @return 保存结果
     */
    boolean save(CommCategoryShip commCategoryShip);
    /**
     * 增加商品类型关系
     * @param categoryId 类型id
     * @param commId 商品id
     * @return
     */
    CommCategoryShip findById(@Param("categoryId") long categoryId,@Param("commId") long commId);
    /**
     * 删除商品类型关系
     * @param typeId 商品类型ID
     * @return 删除结果
     */
    boolean deleteByTypeId(@Param("typeId") long typeId);
    /**
     * 删除商品类型关系
     * @param commId 商品ID
     * @return 删除结果
     */
    boolean deleteByCommId(@Param("commId") long commId);
}
