package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.CommMeasureSpec;

import java.util.List;

/**
 * Created by XuPengFei on 2017/8/14.
 */
@Mapper
public interface CommMeasureSpecDao {

    /**
     * 查询计量规格集合
     * @param supplierId 供应商ID
     * @return  List<CommMeasureSpec>查询结果集合
     */
    List<CommMeasureSpec> find(long supplierId);

    /**
     * 根据计量规格名称 查询计量规格
     * @param supplierId 供应商ID
     * @param name　计量规格名称
     * @return List<CommMeasureSpec> 集合
     */
    List<CommMeasureSpec>  findByName(@Param("supplierId") long supplierId, @Param("name") String name);

    /**
     * 新增加计量规格
     * @param commMeasureSpec 计量规格对象
     * @return boolean
     */
    boolean save(CommMeasureSpec commMeasureSpec);

    /**
     * 根据计量规格主键id 查对应的计量规格
     * @param id  计量规格主键id
     * @return CommMeasureSpec 计量规格对象
     */
    public CommMeasureSpec findOne(long id);

    /**
     * 根据id查询count
     * @param id
     * @return
     */
    int findCountById(@Param("id") Long id);

    /**
     *根据计量规格主键id 删除对应的计量规格
     * @param id 计量规格主键id
     * @return  Boolean
     */
    public Boolean deleteOneById(long id);

    /**
     *根据计量规格的CommMeasureSpec更新计量规格
     * @param commMeasureSpec 计量规格对象
     * @return Boolean
     */
    public Boolean update(CommMeasureSpec commMeasureSpec);

}
