package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import so.sao.shop.supplier.domain.SupplierCommodityTmp;

@Mapper
public interface SupplierCommodityTmpDao {

    /**
     * 新增
     */
    void save(SupplierCommodityTmp supplierCommodityTmp);

}
