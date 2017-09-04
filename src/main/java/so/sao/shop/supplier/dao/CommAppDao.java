package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.pojo.output.CategoryOutput;
import so.sao.shop.supplier.pojo.output.CommAppSeachOutput;

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
    List<Long> searchSuppliersByCode69(String code69);

	/**
     * 查询所有商品科属
     */
    List<CategoryOutput> searchCategories();
}
