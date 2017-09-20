package so.sao.shop.supplier.dao.app;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.pojo.output.*;
import so.sao.shop.supplier.pojo.vo.CategoryVo;

import java.math.BigDecimal;
import java.util.List;

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
     * 根据供应商ID或商品名称或分类或品牌ID查询商品信息
     *
     * @param supplierId 供应商
     * @param commName 商品名称
     * @param categoryOneId 一级分类id
     * @param categoryTwoId 二级分类id
     * @param categoryThreeId 三级分类id
     * @param brandIds 品牌id集合
     * @return 商品详情列表
     */
    List<CommAppOutput> findCommodities(@Param("supplierId")Long supplierId, @Param("commName") String commName, @Param("categoryOneId") Long categoryOneId, @Param("categoryTwoId") Long categoryTwoId, @Param("categoryThreeId") Long categoryThreeId, @Param("ids") Long[] brandIds);

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
    List<CommAppOutput> findCommoditiesByConditionOrder( @Param("categoryTwoId") Long categoryTwoId, @Param("categoryThreeId")Long categoryThreeId,
                                                         @Param("ids")Long[] brandIds,
                                                         @Param("orderPrice") String orderPrice);


}
