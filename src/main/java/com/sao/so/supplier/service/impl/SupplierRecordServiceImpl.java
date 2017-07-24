package com.sao.so.supplier.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sao.so.supplier.dao.SupplierRecordDao;
import com.sao.so.supplier.domain.Condition;
import com.sao.so.supplier.domain.SupplierRecord;
import com.sao.so.supplier.service.SupplierRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by acer on 2017/7/21.
 */
@Service
public class SupplierRecordServiceImpl implements SupplierRecordService {

    @Autowired
    private SupplierRecordDao supplierRecordDao;

    /**
     * 分页查询供应商上传记录
     * @param condition
     * @return 分页对象
     */
    @Override
    public PageInfo<SupplierRecord> searchAccountRecord(Condition condition) {
            if(condition.getBeginTime()!=null){
                condition.setBeginDate(condition.getBeginTime().getTime());
            }
            if(condition.getContractCreateTime()!=null){
                condition.setContractCreateDate(condition.getContractCreateTime().getTime());
            }
            if(condition.getPageNum()==null){
                condition.setPageNum(1);
            }
            if(condition.getPageSize()==null){
                condition.setPageSize(10);
            }
            Page page = PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
            PageInfo pageInfo = new PageInfo(supplierRecordDao.findPage(condition));
            return pageInfo;
    }
}
