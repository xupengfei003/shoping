package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.DistributionScope;
import so.sao.shop.supplier.domain.FreightRules;

import java.util.List;

@Mapper
public interface DistributionScopeDao {
    /**
     * 增加配送范围
     * @param distributionScope distributionScope
     */
    int insert(DistributionScope distributionScope);

    /**
     * 分页获取供应商配送范围列表
     * @param accountId accountId
     */
    List<DistributionScope> queryAll(Long accountId);

    /**
     * 获取单个配送范围信息
     * @param id id
     * @return DistributionScope
     */
    DistributionScope query(Integer id);

    /**
     * 更新某条配送范围信息
     * @param distributionScope
     */
    void update(DistributionScope distributionScope);

    /**
     * 删除单条记录
     * @param id
     */
    void deleteByPrimaryKey(Integer id);
    /**
     * 根据省市区code码获取对应记录
     * @param addressProvince
     * @param addressCity
     * @param addressDistrict
     * @return
     */
    FreightRules selectFreightRulesByCode(@Param("addressProvince") String addressProvince,@Param("addressCity") String addressCity,@Param("addressDistrict") String addressDistrict);
}