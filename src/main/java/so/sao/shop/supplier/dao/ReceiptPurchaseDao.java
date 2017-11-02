package so.sao.shop.supplier.dao;


import so.sao.shop.supplier.domain.ReceiptPurchase;

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
}