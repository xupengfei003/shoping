package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.dao.DistributionScopeDao;
import so.sao.shop.supplier.dao.FreightRulesDao;
import so.sao.shop.supplier.domain.DistributionScope;
import so.sao.shop.supplier.domain.FreightRules;
import so.sao.shop.supplier.pojo.input.DistributionScopeInput;
import so.sao.shop.supplier.service.DistributionScopeService;
import so.sao.shop.supplier.util.Ognl;
import so.sao.shop.supplier.util.PageTool;

import java.util.Date;
import java.util.List;

/**
 * @author gxy on 2017/9/18.
 */
@Service
public class DistributionScopeServiceImpl implements DistributionScopeService {

    @Autowired
    DistributionScopeDao distributionScopeDao;
    @Autowired
    FreightRulesDao freightRulesDao;
    /**
     * 增加配送范围
     * @param accountId 供应商ID
     * @param distributionScopeInput 配送范围信息
     * @throws Exception Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDistributionScope(Long accountId, DistributionScopeInput distributionScopeInput) throws Exception {
        DistributionScope distributionScope = new DistributionScope();
        distributionScope.setSupplierId(accountId);
        distributionScope.setAddressProvince(distributionScopeInput.getAddressProvince());
        distributionScope.setAddressCity(distributionScopeInput.getAddressCity());
        distributionScope.setAddressDistrict(distributionScopeInput.getAddressDistrict());
        distributionScope.setRemark(distributionScopeInput.getRemark());
        distributionScope.setCreatedAt(new Date());
        if(distributionScopeDao.insert(distributionScope) > 0){
            FreightRules freightRules = new FreightRules();
            freightRules.setSupplierId(accountId);
            freightRules.setAddressProvince(distributionScopeInput.getAddressProvince());
            freightRules.setAddressCity(distributionScopeInput.getAddressCity());
            freightRules.setAddressDistrict(distributionScopeInput.getAddressDistrict());
            freightRules.setCreatedAt(new Date());
            freightRules.setRulesType(1);   //规则类型:0-通用规则,1-配送地区物流费用规则
            // TODO 缺少配送范围逐渐

            freightRulesDao.insert(freightRules);
        }
    }

    /**
     * 分页获取供应商配送范围列表
     * @param accountId accountId
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return Result
     * @throws Exception Exception
     */
    @Override
    public List<DistributionScope> queryAll(Long accountId, Integer pageNum, Integer pageSize) throws Exception {
        PageTool.startPage(pageNum, pageSize);
        return distributionScopeDao.queryAll(accountId);
    }

    /**
     * 获取单个配送范围信息
     * @param id id
     * @return Result
     * @throws Exception Exception
     */
    @Override
    public DistributionScope query(Integer id) throws Exception {
        return distributionScopeDao.query(id);
    }

    /**
     * 更新某条配送范围信息
     * @param distributionScope 配送范围实体
     * @return Result
     * @throws Exception Exception
     */
    @Override
    public void update(DistributionScope distributionScope) {
        DistributionScope ds = distributionScopeDao.query(distributionScope.getId());
        if(Ognl.isNotNull(ds)){
            ds.setAddressProvince(distributionScope.getAddressProvince());
            ds.setAddressCity(distributionScope.getAddressCity());
            ds.setAddressDistrict(distributionScope.getAddressDistrict());
            ds.setRemark(distributionScope.getRemark());
            ds.setUpdateAt(new Date());
            distributionScopeDao.update(ds);
        }
    }

    /**
     * 删除某条记录
     * @param id id
     */
    @Override
    public void delete(Integer id) throws Exception {
        distributionScopeDao.deleteByPrimaryKey(id);
    }
}
