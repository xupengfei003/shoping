package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.input.PayInput;

import java.math.BigDecimal;

/**
 * Created by acer on 2017/8/15.
 */
public interface PayService {
    /**
     * 支付回调接口
     *
     * @param payInput 封装了回调参数
     * @return Result 封装了结果
     * @throws Exception 异常
     */
    boolean updatePurchasePayment(PayInput payInput) throws Exception;

    /**
     * 支付回调接口(单订单支付)
     *
     * @param payInput 封装了回调参数
     * @return Result 封装了结果
     * @throws Exception 异常
     */
    boolean updatePurchasePaymentByOrderId(PayInput payInput) throws Exception;

    /**
     * 根据支付ID获取支付总金额
     *
     * @param payId 合并支付ID
     * @return
     */
    BigDecimal getPayOrderTotalPriceByPayId(String payId);
}
