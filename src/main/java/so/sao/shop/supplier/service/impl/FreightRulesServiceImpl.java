package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.dao.FreightRulesDao;
import so.sao.shop.supplier.domain.FreightRules;
import so.sao.shop.supplier.pojo.input.FreightRulesInput;
import so.sao.shop.supplier.service.AccountService;
import so.sao.shop.supplier.service.FreightRulesService;
import so.sao.shop.supplier.util.BeanMapper;
import so.sao.shop.supplier.util.PageTool;

import java.util.Date;
import java.util.List;

/**
 * @author gxy on 2017/9/18.
 *
 */
@Service
public class FreightRulesServiceImpl implements FreightRulesService {

    @Autowired
    FreightRulesDao freightRulesDao;
    @Autowired
    AccountService accountService;
    /**
     * 插入一条配送规则(通用规则）
     * @param freightRulesInput 配送规则
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(Long accountId,FreightRulesInput freightRulesInput) {
        /**
         * 1.查询通用规则记录是否存在，不存在返回false，存在则插入并返回true
         *
         */
        if (freightRulesDao.queryAll(accountId,0).isEmpty()){
            FreightRules freightRules = new FreightRules();
            freightRules.setRulesType(0);//规则类型:0-通用规则,1-配送地区物流费用规则
            freightRules.setWhetherShipping(freightRulesInput.getWhetherShipping());//设置是否包邮
            freightRules.setCreatedAt(new Date());//创建时间
            freightRules.setSupplierId(accountId);//供应商ID
            freightRules.setSendAmount(freightRulesInput.getSendAmount());//起送金额
            freightRules.setDefaultPiece(freightRulesInput.getDefaultPiece());//默认计件
            freightRules.setDefaultAmount(freightRulesInput.getDefaultAmount());//运费基础金额
            freightRules.setExcessPiece(freightRulesInput.getExcessPiece());//超量计件
            freightRules.setExcessAmount(freightRulesInput.getExcessAmount());//运费增加金额
            freightRules.setRemark(freightRulesInput.getRemark());//备注
            freightRulesDao.insert(freightRules);
            // TODO 判断配送规则是否填写完整， 若没有填写完整，则设为默认配送类型
            List<FreightRules> freightRulesList = freightRulesDao.queryAll(accountId,1);
            if (null == freightRulesList || freightRulesList.isEmpty()){
                accountService.updateRulesByFreightRules(accountId,0);
            }
            if (null != freightRulesList){
                for (FreightRules freightRule:freightRulesList) {
                    if (null == freightRule.getWhetherShipping()){
                        accountService.updateRulesByFreightRules(accountId,0);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 分页获取供应商配送规则列表
     * @param accountId accountId
     * @param rulesType  运费规则类型
     * @return
     */
    @Override
    public List<FreightRules> queryAll(Long accountId ,Integer pageNum ,Integer pageSize,Integer rulesType) {
        /**
         * 1.设置分页
         * 2.根据商户ID及配送规则类型查询集合
         */
        PageTool.startPage(pageNum,pageSize);
        return freightRulesDao.queryAll(accountId,rulesType);
    }

    /**
     * 获取单个配送规则
     * @param id id
     * @return
     */
    @Override
    public FreightRules query(Integer id) {
        return freightRulesDao.query(id);
    }

    /**
     * 更新某条配送规则
     * @param id
     * @param freightRulesInput
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Long accountId,Integer id,FreightRulesInput freightRulesInput) {
        /**
         * 1.判断配送规则入参对象中的配送规则类型是否为0(通用规则) ,若为0查询通用规则及配送规则ID是否匹配，匹配可修改
         * 2.修改配送规则记录
         */
        FreightRules freightRules = freightRulesDao.query(id);
        if (null == freightRules){
            return false;
        }
        if (freightRulesInput.getRulesType() == freightRules.getRulesType()){//入参中的配送规则类型和查出来的配送规则类型是否匹配
            freightRules = BeanMapper.map(freightRulesInput,freightRules.getClass());
            freightRules.setId(id);
            freightRules.setUpdateAt(new Date());
            freightRulesDao.update(freightRules);
            this.updateAccountRulesType(accountId);
            return true;
        }else {
            return false;
        }


    }

    /**
     * 更换商户默认配送规则类型
     * @param accountId
     */
    private void updateAccountRulesType(Long accountId) {
        List<FreightRules>  commonList = freightRulesDao.queryAll(accountId,0);//通用规则
        List<FreightRules>  dispatchingList = freightRulesDao.queryAll(accountId,1);//配送规则
        boolean flag = false;
        if (null != dispatchingList && !dispatchingList.isEmpty()){
            for (FreightRules feightRules:dispatchingList) {
                if (null == feightRules.getWhetherShipping()){
                    flag = true;
                    break;
                }
            }
        }
        if (null != commonList && !commonList.isEmpty()){
            flag = true;
        }
        if (!flag){
            accountService.updateRulesByFreightRules(accountId,1);
        }
;    }

    /**
     *  删除通用配送规则记录
     * @param id
     */
    @Override
    public boolean deleteByPrimaryKey(Integer id,Long accountId) {
        FreightRules freightRules = freightRulesDao.query(id);
        Integer rules = accountService.findRulesById(accountId);
        if (null != freightRules && 0 == freightRules.getRulesType() && ( null == rules || 0 != rules)){
            freightRulesDao.deleteByPrimaryKey(id);
            return true;
        }
        return false;
    }

    /**
     * 获取完整的配送规则种类数量
     *  返回0 表示有一种规则或都没有
     *  返回1 表示两种规则都有
     * @param accountId
     * @return
     */
    @Override
    public int count(Long accountId) {
       List<FreightRules> commonFreightRulesList = freightRulesDao.queryAll(accountId,0);//通用规则
       List<FreightRules> dispatchingFreightRulesList = freightRulesDao.queryAll(accountId,1);//配送规则
       if (null != commonFreightRulesList && !commonFreightRulesList.isEmpty() && null != dispatchingFreightRulesList && !dispatchingFreightRulesList.isEmpty()){
           for (FreightRules freightRules:dispatchingFreightRulesList) {
               if (null == freightRules.getWhetherShipping()){
                   return 0;
               }
           }
           return 1;
       }
       return 0;
    }


    /**
     * 地址匹配 ---配送范围与下单收货地址的匹配
     * @param freightRulesList 配送规则
     * @return
     */
    @Override
    public FreightRules matchAddress(String province,String city,String district,List<FreightRules> freightRulesList) {
        FreightRules rulesCity = null;
        FreightRules rulesProvince = null;
        for (FreightRules freightRules:freightRulesList) {
            if (null != freightRules.getWhetherShipping()){
                if (freightRules.getAddressDistrict() .equals(district)){//匹配区
                    return freightRules;
                }
                if (freightRules.getAddressCity().equals(city)){//匹配市
                    rulesCity = freightRules;
                }
                if (freightRules.getAddressProvince().equals( province)){//匹配省
                    rulesProvince =  freightRules;
                }
            }

        }
        if (null != rulesCity ){
            return rulesCity;
        }
        if (null != rulesProvince ){
            return rulesProvince;
        }
        return null;
    }

    @Override
    public List<FreightRules> queryAll0(Long accountId, int rules) {

        return freightRulesDao.queryAll0(accountId,rules);
    }


}
