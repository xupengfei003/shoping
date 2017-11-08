package so.sao.shop.supplier.util;

import so.sao.shop.supplier.domain.Purchase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券规则工具类
 * @author gxy on 2017/11/1.
 */
public class CouponRulesUtil {

    /**
     * 计算优惠金额
     *
     * @param listPurchase 订单列表
     * @param usableValue 可用金额
     * @param couponValue 优惠券金额
     * @return 优惠后的商品价格表
     */
    public static List<Purchase> couponRule(List<Purchase> listPurchase, BigDecimal usableValue, BigDecimal couponValue) {

        //优惠前总金额
        BigDecimal beforeTotal = BigDecimal.valueOf(0);

        for (Purchase purchase : listPurchase) {
            beforeTotal = beforeTotal.add(purchase.getOrderPrice().add(purchase.getOrderPostage()));
        }
        //优惠后总金额
        BigDecimal afterTotal = new BigDecimal(0);

        for (Purchase purchase : listPurchase) {
            //每单价格
            BigDecimal orderPrice = purchase.getOrderPrice().add(purchase.getOrderPostage());
            //计算折扣优惠
            purchase.setDiscount(orderPrice.divide(beforeTotal, 2, RoundingMode.HALF_UP).multiply(couponValue));
            //计算合计金额
            purchase.setOrderTotalPrice(orderPrice.subtract(purchase.getDiscount()));

            afterTotal = afterTotal.add(orderPrice.subtract(purchase.getDiscount()));
        }
        //除不尽 误差值
        BigDecimal error = (afterTotal.add(couponValue)).subtract(beforeTotal);
        if (error.compareTo(BigDecimal.ZERO) != 0) {
            listPurchase.get(listPurchase.size()-1).setOrderTotalPrice(listPurchase.get(listPurchase.size()-1).getOrderTotalPrice().subtract(error));
            listPurchase.get(listPurchase.size()-1).setDiscount(listPurchase.get(listPurchase.size()-1).getDiscount().add(error));
        }
        return listPurchase;
    }
}
