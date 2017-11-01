package so.sao.shop.supplier.service.impl;

import so.sao.shop.supplier.domain.InvoiceSetting;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.InvoiceSettingService;

/**
 * <p>Version: New shop-1.1.0 V1.1.0 </p>
 * <p>Title: InvoiceSettingImpl</p>
 * <p>Description: </p>
 *
 * @author: zhaoyan
 * @Date: Created in 2017/10/31 17:54
 */
public class InvoiceSettingImpl implements InvoiceSettingService {
    /**
     * 新增供应商发票设置
     * @param invoiceSetting 供应商发票设置实体
     */
    @Override
    public void saveInvoiceSetting(InvoiceSetting invoiceSetting) {

    }

    /**
     * 修改供应商发票设置
     * @param invoiceSetting 供应商发票设置实体
     */
    @Override
    public void updateInvoiceSetting(InvoiceSetting invoiceSetting) {

    }

    /**
     * 根据供应商Id查询供应商发票设置
     * @param supplierId 供应商Id
     * @return 供应商发票设置
     */
    @Override
    public Result searchBySupplierId(Long supplierId) {
        return null;
    }
}
