package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.DistributionScope;
import so.sao.shop.supplier.domain.FreightRules;
import so.sao.shop.supplier.pojo.input.FreightRulesInput;

import java.util.List;

@Mapper
public interface FreightRulesDao {
    /**
     * 插入一条配送规则
     * @param freightRules 配送规则
     */
    void insert(FreightRules freightRules);

    /**
     * 分页获取供应商配送规则列表
     * @param accountId accountId
     * @param rulesType 配送规则类型
     */
    List<FreightRules> queryAll(@Param("accountId") Long accountId,@Param("rulesType") Integer rulesType);

    /**
     * 获取单个配送范围信息
     * @param id id
     * @return DistributionScope
     */
    FreightRules query(Integer id);

    /**
     * 更新某条配送范围信息
     * @param freightRules
     */
    void update(FreightRules freightRules);

    /**
     * 删除单条记录
     * @param id
     */
    void deleteByPrimaryKey(Integer id);

    /**
     * 根据配送范围ID删除运费规则
     * @param distributionScopeId 配送范围ID
     */
    void deleteByDistributionScopeId(Integer distributionScopeId);


}