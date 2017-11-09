package so.sao.shop.supplier.dao.external;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.CommCategory;
import so.sao.shop.supplier.pojo.input.CommCategoryPortalInput;

import java.util.List;

/**
 *<p>Version: supplier V1.1.0 </p>
 *<p>Title: CommCategoryPortalDao</p>
 *<p>Description: </p>
 *@author: hanchao
 *@Date: Created in 2017/10/27 13:58
 */
@Mapper
public interface CommCategoryPortalDao {
    /**
     * 批量修改商品分类名称和显示状态
     * @param commCategoryPortalInputs
     * @return 返回受影响行数
     */
    int updateCommCategorys(@Param("commCategoryPortalInputs") List<CommCategoryPortalInput> commCategoryPortalInputs);

    /**
     * 查询商品分类表所有数据
     * @return 商品分类集合
     */
    List<CommCategory> findAll();


}

