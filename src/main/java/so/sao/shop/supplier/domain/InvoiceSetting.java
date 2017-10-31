package so.sao.shop.supplier.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>Version: New shop-1.0.0 V1.1.0 </p>
 * <p>Title: InvoiceSetting</p>
 * <p>Description: 供应商发票设置实体</p>
 *
 * @author: zhaoyan
 * @Date: Created in 2017/10/31 10:53
 */
public class InvoiceSetting {
    /**
     * ID
     */
    @Getter @Setter private Long id;
    /**
     * 供应商ID
     */
    @Getter @Setter private Long supplierId;
    /**
     * 供应商发票状态
     */
    @Getter @Setter private int status;
    /**
     * 增值税普通发票状态
     */
    @Getter @Setter private int invoice;
    /**
     * 增值税专用发票状态
     */
    @Getter @Setter private int specialInvoice;
    /**
     * 创建时间
     */
    @Getter @Setter private Date createdAt;
    /**
    * 更新时间
    */
    @Getter @Setter private Date updatedAt;
}
