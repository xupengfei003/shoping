package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.TyCommImge;

import java.util.List;

public interface TyCommImagDao {

    /**
     * 通过商品条码查询商品公共图片库
     * @param code69 商品条码
     * @return  公共图片集合
     */
    List<TyCommImge> findByCode69(@Param("code69") String code69);

    /**
     * 新增
     * @param tyCommImge
     */
    void save(TyCommImge tyCommImge);
}
