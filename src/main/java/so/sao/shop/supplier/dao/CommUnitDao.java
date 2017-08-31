package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.CommUnit;
import so.sao.shop.supplier.domain.SupplierCommodity;

import java.util.List;

/**
 * Created by Renzhiping on 2017/8/14.
 */
public interface CommUnitDao {
    /**
     * 添加商品单位
     * @param commUnit 商品单位对象
     * @return 新增结果
     */
    boolean save(CommUnit commUnit);

    /**
     * 修改商品单位
     * @param commUnit 商品单位对象
     * @return 修改结果
     */
    boolean update(CommUnit commUnit);

    /**
     * 通过id删除商品单位
     * @param id 商品单位id
     * @return 删除结果
     */
    boolean delete(@Param("id") Long id);

    /**
     * 查询商品单位集合
     * @param  supplierId 供应商id
     * @return 商品单位对象集合
     */
    List<CommUnit> search(Long  supplierId);

    /**
     * 查询商品单位有无重复
     * @param supplierId 供应商id
     * @param name 商品单位名称
     * @return 商品单位对象集合
     */
    List<CommUnit> findNameAndSupplierId(@Param("supplierId") Long supplierId, @Param("name") String name);

    /**
     *根据id查询商品单位信息
     * @param id 商品单位id
     * @return  商品单位对象
     */
     CommUnit findOne(Long id);

    /**
     * 根据id查询count
     * @param id
     * @return
     */
    int countById(@Param("id") Long id);
}
