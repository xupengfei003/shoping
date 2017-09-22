package so.sao.shop.supplier.service;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.FreightRules;
import so.sao.shop.supplier.pojo.input.FreightRulesInput;

import java.util.List;

/**
 * @author gxy on 2017/9/18.
 */
public interface FreightRulesService {
    /**
     * 插入一条配送规则（通用规则）
     * @param accountId  供应商ID
     * @param freightRulesInput 配送规则
     */
    boolean insert(Long accountId,FreightRulesInput freightRulesInput);

    /**
     * 分页获取供应商配送规则列表
     * @param accountId accountId
     */
    List<FreightRules> queryAll(Long accountId,Integer pageNum,Integer pageSize,Integer rulesType);

    /**
     * 获取单个配送规则
     * @param id id
     * @return DistributionScope
     */
    FreightRules query(Integer id);

    /**
     * 更新某条配送规则
     * @param id 配送规则ID
     * @param freightRulesInput
     */
    boolean update(Long accountId,Integer id,FreightRulesInput freightRulesInput);

    /**
     *  删除通用配送规则记录
     * @param id
     */
    boolean deleteByPrimaryKey(Integer id,Long accountId);

    int count(Long accountId);

    /**
     * 分页获取供应商配送规则列表（省市区为code）
     * @param accountId
     * @param rules
     * @return
     */
    List<FreightRules> queryAll0(@Param("accountId")Long accountId, @Param("rules")Integer rules);


    FreightRules matchAddress(String province,String city,String district,List<FreightRules> freightRulesList);
}
