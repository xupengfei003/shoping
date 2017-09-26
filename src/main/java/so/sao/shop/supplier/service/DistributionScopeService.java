package so.sao.shop.supplier.service;

import so.sao.shop.supplier.domain.DistributionScope;
import so.sao.shop.supplier.pojo.input.DistributionScopeInput;

import java.util.List;

/**
 * @author gxy on 2017/9/18.
 */
public interface DistributionScopeService {
    /**
     * 增加配送范围
     * @param accountId 供应商ID
     * @param distributionScopeInput 配送范围信息
     * @throws Exception Exception
     */
    boolean createDistributionScope(Long accountId, DistributionScopeInput distributionScopeInput) throws Exception;

    /**
     * 分页获取供应商配送范围列表
     * @param accountId accountId
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return List<DistributionScope>
     * @throws Exception Exception
     */
    List<DistributionScope> queryAll(Long accountId, Integer pageNum, Integer pageSize) throws Exception;

    /**
     * 获取单个配送范围信息
     * @param id id
     * @return DistributionScope
     * @throws Exception Exception
     */
    DistributionScope query(Integer id) throws Exception;

    /**
     * 更新某条配送范围信息
     *
     * @param accountId
     * @param id 配送范围ID
     * @param distributionScopeInput 配送范围实体
     * @throws Exception Exception
     */
    boolean update(Long accountId, Integer id, DistributionScopeInput distributionScopeInput);
    /**
     * 删除某条记录
     * @param id id
     * @throws Exception Exception
     */
    boolean delete(Integer id,Long accountId) throws Exception;
}
