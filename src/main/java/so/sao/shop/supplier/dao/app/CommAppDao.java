package so.sao.shop.supplier.dao.app;

import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;
import so.sao.shop.supplier.pojo.input.CommAppInput;
import so.sao.shop.supplier.pojo.input.CommodityAppInput;
import so.sao.shop.supplier.pojo.output.*;
import so.sao.shop.supplier.pojo.vo.CategoryVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CommAppDao {

    /**
     * 查询所有商品信息集合
     * @param sku SKU(商品ID)
     * @param supplierId 商家ID
     * @param code69 商品编码
     * @param suppCommCode 商家商品编码
     * @param inputValue 输入值（商品名称或分类名称）
     * @param minPrice 价格（低）
     * @param maxPrice 价格（高）
     * @return 查询结果结合
     */
    List<CommAppSeachOutput> findAll(@Param("supplierId") Long supplierId, @Param("sku")String sku, @Param("code69") String code69,
                                     @Param("suppCommCode") String suppCommCode, @Param("inputValue") String inputValue,
                                     @Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);



    /**
     * 根据code69查询供应商列表
     * @param code69
     * @return 返回供应商ID
     */
    List<AccountOutput> searchSuppliersByCode69(@Param("code69")String code69);

    /**
     * 查询所有商品科属
     */
    List<CategoryOutput> searchCategories();

    /**
     * 模糊查询品牌名称
     * @param name
     * @return list集合(属性保护ID,name)
     */
    List<CommBrandOutput> findBrandName(@Param("name") String name);

    /**
     * 根据code69或供应商ID或商品名称或分类或品牌ID查询商品信息
     *@param commAppInput
     * @return 商品详情列表
     */
    List<CommAppOutput> findCommodities(CommAppInput commAppInput);

    /**
     * 供应商主营分类
     *
     * @param supplierId 供应商
     * @return   商品分类
     */
    List<CategoryVo> findMainCateGory(@Param("supplierId") Long supplierId);

    /**
     * 根据科属的等级参数获取所有的2或3级科属
     * @param level
     * @return
     */
    List<CategoryOutput> findCategories(@Param("level") Integer level );

    /**
     *根据条件 获取所属类型下面的 商品的全部品牌
     * @return
     */
    List<CommBrandOutput> findAllBrands( @Param("categoryId") Integer categoryId );

    /**
     * 根据动态条件(供应商ID/分类/品牌ids/排序条件)查询商品
     * @param CommAppInput commAppInput
     * @return
     */
    List<CommAppOutput> findCommoditiesByConditionOrder( CommodityAppInput commodityAppInput );

    /**
     *
     * @param supplierId
     * @param commName
     * @return
     */
    List<CommodityOutput> listCommodities(@Param("supplierId") Long supplierId, @Param("commName") String commName);
    /**
     * 根据商品名称模糊查询商品，返回商品列表
     *
     * @param goodsName 商品名称
     * @return
     */
    List<Map> findGoodsByName(@Param("goodsName") String goodsName);

}
