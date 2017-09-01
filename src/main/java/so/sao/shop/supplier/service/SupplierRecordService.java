package so.sao.shop.supplier.service;

import com.github.pagehelper.PageInfo;
import so.sao.shop.supplier.domain.SupplierRecord;
import so.sao.shop.supplier.pojo.input.AccountInput;

/**
 * Created by acer on 2017/7/21.
 */
public interface SupplierRecordService {
    /**
     * 分页查询供应商上传记录
     * @param accountInput
     * @return 分页对象
     */
    PageInfo<SupplierRecord> searchAccountRecord(AccountInput accountInput);
}
