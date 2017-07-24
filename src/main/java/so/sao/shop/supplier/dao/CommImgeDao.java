package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.CommImge;

import java.util.List;

/**
 * Created by QuJunLong on 2017/7/17.
 */
@Mapper
public interface CommImgeDao {
    /**
     * 增加商品大图
     * @param commImge 商品大图信息
     * @return 商品大图
     */
    boolean save(CommImge commImge);
    /**
     * 删除商品大图
     * @param id 商品大图ID
     * @return 删除结果
     */
    boolean deleteById(@Param("id") Long id);
    /**
     * 修改商品大图
     * @param commImge 商品大图对象
     * @return 修改结果
     */
    boolean update(CommImge commImge);

    /**
     * 查询商品大图
     * @param id 商品大图ID
     * @return  商品大图对象
     */
    CommImge findOne(@Param("id")Long id);

    /**
     * 查询商品大图集合
     * @param scId 供应商商品Id
     * @return 查询结果结合
     */
    List<CommImge> find(@Param("scId") Long scId);
}
