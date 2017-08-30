package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.CommTag;

import java.util.List;

/**
 * 商品标签dao
 *
 * @author zhaoyan
 * @create 2017/8/14 14:37
 */
@Mapper
public interface CommTagDao {

    /**
     * 新增商品标签
     *
     * @param commTag 商品标签对象
     * @return 新增结果
     */
    boolean save(CommTag commTag);

    /**
     * 根据id删除商品标签
     *
     * @param id 商品标签id
     * @return 删除结果
     */
    boolean deleteById(Long id);

    /**
     * 修改商品标签
     *
     * @param commTag 商品标签对象
     * @return 修改结果
     */
    boolean update(CommTag commTag);

    /**
     * 根据id查找商品标签
     *
     * @param id 商品标签id
     * @return 商品标签对象
     */
    CommTag findOne(Long id);

    /**
     * 根据id查询count
     * @param id
     * @return
     */
    int findCountById(@Param("id") Long id);

    /**
     * 根据商品标签名称和supplierId查找商品标签
     *
     * @param name  商品标签名
     * @param supplierId 供应商ID
     * @return
     */
    List<CommTag> findByNameAndSupplierId(@Param("name") String name, @Param("supplierId")Long supplierId);

    /**
     * 查询商品标签集合
     *
     * @param supplierId 供应商ID
     * @return 商品标签集合
     */
    List<CommTag> find(Long supplierId);
}
