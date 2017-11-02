package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.InvoiceSettingUpdateInput;

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
     * @param supplierId 供应商ID
     */
    void saveInvoiceSetting(Long supplierId);

    /**
     * 修改供应商发票设置
     * @param invoiceSettingUpdateInput 供应商发票设置实体
     * @param supplierId 供应商ID
     * @return result
     */
    Result updateInvoiceSetting(InvoiceSettingUpdateInput invoiceSettingUpdateInput, Long supplierId);

    /**
     * 根据供应商Id查询供应商发票设置
     * @param supplierId 供应商Id
     * @return result
     */
    Result searchBySupplierId(Long supplierId);

    /**
     * app端-根据供应商Id查询供应商发票设置
     * @param supplierId 供应商Id
     * @return result
     */
    Result getBySupplierId(Long supplierId);

}
