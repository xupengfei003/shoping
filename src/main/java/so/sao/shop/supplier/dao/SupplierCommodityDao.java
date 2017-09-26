package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.SupplierCommodity;
import so.sao.shop.supplier.pojo.input.CommSearchInput;
import so.sao.shop.supplier.pojo.output.CommodityBannerOut;
import so.sao.shop.supplier.pojo.output.CommodityOutput;
import so.sao.shop.supplier.pojo.vo.SuppCommSearchVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    void save(SupplierCommodity supplierCommodity);
    /**
     * 删除商品
     * @param id 商品
     * @return 删除结果
     */
    void deleteById(@Param("id") long id);

    /**
     * 批量删除商品
     * @param supplierCommodityList 商品集合
     * @return 删除结果
     */
    void deleteByIds(List supplierCommodityList);

    /**
     * 修改商品
     * @param supplierCommodity 商品信息对象
     * @return 修改结果
     */
    void update(SupplierCommodity supplierCommodity);

    /**
     * 查询商品
     * @param id 商品ID
     * @return  商品信息对象
     */
    SupplierCommodity findOne(@Param("id")long id);
	
    /**
     * 查询商品
     * @param id 商品ID
     * @return  商品信息对象
     */
    CommodityOutput findDetail(@Param("id")long id);

    /**
     * 根据id 查找商品审核表商品的状态
     * @param id  商品id
     * @return 商品状态
     */

    int findAuditStatus(@Param("id")long id);

    /**
     * 根据商品ID查询购物车所需字段
     * @param id
     * @return
     */
    SupplierCommodity findOneToCartItem (@Param("id")long id) throws Exception;

    /**
     *
     * @param code69
     * @return
     */
    SupplierCommodity findSupplierCommodityInfo(@Param("code69")String code69,@Param("supplierId") Long supplierId);

    /**
     * 根据id查询count
     * @param code69
     * @param supplierId
     * @return
     */
    int countByCode69(@Param("code69")String code69,@Param("supplierId") Long supplierId);

    /**
     * 查询商品信息集合
     * @param commSearchInput 高级搜索查询请求
     * @return 查询结果结合
     */
    List<SuppCommSearchVo> find(CommSearchInput commSearchInput);

    /**
     * 根据id查询count
     * @param id
     * @return
     */
    int countById(@Param("id") Long id);

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
     * 根据供应商商品ID查询商品状态
     * @param id 商品id
     * @return 商品状态
     */
    int findSupplierCommStatus(@Param("id")long id);


    /**
     * 删除商品
     * @param id 商品
     * @return 删除结果
     */
    boolean deleteById(@Param("id") long id, @Param("deleted") Boolean deleted, @Param("updatedAt")Date updatedBy);

    /**
     * 上下架商品（仅限待上架商品的下架与管理员上下架操作）
     * @param supplierCommodity
     */
    void onOrOffShelves(SupplierCommodity supplierCommodity);

    /**
     * 批量上下架商品（仅限待上架商品的批量下架与管理员批量上下架操作操作）
     * @param list
     */
    void onOrOffShelvesBatch(List<SupplierCommodity> list);

	/**
     * 查询商品信息集合
     * @param supplierId 供应商ID
     * @param inputvalue 输入值（商品条码/商品名称）
     * @param beginCreateAt 创建时间（前）
     * @param endCreateAt 创建时间（后）
     * @return 查询结果集
     */
    List<SuppCommSearchVo> findSimple(@Param("supplierId")Long supplierId, @Param("inputvalue")String inputvalue,
                                      @Param("beginCreateAt") Date beginCreateAt, @Param("endCreateAt") Date endCreateAt);

    /**
     * 根据商品标签ID查询未删除状态的供应商商品
     * @param tagId 商品标签ID
     * @return int 在使用标签的商品数量
     */
    int countSupplierCommodityByTagId(@Param("tagId")Long tagId);

    /**
     * 根据计量规格主键commMeasureSpecId 查所有引用到的未删除状态的SupplierCommodity
     * @param commMeasureSpecId 计量规格主键ID
     * @return List<SupplierCommodity>
     */
    int countSupplierCommodityById( Long commMeasureSpecId);
    /**
     *根据商品单位id查询商品单位是否被使用
     * @param unitId 商品单位id
     * @return List<SupplierCommodity>
     */
    List<SupplierCommodity> findSupplierCommodityByUnitId(@Param("unitId") Long unitId);
    /**
     * 商品单位是否被sc表引用
     * @param unitId
     * @return int
     */
    int countByUnitId(@Param("unitId") Long unitId);

    /**
     * 根据id数组查询，过滤已删除的商品
     * @param ids
     * @return
     */
    List<SupplierCommodity> findSupplierCommodityByIds(@Param("ids") Long[] ids);

    /**
     * 商品失效
     * @param
     */
    void updateInvalidStatus(SupplierCommodity supplierCommodity);

    /**
     * 根据map中的key（goodsId）、value（inventory）修改库存
     *
     * @param map
     * @return
     */
     int updateInventoryByGoodsId(@Param("map")Map map);

    /**
     * 根据id更新失效状态
     * @param id
     * @param invalidStatus
     * @return
     */
    void updateInvalidStatusById(@Param("id")long id, @Param("invalidStatus ") int invalidStatus, @Param("updatedAt ") Date updatedAt);

    /**
     * 根据supplierId获取规格列表
     * @param supplierId
     * @return int
     */
    List<SupplierCommodity> findBySupplierId(@Param("supplierId") Long supplierId);

    /**
     * 修改商品
     * @param supplierCommodity 商品信息对象
     * @return 修改结果
     */
    void updateAmz(SupplierCommodity supplierCommodity);

    /**
     *更新SC 表中的缩略图 字段
     * @param minImg
     * @param updatedAt
     * @param updatedBy
     * @param scId
     * @return
     */
    void updateMinImg(@Param("minImg")String minImg, @Param("updatedAt")Date updatedAt, @Param("updatedBy")Long updatedBy, @Param("scId") Long scId);

    /**
     * 根据商品名称商品类型查询商品信息
     * @param commodityName 商品名称
     * @param categoryOneId 商品类型一
     * @param categoryTwoId 商品类型二
     * @param categoryThreeId 商品类型三
     * @return 轮播图商品信息列表
     */
    List<CommodityBannerOut> findCommByNameAndCategory(@Param("commodityName") String commodityName,@Param("categoryOneId") Long categoryOneId,@Param("categoryTwoId") Long categoryTwoId,@Param("categoryThreeId") Long categoryThreeId);


    /**
     * 根据id更新SupplierCommodity表 status状态
     * @param id
     * @param status
     * @param updatedAt
     * @return
     */
    void updateSupplierCommodityStatusById(@Param("id")Long id, @Param("status") int status, @Param("updatedAt") Date updatedAt);



}
