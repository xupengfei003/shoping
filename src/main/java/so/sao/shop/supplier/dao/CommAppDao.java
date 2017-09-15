package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.pojo.output.*;

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
}
