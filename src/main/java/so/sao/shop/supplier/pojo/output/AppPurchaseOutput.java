package so.sao.shop.supplier.pojo.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import so.sao.shop.supplier.pojo.vo.AppPurchaseItemVo;

import java.util.Date;
import java.util.List;

/**
 * Created by acer on 2017/9/7.
 */
public class AppPurchaseOutput {
    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 下单时间
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
    private Short orderStatus;

    /**
     * 商品信息
     */
    private List<AppPurchaseItemVo> appPurchaseItemVos;

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

    public Short getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Short orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<AppPurchaseItemVo> getAppPurchaseItemVos() {
        return appPurchaseItemVos;
    }

    public void setAppPurchaseItemVos(List<AppPurchaseItemVo> appPurchaseItemVos) {
        this.appPurchaseItemVos = appPurchaseItemVos;
    }
}
