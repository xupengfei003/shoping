package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.CommCarton;

import java.util.List;

/**
 * 商品箱规单位dao
 * @author chensha
 */
@Mapper
public interface CommCartonDao {

    /**
     * 新增箱规单位
     * @param commCarton 商品箱规单位对象
     */
    void save(CommCarton commCarton);

    /**
     * 根据id删除箱规单位
     * @param id 箱规单位id
     */
    void deleteById(Long id);

    /**
     * 修改箱规单位
     * @param commCarton 箱规单位对象
     */
    void update(CommCarton commCarton);

    /**
     * 根据id查找箱规单位
     * @param id 箱规单位id
     * @return 箱规单位对象
     */
    CommCarton findOne(Long id);

    /**
     * 根据id统计数量
     * @param id
     * @return
     */
    int countById(@Param("id") Long id);

    /**
     * 根据箱规单位名称和supplierId查找
     * @param name  箱规单位名称
     * @param supplierId 供应商ID
     * @return
     */
    List<CommCarton> findByNameAndSupplierId(@Param("name") String name, @Param("supplierId") Long supplierId);

    /**
     * 根据箱规单位名称和supplierId查找存在数量(存在性校验)
     * @param name  箱规单位名称
     * @param supplierId 供应商ID
     * @return
     */
    int countByNameAndSupplierId(@Param("name") String name, @Param("supplierId") Long supplierId);

    /**
     * 根据供应商ID查询箱规单位集合
     * @param supplierId 供应商ID
     * @return 箱规单位集合
     */
    List<CommCarton> find(Long supplierId);
}
