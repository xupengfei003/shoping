package so.sao.shop.supplier.service;

import so.sao.shop.supplier.domain.Receipt;

/**
 * <p>Version: supplier V1.1.0 </p>
 * <p>Title: ReceiptService</p>
 * <p>Description: 发票与门店关系表 业务层</p>
 *
 * @author: zhenhai.zheng
 * @Date: Created in 2017/11/1 10:07
 */

public interface ReceiptService {

    /**
     * 发票记录录入
     * @param receipt 发票信息
     * @return
     */
    boolean insertReceipt(Receipt receipt);

    /**
     * 发票信息更改
     *  根据发票ID修改发票记录
     * @param receipt 发票信息
     * @return
     */
    boolean updateReceiptById(Receipt receipt);

    /**
     * 根据主键查询发票记录
     * @param receiptId
     * @return
     */
    Receipt getReceiptById(Long receiptId);

    /**
     * 根据门店ID和发票类型获取发票记录
     * @param userId
     * @param receiptType 1增值税普通单位发票 2增值税专用发票
     * @return
     */
    Receipt getReceiptByUserIdAndType(Long userId,Integer receiptType);
}
