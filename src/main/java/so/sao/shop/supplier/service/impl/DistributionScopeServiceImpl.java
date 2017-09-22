package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.dao.AccountDao;
import so.sao.shop.supplier.dao.DistributionScopeDao;
import so.sao.shop.supplier.dao.FreightRulesDao;
import so.sao.shop.supplier.domain.DistributionScope;
import so.sao.shop.supplier.domain.FreightRules;
import so.sao.shop.supplier.pojo.input.DistributionScopeInput;
import so.sao.shop.supplier.service.DistributionScopeService;
import so.sao.shop.supplier.service.FreightRulesService;
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
    @Autowired
    AccountDao accountDao;
    @Autowired
    FreightRulesService freightRulesService;
    /**
     * 增加配送范围
     * @param accountId 供应商ID
     * @param distributionScopeInput 配送范围信息
     * @throws Exception Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createDistributionScope(Long accountId, DistributionScopeInput distributionScopeInput) throws Exception {
        /**
         * 1.新增配送范围
         * 2.获取到上一步新增配送范围记录的主键,新增配送规则
         */
        //先根据区查询是否存在该区的配送范围，如果有添加失败，提示已添加
        List<FreightRules> list = freightRulesDao.queryAll0(accountId,1);
        FreightRules freightRule = freightRulesService.matchAddress(distributionScopeInput.getAddressProvince(),distributionScopeInput.getAddressCity(),distributionScopeInput.getAddressDistrict(),list);
        if (null != freightRule){
            return false;
        }
        DistributionScope distributionScope = new DistributionScope();
        distributionScope.setSupplierId(accountId);
        distributionScope.setAddressProvince(distributionScopeInput.getAddressProvince());
        distributionScope.setAddressCity(distributionScopeInput.getAddressCity());
        distributionScope.setAddressDistrict(distributionScopeInput.getAddressDistrict());
        distributionScope.setRemark(distributionScopeInput.getRemark());
        distributionScope.setCreatedAt(new Date());
        if(distributionScopeDao.insert(distributionScope) > 0){//新增配送范围
            distributionScope.getId();//获取到上一步新增配送范围记录的主键
            FreightRules freightRules = new FreightRules();
            freightRules.setSupplierId(accountId);
            freightRules.setAddressProvince(distributionScopeInput.getAddressProvince());
            freightRules.setAddressCity(distributionScopeInput.getAddressCity());
            freightRules.setAddressDistrict(distributionScopeInput.getAddressDistrict());
            freightRules.setCreatedAt(new Date());
            freightRules.setRulesType(1);   //规则类型:0-通用规则,1-配送地区物流费用规则
            freightRules.setDistributionScopeId(distributionScope.getId()); //配送规则ID
            //新增配送规则
            freightRulesDao.insert(freightRules);
            return true;
        }
        return false;
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
     * @param id 配送范围ID
     * @param distributionScopeInput 配送范围实体
     * @return Result
     * @throws Exception Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Integer id,DistributionScopeInput distributionScopeInput) {
        /**
         * 1.更改配送范围记录
         * 2.更改配送规则中相关记录信息
         */
        DistributionScope ds = distributionScopeDao.query(id);//根据配送范围ID查找相应记录
        if(Ognl.isNotNull(ds)){
            //更新配送范围
            String province = distributionScopeInput.getAddressProvince();//省
            String city = distributionScopeInput.getAddressCity();//市
            String addressDistrict = distributionScopeInput.getAddressDistrict();//区
            String remart = distributionScopeInput.getRemark();//备注
            Date date = new Date();
            ds.setAddressProvince(province);//设置省份
            ds.setAddressCity(city);//设置市
            ds.setAddressDistrict(addressDistrict);//设置区
            ds.setRemark(remart);//设置备注
            ds.setUpdateAt(date);//设置更新时间
            distributionScopeDao.update(ds);//更改配送范围记录

            //更新配送规则中相关信息
            FreightRules freightRules = new FreightRules();
            freightRules.setAddressProvince(province);//设置省
            freightRules.setAddressCity(city);//设置市
            freightRules.setAddressDistrict(addressDistrict);//设置区
            freightRules.setUpdateAt(date);//设置更新时间
            freightRules.setRemark(remart);//设置备注
            freightRules.setDistributionScopeId(ds.getId());//设置关联配送范围ID
            freightRulesDao.update(freightRules);//更改配送规则中相关记录信息
        }
    }

    /**
     * 删除某条记录
     * @param id id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Integer id,Long accountId) throws Exception {
        /**
         * 1.根据商户ID查询当前商户默认运费规则类型
         * 2.判断运费规则类型是否为1(配送规则)
         */
        Integer rules = accountDao.findRulesById(accountId);
        if (null == rules || rules != 1){
            //删除配送范围某条记录
            distributionScopeDao.deleteByPrimaryKey(id);
            //删除配送规则某条记录
            freightRulesDao.deleteByDistributionScopeId(id);
            return true;
        }
        return false;
    }
}
