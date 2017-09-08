package so.sao.shop.supplier.pojo.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import so.sao.shop.supplier.pojo.vo.AppPurchaseItemVo;

import java.util.Date;
import java.util.List;

/**
 * Created by acer on 2017/9/7.
 */
public class AppPurchaseItemOutput {
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderCreateTime;
    /**
     * 供应商名称
     */
    private String storeName;
    /**
     * 订单状态
     */
    private Integer orderStatus;
    /**
     * 商品信息
     */
    private List<AppPurchaseItemVo> appPurchaseItemVos;

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
        this.orderPrice = orderPrice;
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

    public List<AppPurchaseItemVo> getAppPurchaseItemVos() {
        return appPurchaseItemVos;
    }

    public void setAppPurchaseItemVos(List<AppPurchaseItemVo> appPurchaseItemVos) {
        this.appPurchaseItemVos = appPurchaseItemVos;
    }
}
