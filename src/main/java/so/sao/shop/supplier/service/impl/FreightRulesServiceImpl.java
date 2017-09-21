package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.AccountDao;
import so.sao.shop.supplier.dao.FreightRulesDao;
import so.sao.shop.supplier.domain.FreightRules;
import so.sao.shop.supplier.pojo.input.FreightRulesInput;
import so.sao.shop.supplier.service.AccountService;
import so.sao.shop.supplier.service.FreightRulesService;
import so.sao.shop.supplier.util.BeanMapper;
import so.sao.shop.supplier.util.PageTool;

import java.math.BigDecimal;
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
            // TODO
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
    public boolean update(Integer id,FreightRulesInput freightRulesInput) {
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
            return true;
        }else {
            return false;
        }


    }

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

}
