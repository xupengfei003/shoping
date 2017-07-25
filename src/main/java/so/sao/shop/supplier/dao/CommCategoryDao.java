package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.CommCategory;

import java.util.List;

/**
 * Created by QuJunLong on 2017/7/17.
 */
@Mapper
public interface CommCategoryDao {
    /**
     * 新增加商品类型
     * @param commCategory 商品类型信息
     * @return 商品对象
     */
    boolean save(@Param("commCategory") CommCategory commCategory);
    /**
     * 删除商品类型
     * @param id 商品类型ID
     * @return 删除结果
     */
    boolean deleteById(@Param("id") long id);
    /**
     * 修改商品类型
     * @param commCategory 商品类型对象
     * @return 修改结果
     */
    boolean update(CommCategory commCategory);

    /**
     * 查询商品类型
     * @param id 商品类型ID
     * @return  商品类型对象
     */
    CommCategory findOne(@Param("id")long id);

    /**
     * 查询商品类型集合
     * @param commCategory 商品类型对象
     * @return 查询结果结合
     */
    List<CommCategory> find(CommCategory commCategory);

    /**
     * 根据类型名称 查询商品类型
     * @param commCategory 商品类型对象
     * @return 查询结果结合
     */
    CommCategory findCommCategoryByName(@Param("commCategoryName") String commCategoryName);

    /**
     *  根据新增的类型PID 查询商品类型ID = PID 的商品类型的等级level
     * @param pid
     * @return  等级level
     */
    Integer findCommCategoryLevelByPid(@Param("pid")Long pid);

    /**
     * 查询商品类型集合
     * @param pid
     * @return CommCategory
     */
    List<CommCategory> find(@Param("pid")Long pid);

    /**
     * 查询商品类型名称
     * @param id
     * @return CommCategory
     */
    String findNameById(@Param("id")Long id);
}
