package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import so.sao.shop.supplier.domain.DistributionScope;

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
}