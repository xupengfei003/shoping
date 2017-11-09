package so.sao.shop.supplier.dao;


import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.Receipt;

/**
 * 发票持久化层
 * @author zhenhai.zheng
 */

public interface ReceiptDao {

    /**
     * 发票记录录入
     * @return
     */
    int insertReceipt(Receipt receipt);

    /**
     * 发票信息更改
     *  根据发票ID修改发票记录
     * @param receipt
     * @return
     */
    int updateReceiptById(Receipt receipt);

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
    Receipt getReceiptByUserIdAndType(@Param("userId") Long userId,@Param("receiptType") Integer receiptType);
}