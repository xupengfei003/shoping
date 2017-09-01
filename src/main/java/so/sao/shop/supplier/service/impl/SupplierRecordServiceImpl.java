package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.SupplierRecordDao;
import so.sao.shop.supplier.domain.SupplierRecord;
import so.sao.shop.supplier.pojo.input.AccountInput;
import so.sao.shop.supplier.service.SupplierRecordService;
import so.sao.shop.supplier.util.PageTool;

import java.util.List;

/**
 * Created by acer on 2017/7/21.
 */
@Service
public class SupplierRecordServiceImpl implements SupplierRecordService {

    @Autowired
    private SupplierRecordDao supplierRecordDao;

    /**
     * 分页查询供应商上传记录
     * @param accountInput
     * @return 分页对象
     */
    @Override
    public PageInfo<SupplierRecord> searchAccountRecord(AccountInput accountInput) {
        PageTool.startPage(accountInput.getPageNum(), accountInput.getPageSize());
        List<SupplierRecord> supplierRecordList = supplierRecordDao.findPage(accountInput);
        PageInfo pageInfo = new PageInfo(supplierRecordList);
            return pageInfo;
    }
}
