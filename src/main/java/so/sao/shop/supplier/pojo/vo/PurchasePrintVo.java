package so.sao.shop.supplier.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单打印页面信息的Vo对象
 *
 * @author hengle.yang
 * @since 2017/8/14 14:48
 */
public class PurchasePrintVo {

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 客户
     */
    private String customer;

    /**
     * 客户电话
     */
    private String customerPhone;

    /**
     * 收货地址
     */
    private String receivingAddress;

    /**
     * 下单时间
     */
    private Date orderCreateTime;

    /**
     * 合计
     */
    private BigDecimal totalPrice;

    /**
     * 二维码地址
     */
    private String qrcodeUrl;

    /**
     * 二维码状态，默认0：正常，1：失效
     */
    private Integer qrcodeStatus;

    /**
     * 供应商名称
     */
    private String providerName;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getReceivingAddress() {
        return receivingAddress;
    }

    public void setReceivingAddress(String receivingAddress) {
        this.receivingAddress = receivingAddress;
    }

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public Integer getQrcodeStatus() {
        return qrcodeStatus;
    }

    public void setQrcodeStatus(Integer qrcodeStatus) {
        this.qrcodeStatus = qrcodeStatus;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PurchasePrintVo that = (PurchasePrintVo) o;

        if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
        if (customer != null ? !customer.equals(that.customer) : that.customer != null) return false;
        if (customerPhone != null ? !customerPhone.equals(that.customerPhone) : that.customerPhone != null)
            return false;
        if (receivingAddress != null ? !receivingAddress.equals(that.receivingAddress) : that.receivingAddress != null)
            return false;
        if (orderCreateTime != null ? !orderCreateTime.equals(that.orderCreateTime) : that.orderCreateTime != null)
            return false;
        if (totalPrice != null ? !totalPrice.equals(that.totalPrice) : that.totalPrice != null) return false;
        if (qrcodeUrl != null ? !qrcodeUrl.equals(that.qrcodeUrl) : that.qrcodeUrl != null) return false;
        if (qrcodeStatus != null ? !qrcodeStatus.equals(that.qrcodeStatus) : that.qrcodeStatus != null) return false;
        return providerName != null ? providerName.equals(that.providerName) : that.providerName == null;
    }

    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (customerPhone != null ? customerPhone.hashCode() : 0);
        result = 31 * result + (receivingAddress != null ? receivingAddress.hashCode() : 0);
        result = 31 * result + (orderCreateTime != null ? orderCreateTime.hashCode() : 0);
        result = 31 * result + (totalPrice != null ? totalPrice.hashCode() : 0);
        result = 31 * result + (qrcodeUrl != null ? qrcodeUrl.hashCode() : 0);
        result = 31 * result + (qrcodeStatus != null ? qrcodeStatus.hashCode() : 0);
        result = 31 * result + (providerName != null ? providerName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PurchasePrintVo{" +
                "orderId='" + orderId + '\'' +
                ", customer='" + customer + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", receivingAddress='" + receivingAddress + '\'' +
                ", orderCreateTime=" + orderCreateTime +
                ", totalPrice=" + totalPrice +
                ", qrcodeUrl='" + qrcodeUrl + '\'' +
                ", qrcodeStatus=" + qrcodeStatus +
                ", providerName='" + providerName + '\'' +
                '}';
    }
}
