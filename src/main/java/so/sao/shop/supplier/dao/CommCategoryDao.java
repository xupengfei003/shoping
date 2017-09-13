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
     * 查询商品类型集合
     * @param commCategoryOneId
     * @param commCategoryTwoId
     * @param commCategoryThreeId
     * @return
     */
    List<CommCategory> findByIds(@Param("commCategoryOneId")Long commCategoryOneId, @Param("commCategoryTwoId")Long commCategoryTwoId, @Param("commCategoryThreeId")Long commCategoryThreeId);

    /**
     * 根据类型名称 查询商品类型
     * @param commCategoryName 商品类型名称
     * @param pid 商品类型父id
     * @return 查询结果结合
     */
    CommCategory findCommCategoryByNameAndPid(@Param("commCategoryName") String commCategoryName,@Param("pid") Long pid);

    /**
     * 查询商品类型集合
     * @param pid
     * @return CommCategory
     */
    List<CommCategory> find(@Param("pid")Long pid);

    /**
     * 查询商品类型集合
     * @param name
     * @return CommCategory
     */
    List<CommCategory> findOneByName(@Param("name")String name);

    /**
     * 查询商品类型
     * @param id
     * @return CommCategory
     */
    CommCategory findById(@Param("id")Long id);
}
