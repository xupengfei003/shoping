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
     * 物流单号
     */
    private String orderShipmentNumber;
    /**
     * 订单合计（订单实付金额）
     */
    private String orderPrice;
    /**
     * 产品数量
     */
    private Integer goodsAllNum;

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

    public String getOrderShipmentNumber() {
        return orderShipmentNumber;
    }

    public void setOrderShipmentNumber(String orderShipmentNumber) {
        this.orderShipmentNumber = orderShipmentNumber;
    }

    public List<AppPurchaseItemVo> getAppPurchaseItemVos() {
        return appPurchaseItemVos;
    }

    public void setAppPurchaseItemVos(List<AppPurchaseItemVo> appPurchaseItemVos) {
        this.appPurchaseItemVos = appPurchaseItemVos;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Integer getGoodsAllNum() {
        return goodsAllNum;
    }

    public void setGoodsAllNum(Integer goodsAllNum) {
        this.goodsAllNum = goodsAllNum;
    }
}
