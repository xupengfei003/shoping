package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.CommImgeTmp;

import java.util.List;

@Mapper
public interface CommImgeTmpDao {

    /**
     * 新增
     */
    void batchSave(List<CommImgeTmp> commImgeTmps);

    /**
     * 查询商品大图集合
     * @param scaId 供应商商品Id
     * @return 查询结果结合
     */
    List<CommImgeTmp> findTmp(@Param("scaId") Long scaId);
}
