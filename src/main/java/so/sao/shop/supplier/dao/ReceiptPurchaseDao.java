package so.sao.shop.supplier.dao;


import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.ReceiptPurchase;

import java.util.List;

/**
 * 发票与订单持久化层
 * @author zhenhai.zheng
 */
public interface ReceiptPurchaseDao {

    /**
     * 订单详情-发票详情
     * @param orderId 订单ID
     * @return 结果
     */
    ReceiptPurchase findReceiptItemByOrderId(String orderId);

    /**
     * 订单详情-发票详情批量录入
     * @param receiptPurchases 订单ID
     * @return 结果
     */
    int insertReceiptItems(@Param("receiptPurchases") List<ReceiptPurchase> receiptPurchases);
}