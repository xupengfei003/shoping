package com.sao.so.shop.dao;

import com.sao.so.shop.domain.Condition;
import com.sao.so.shop.domain.SupplierRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by acer on 2017/7/21.
 */
@Mapper
public interface SupplierRecordDao {

    /**
     * 根据上传开始时间、结束时间、上传方式查询供应商上传记录
     * @param condition 查询条件对象
     * @return 供应商信息列表
     */
    public List<SupplierRecord> findPage(Condition condition);

}
