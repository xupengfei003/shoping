package so.sao.shop.supplier.dao.external;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.external.HotCategories;

import java.util.List;

public interface HotCategoriesDao {

    /**
     * 查询所有热门分类集合
     * @return 热门分类集合
     */
    List<HotCategories> find();

    /**
     * 清除热门分类数据表
     */
    void deleteHotCategoriesData();

    /**
     * 批量添加热门分类
     * @param hotCategories 热门分类对象集合
     */
    void saveBatch(@Param("hotCategories") List<HotCategories> hotCategories);
}
