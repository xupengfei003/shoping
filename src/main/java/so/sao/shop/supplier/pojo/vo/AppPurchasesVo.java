package so.sao.shop.supplier.pojo.vo;

import so.sao.shop.supplier.util.NumberUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by bzh on 2017/7/20.
 */
public class AppPurchasesVo {
    /**
     * 收货人
     */
    private String orderReceiverName;
    /**
     * 收货人电话
     */
    private String orderReceiverMobile;
    /**
     * 收货人地址
     */
    private String orderAddress;
    /**
     * 订单合计（订单实付金额）
     */
    private String orderPrice;
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 下单时
     */
    private Date orderCreateTime;
    /**
     * 供应商名称
     */
    private String storeName;
    /**
     * 订单状态
     */
    private Integer orderStatus;

    public String getOrderReceiverName() {
        return orderReceiverName;
    }

    public void setOrderReceiverName(String orderReceiverName) {
        this.orderReceiverName = orderReceiverName;
    }

    public String getOrderReceiverMobile() {
        return orderReceiverMobile;
    }

    public void setOrderReceiverMobile(String orderReceiverMobile) {
        this.orderReceiverMobile = orderReceiverMobile;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = NumberUtil.number2Thousand(new BigDecimal(orderPrice));
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }
}
