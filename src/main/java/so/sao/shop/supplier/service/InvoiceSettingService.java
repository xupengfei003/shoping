package so.sao.shop.supplier.service;

import so.sao.shop.supplier.domain.InvoiceSetting;
import so.sao.shop.supplier.pojo.Result;

/**
 * <p>Version: New shop-1.0.0 V1.1.0 </p>
 * <p>Title: InvoiceSettingService</p>
 * <p>Description: 供应商发票设置service</p>
 *
 * @author: zhaoyan
 * @Date: Created in 2017/10/31 11:56
 */
public interface InvoiceSettingService {
    /**
     * 新增供应商发票设置
     * @param invoiceSetting 供应商发票设置实体
     */
    void saveInvoiceSetting(InvoiceSetting invoiceSetting);

    /**
     * 修改供应商发票设置
     * @param invoiceSetting 供应商发票设置实体
     */
    void updateInvoiceSetting(InvoiceSetting invoiceSetting);

    /**
     * 根据供应商Id查询供应商发票设置
     * @param supplierId 供应商Id
     * @return 供应商发票设置
     */
    Result searchBySupplierId(Long supplierId);
}
