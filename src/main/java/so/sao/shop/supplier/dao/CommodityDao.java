package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.Commodity;
import so.sao.shop.supplier.pojo.input.CommExportInput;
import so.sao.shop.supplier.pojo.output.CommodityExportOutput;
import so.sao.shop.supplier.pojo.output.CommodityInfoOutput;

import java.util.List;

/**
 * Created by QuJunLong on 2017/7/17.
 */
@Mapper
public interface CommodityDao {
    /**
     * 增加商品
     * @param commodity 商品信息对象
     * @return 商品对象
     */
    boolean save(Commodity commodity);
    /**
     * 删除商品
     * @param id 商品
     * @return 删除结果
     */
    boolean deleteById(@Param("id") long id);
    /**
     * 修改商品
     * @param commodity 商品信息对象
     * @return 修改结果
     */
    boolean update(Commodity commodity);
    /**
     * 修改商品
     * @param commodity 商品信息对象
     * @return 修改结果
     */
    boolean updateComm(Commodity commodity);
    /**
     * 查询商品
     * @param id 商品ID
     * @return  商品信息对象
     */
    Commodity findOne(@Param("id")long id);

    /**
     * 查询商品
     * @param code69 商品编码
     * @return  商品信息对象
     */
    CommodityInfoOutput findByCode69(@Param("code69")String code69);
    /**
     * 查询商品
     * @param code69 商品编码
     * @return  商品信息对象
     */
    Commodity findCommInfoByCode69(@Param("code69")String code69);

    /**
     * 根据code69 商品编码查询商品名称
     * @param code69
     * @return
     * @throws Exception
     */
    Commodity findNameByCode69(@Param("code69")String code69) throws Exception;

    /**
     * 查询商品信息集合
     * @param commodity
     * @return 查询结果结合
     */
    List<Commodity> find(Commodity commodity);

    /**
     * 商品批量导出
     * @param commExportInput
     * @return
     */
    List<CommodityExportOutput> findByIds(CommExportInput commExportInput);

}
