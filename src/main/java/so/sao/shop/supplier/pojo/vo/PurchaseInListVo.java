package so.sao.shop.supplier.pojo.vo;

import so.sao.shop.supplier.util.NumberUtil;

import java.math.BigDecimal;

/**
 * <p>
 * //订单明细所属订单类
 * </p>
 *
 * @author 透云-中软-西安项目组-zhenhai.zheng
 * @since 2017年8月14日 09:58:00
 **/
public class PurchaseInListVo {
    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 订单总金额
     */
    private String orderPrice;

    /**
     * 收货人姓名
     */
    private String orderReceiverName;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderReceiverName() {
        return orderReceiverName;
    }

    public void setOrderReceiverName(String orderReceiverName) {
        this.orderReceiverName = orderReceiverName;
    }

    public PurchaseInListVo(String orderId, BigDecimal orderPrice, String orderReceiverName) {
        this.orderId = orderId;
        this.orderPrice = NumberUtil.number2Thousand(orderPrice);
        this.orderReceiverName = orderReceiverName;
    }
}
