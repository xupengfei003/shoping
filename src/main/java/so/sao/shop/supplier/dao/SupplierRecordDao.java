package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import so.sao.shop.supplier.domain.SupplierRecord;
import so.sao.shop.supplier.pojo.input.AccountInput;

import java.util.List;

/**
 * Created by acer on 2017/7/21.
 */
@Mapper
public interface SupplierRecordDao {

    /**
     * 根据上传开始时间、结束时间、上传方式查询供应商上传记录
     * @param accountInput 查询条件对象
     * @return 供应商信息列表
     */
     List<SupplierRecord> findPage(AccountInput accountInput);

}
