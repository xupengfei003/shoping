package so.sao.shop.supplier.pojo.output;

/**
 * <p>Version: supplier2 V1.1.0 </p>
 * <p>Title: AppInvoiceSettingOutput</p>
 * <p>Description: </p>
 *
 * @author: zhenhai.zheng
 * @Date: Created in 2017/11/1 18:14
 */
public class AppInvoiceSettingOutput {

    /**
     * 供应商发票状态
     */
    private int isOpen;
    /**
     * 增值税普通发票状态
     */
    private int plainInvoice;
    /**
     * 增值税专用发票状态
     */
    private int specialInvoice;

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public int getPlainInvoice() {
        return plainInvoice;
    }

    public void setPlainInvoice(int plainInvoice) {
        this.plainInvoice = plainInvoice;
    }

    public int getSpecialInvoice() {
        return specialInvoice;
    }

    public void setSpecialInvoice(int specialInvoice) {
        this.specialInvoice = specialInvoice;
    }
}
