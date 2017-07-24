package com.sao.so.supplier.dao;

import com.sao.so.supplier.domain.Commodity;
import com.sao.so.supplier.pojo.output.CommodityExportOutput;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by QuJunLong on 2017/7/17.
 */
@Mapper
public interface CommodityDao {
    /**
     * 增加商品
     * @param commodity 商品信息对象
     * @return 商品对象
     */
    boolean save(Commodity commodity);
    /**
     * 删除商品
     * @param id 商品
     * @return 删除结果
     */
    boolean deleteById(@Param("id") long id);
    /**
     * 修改商品
     * @param commodity 商品信息对象
     * @return 修改结果
     */
    boolean update(Commodity commodity);
    /**
     * 查询商品
     * @param id 商品ID
     * @return  商品信息对象
     */
    Commodity findOne(@Param("id")long id);
    /**
     * 查询商品
     * @param name 商品名称
     * @return  商品信息对象
     */
    Commodity findByName(@Param("name")String name);

    /**
     * 查询商品信息集合
     * @param commodity
     * @return 查询结果结合
     */
    List<Commodity> find(Commodity commodity);

    /**
     * 根据id查询多个商品
     * @param ids
     * @return
     */
    List<CommodityExportOutput> findByIds(Long[] ids);

}
