package so.sao.shop.supplier.dao;

import so.sao.shop.supplier.domain.InvoiceSetting;
import so.sao.shop.supplier.pojo.output.InvoiceSettingOutput;

/**
 * <p>Version: New shop-1.0.0 V1.1.0 </p>
 * <p>Title: InvoiceSettingDao</p>
 * <p>Description: 供应商发票设置DAO</p>
 *
 * @author: zhaoyan
 * @Date: Created in 2017/10/31 12:00
 */
public interface InvoiceSettingDao {
    /**
     * 新增供应商发票设置
     * @param invoiceSetting 供应商发票设置实体
     */
    void save(InvoiceSetting invoiceSetting);

    /**
     * 更新供应商发票设置
     * @param invoiceSetting 供应商发票设置实体
     */
    void update(InvoiceSetting invoiceSetting);

    /**
     * 根据供应商ID查询发票设置
     * @param supplierId 供应商ID
     * @return 发票设置出参对象
     */
    InvoiceSettingOutput findBySupplierId(Long supplierId);

}
