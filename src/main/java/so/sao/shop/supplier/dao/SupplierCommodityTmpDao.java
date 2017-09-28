package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.SupplierCommodityTmp;
import so.sao.shop.supplier.pojo.output.CommodityOutput;

@Mapper
public interface SupplierCommodityTmpDao {

    /**
     * 新增
     */
    void save(SupplierCommodityTmp supplierCommodityTmp);


    /** 通过scaid 查询商品编辑待审核数据表
     *
     * @param id
     * @return
     */
    SupplierCommodityTmp findSupplierCommodityTmpByScaId(@Param("id") Long id);
    /**
     * 查询编辑后的商品详情
     * @param id 商品ID
     * @return 商品信息对象
     */
    CommodityOutput findDetailTmp(@Param("id")long id);

    /**
     * 查询审核编辑后的商品详情
     * @param id 审核表ID
     * @return 商品信息对象
     */
    CommodityOutput findAuditDetailTmp(@Param("id")long id);

    /**
     * 通过审核表ID查询tmp表是否存在数据
     * @param id 审核表ID
     * @return
     */
    int countTemByScaId(@Param("id")long id);
}
