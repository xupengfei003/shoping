package so.sao.shop.supplier.pojo.input;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Version: New shop-1.1.0 V1.1.0 </p>
 * <p>Title: InvoiceSettingUpdateInput</p>
 * <p>Description: 供应商发票设置输入对象</p>
 *
 * @author: zhaoyan
 * @Date: Created in 2017/11/1 10:15
 */
@Getter
@Setter
public class InvoiceSettingUpdateInput {
    /**
     * 供应商发票状态
     */
   private int status;
    /**
     * 增值税普通发票状态
     */
   private int invoice;
    /**
     * 增值税专用发票状态
     */
   private int specialInvoice;
}
