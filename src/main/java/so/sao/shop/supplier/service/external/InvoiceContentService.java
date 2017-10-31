package so.sao.shop.supplier.service.external;

import so.sao.shop.supplier.domain.external.InvoiceContent;
import so.sao.shop.supplier.pojo.Result;

/**
 * <p>Version: 运维平台 V0.9.0 </p>
 * <p>Title: InvoiceContent</p>
 * <p>Description:运维平台-发票内容service</p>
 *
 * @author: sha.chen
 * @Date: Created in 2017/10/30 15:00
 */
public interface InvoiceContentService {

    /**
     * 添加发票内容
     * @param invoiceContent
     * @return
     */
    Result saveInvocieContent(InvoiceContent invoiceContent);

    /**
     * 更新发票内容
     * @param invoiceContent
     * @return
     */
    Result updateInvoiceContent(InvoiceContent invoiceContent);

    /**
     * 根据id删除发票内容
     * @param id
     * @return
     */
    Result delete(Long id);

    /**
     * 查询发票内容列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    Result searchInvoiceContents(Integer pageNum, Integer pageSize);
}
