package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.CommImge;
import so.sao.shop.supplier.domain.TyCommImge;

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
     * 批量新增
     * @param commImges
     */
    void batchSave(List<CommImge> commImges);

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

    /**
     * 批量删除商品大图
     * @param ids 商品大图ID
     * @return 删除结果
     */
    boolean deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 根据scId获取id集合
     * @param scId 供应商商品Id
     * @return 查询结果结合
     */
    List<Long> findIdsByScId(@Param("scId") Long scId);

    /**
     * 批量删除商品大图
     * @param scIds 商品ID
     */
    void deleteByScIds(@Param("scIds") List<Long> scIds);
}
