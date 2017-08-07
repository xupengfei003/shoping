package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.SupplierCommodity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by QuJunLong on 2017/7/18.
 */
@Mapper
public interface SupplierCommodityDao {
    /**
     * 增加商品
     * @param supplierCommodity 商品信息对象
     * @return 商品对象
     */
    boolean save(SupplierCommodity supplierCommodity);
    /**
     * 删除商品
     * @param id 商品
     * @return 删除结果
     */
    boolean deleteById(@Param("id") long id);
    /**
     * 修改商品
     * @param supplierCommodity 商品信息对象
     * @return 修改结果
     */
    boolean update(SupplierCommodity supplierCommodity);
    /**
     * 查询商品
     * @param id 商品ID
     * @return  商品信息对象
     */
    SupplierCommodity findOne(@Param("id")long id);

    /**
     * 查询商品信息集合
     * @param supplierId 供应商ID
     * @param commCode69 商品编码
     * @param commId 商品ID
     * @param suppCommCode 商家商品编码
     * @param commName 商品名称
     * @param status 状态
     * @param typeId 类型ID
     * @param minPrice 价格（低）
     * @param maxPrice 价格（高）
     * @return 查询结果结合
     */
    List<SupplierCommodity> find(@Param("supplierId")Long supplierId, @Param("commCode69")String commCode69,
                                 @Param("commId") Long commId, @Param("suppCommCode") String suppCommCode,
                                 @Param("commName") String commName, @Param("status")Integer status, @Param("typeId") Long typeId,
                                 @Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);


    /**
     * 统计查询总记录数
     * @param supplierId 供应商ID
     * @param commCode69 商品编码
     * @param commId 商品ID
     * @param suppCommCode 商家商品编码
     * @param commName 商品名称
     * @param status 状态
     * @param typeId 类型ID
     * @param minPrice 价格（低）
     * @param maxPrice 价格（高）
     * @return 查询结果结合
     */
    Long countTotal(@Param("supplierId")Long supplierId,@Param("commCode69")String commCode69,
                         @Param("commId") Long commId, @Param("suppCommCode") String suppCommCode,
                         @Param("commName") String commName, @Param("status")Integer status,@Param("typeId") Long typeId,
                         @Param("minPrice") BigDecimal minPrice,@Param("maxPrice") BigDecimal maxPrice);

    /**
     * 更新商品状态
     * @param id 商品ID
     * @param status 商品状态
     * @return 更新结果
     */
    boolean updateStatus(@Param("id")long id, @Param("status") String status, @Param("updatedAt")Long updatedAt);

    /**
     * 根据商品ID查询商品状态
     * @param id 商品ID
     * @return 商品状态
     */
    int findStatus(@Param("id")long id);

    /**
     * 删除商品
     * @param id 商品
     * @return 删除结果
     */
    boolean deleteById(@Param("id") long id, @Param("deleted") Boolean deleted, @Param("updatedAt")Date updatedBy);

    /**
     * 上下架商品
     * @param supplierCommodity
     */
    boolean updateStatusSXj(SupplierCommodity supplierCommodity);

    /**
     *
     * @param code69
     * @return
     */
    SupplierCommodity findSupplierCommodityInfo(@Param("code69")String code69,@Param("supplierId") Long supplierId);


    /**
     * 查询所有商品信息集合
     * @param id scID
     * @param commName 商品名称
     * @param code69 商品编码
     * @param suppCommCode 商家商品编码
     * @param typeId 类型ID
     * @param minPrice 价格（低）
     * @param maxPrice 价格（高）
     * @return 查询结果结合
     */
    List<SupplierCommodity> findAll(@Param("id")Long id, @Param("commName") String commName, @Param("code69") String code69,
                                    @Param("suppCommCode") String suppCommCode, @Param("typeId") Long typeId,
                                    @Param("minPrice") BigDecimal minPrice,@Param("maxPrice") BigDecimal maxPrice);

    /**
     * 统计查询总记录数
     * @param id scID
     * @param commName 商品名称
     * @param suppCommCode 商家商品编码
     * @param code69 商品编码
     * @param typeId 类型ID
     * @param minPrice 价格（低）
     * @param maxPrice 价格（高）
     * @return 总记录条数
     */
    Long countAllTotal(@Param("id")Long id,@Param("commName") String commName, @Param("code69") String code69,
                       @Param("suppCommCode") String suppCommCode,@Param("typeId") Long typeId,
                       @Param("minPrice") BigDecimal minPrice,@Param("maxPrice") BigDecimal maxPrice);
}
