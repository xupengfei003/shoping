package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.SupplierRecordDao;
import so.sao.shop.supplier.domain.Condition;
import so.sao.shop.supplier.domain.SupplierRecord;
import so.sao.shop.supplier.service.SupplierRecordService;

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
