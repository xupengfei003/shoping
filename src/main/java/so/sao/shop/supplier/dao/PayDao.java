package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by bzh on 2017/8/15.
 */
public interface PayDao {
    /**
     * 支付回调接口
     *
     * @param payInput 封装了回调参数
     * @return boolean
     * @throws Exception 异常
     */
    boolean updatePaymentByPayId(@Param("payInput") Map<String, Object> payInput) throws Exception;

    /**
     * 支付回调接口(单订单支付)
     *
     * @param payInput 封装了回调参数,orderId为单个订单ID
     * @return boolean
     * @throws Exception 异常
     */
    boolean updatePaymentByOrderId(@Param("payInput") Map<String, Object> payInput) throws Exception;

    /**
     * 根据订单编号获取支付编号
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    String findPayIdByOrderId(@Param("orderId") String orderId) throws Exception;

    /**
     * 根据支付ID获取支付总金额
     *
     * @param payId 合并支付ID
     * @return
     */
    BigDecimal findPayOrderTotalPrice(@Param("payId") String payId);

}
