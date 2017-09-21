package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.CommBrand;
import so.sao.shop.supplier.pojo.output.CommBrandOutput;

import java.util.List;

/**
 * Created by QuJunLong on 2017/7/18.
 */
@Mapper
public interface CommBrandDao {
    /**
     * 增加品牌
     * @param commBrand 品牌信息
     * @return 商品对象
     */
    boolean save(CommBrand commBrand);
    /**
     * 查询品牌集合
     * @param commBrand 品牌对象
     * @return 查询结果结合
     */
    List<CommBrand> find(CommBrand commBrand);
    /**
     * 查询品牌
     * @param name 品牌名称
     * @return  商品类型对象
     */
    CommBrand findByName(@Param("name")String name);

    /**
     * 根据品牌id查询品牌
     * @param id
     * @return
     */
    CommBrand findById(@Param("id")Long id);

    /**
     * 查询商品品牌
     * @return
     */
    List<CommBrandOutput> search();

}
