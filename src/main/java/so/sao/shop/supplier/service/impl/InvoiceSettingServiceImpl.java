package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.InvoiceSettingDao;
import so.sao.shop.supplier.domain.InvoiceSetting;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.InvoiceSettingUpdateInput;
import so.sao.shop.supplier.pojo.output.AppInvoiceSettingOutput;
import so.sao.shop.supplier.pojo.output.InvoiceSettingOutput;
import so.sao.shop.supplier.service.InvoiceSettingService;
import so.sao.shop.supplier.util.BeanMapper;

import java.util.Date;

/**
 * <p>Version: New shop-1.1.0 V1.1.0 </p>
 * <p>Title: InvoiceSettingImpl</p>
 * <p>Description: </p>
 *
 * @author: zhaoyan
 * @Date: Created in 2017/10/31 17:54
 */
@Service
public class InvoiceSettingServiceImpl implements InvoiceSettingService {

    @Autowired
    private InvoiceSettingDao invoiceSettingDao;

    /**
     * 新增供应商发票设置
     * @param supplierId 供应商ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInvoiceSetting(Long supplierId) {
        InvoiceSetting invoiceSetting = new InvoiceSetting();
        invoiceSetting.setSupplierId(supplierId);
        invoiceSetting.setStatus(Constant.InvoiceSetting.STATUS_OFF);
        invoiceSetting.setInvoice(Constant.InvoiceSetting.INVOICE_OFF);
        invoiceSetting.setSpecialInvoice(Constant.InvoiceSetting.SPECIAL_INVOICE_OFF);
        invoiceSetting.setCreatedAt(new Date());
        invoiceSetting.setUpdatedAt(new Date());
        invoiceSettingDao.save(invoiceSetting);
    }

    /**
     * 修改供应商发票设置
     * @param invoiceSettingUpdateInput 供应商发票设置实体
     * @param supplierId 供应商ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateInvoiceSetting(InvoiceSettingUpdateInput invoiceSettingUpdateInput, Long supplierId) {
        InvoiceSettingOutput invoiceSettingOutput = invoiceSettingDao.findBySupplierId(supplierId);
        if(null == invoiceSettingOutput){
            return Result.fail("暂无该供应商发票设置信息，请与管理员取得联系！");
        }
        InvoiceSetting invoiceSetting = BeanMapper.map(invoiceSettingUpdateInput, InvoiceSetting.class);
        invoiceSetting.setSupplierId(supplierId);
        invoiceSetting.setUpdatedAt(new Date());
        invoiceSettingDao.update(invoiceSetting);
        return Result.success("修改供应商发票设置成功！");
    }

    /**
     * 根据供应商Id查询供应商发票设置
     * @param supplierId 供应商Id
     * @return 供应商发票设置
     */
    @Override
    public Result searchBySupplierId(Long supplierId) {
        InvoiceSettingOutput invoiceSettingOutput = invoiceSettingDao.findBySupplierId(supplierId);
        if(null == invoiceSettingOutput){
            return Result.fail("暂无该供应商发票设置信息，请与管理员取得联系！");
        }
        return Result.success("查询供应商发票设置成功！", invoiceSettingOutput);
    }

    /**
     * app端-根据供应商Id查询供应商发票设置
     * @param supplierId 供应商Id
     * @return 供应商发票设置
     */
    @Override
    public Result getBySupplierId(Long supplierId) {
        AppInvoiceSettingOutput appInvoiceSettingOutput = invoiceSettingDao.getBySupplierId(supplierId);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, appInvoiceSettingOutput);
    }
}
