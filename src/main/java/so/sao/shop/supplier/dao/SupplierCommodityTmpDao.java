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

}
