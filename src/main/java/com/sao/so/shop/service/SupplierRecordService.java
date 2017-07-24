package com.sao.so.shop.service;

import com.github.pagehelper.PageInfo;
import com.sao.so.shop.domain.Condition;
import com.sao.so.shop.domain.SupplierRecord;

/**
 * Created by acer on 2017/7/21.
 */
public interface SupplierRecordService {
    /**
     * 分页查询供应商上传记录
     * @param condition
     * @return 分页对象
     */
    PageInfo<SupplierRecord> searchAccountRecord(Condition condition);
}
