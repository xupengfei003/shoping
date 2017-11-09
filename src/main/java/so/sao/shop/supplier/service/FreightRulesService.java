package so.sao.shop.supplier.service;

import so.sao.shop.supplier.domain.FreightRules;
import so.sao.shop.supplier.pojo.input.FreightRulesInput;

import java.util.List;

/**
 * @author gxy on 2017/9/18.
 */
public interface FreightRulesService {

    /**
     * 插入一条配送规则（通用规则）
     *
     * @param accountId         供应商ID
     * @param freightRulesInput 配送规则
     */
    boolean insert(Long accountId, FreightRulesInput freightRulesInput);

    /**
     * 分页获取供应商配送规则列表
     *
     * @param accountId accountId
     */
    List<FreightRules> queryAll(Long accountId, Integer pageNum, Integer pageSize, Integer rulesType);

    /**
     * 获取单个配送规则
     *
     * @param id id
     * @return DistributionScope
     */
    FreightRules query(Integer id);

    /**
     * 更新某条配送规则
     *
     * @param id                配送规则ID
     * @param freightRulesInput
     */
    boolean update(Long accountId, Integer id, FreightRulesInput freightRulesInput);

    /**
     * 删除通用配送规则记录
     *
     * @param id
     */
    boolean deleteByPrimaryKey(Integer id, Long accountId);

    /**
     * 查询规则数量
     *
     * @param accountId
     * @return
     */
    int count(Long accountId);


    /**
     * 根据省市区code匹配配送规则
     *
     * @param province 省
     * @param city 市
     * @param district 区
     * @param freightRulesList 要匹配的配送规则集合
     * @return
     */
    FreightRules matchAddress(String province, String city, String district, List<FreightRules> freightRulesList);

    /**
     * 分页获取供应商配送规则列表
     *
     * @param accountId accountId
     */
    List<FreightRules> queryAll0(Long accountId, int rules);
}
