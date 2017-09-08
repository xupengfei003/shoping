package so.sao.shop.supplier.pojo.output;

import so.sao.shop.supplier.pojo.vo.PurchaseItemPrintVo;
import so.sao.shop.supplier.util.NumberUtil;
import so.sao.shop.supplier.util.StringUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 打印页面所有信息封装为output对象
 *
 * @author hengle.yang
 * @since 2017/8/10 18:00
 */
public class PurchaseItemPrintOutput{

    /**
     * 供应商名称
     */
    private String providerName;

    /**
     * 订单编号
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
     * 下单时间
     */
    private String orderCreateTimeStr;

    /**
     * 合计
     */
    private BigDecimal totalPrice;

    /**
     * 合计格式化（千分位）
     */
    private String totalPriceFormat;

    /**
     * 合计（合计金额中文）
     */
    private String totalPriceCN;

    /**
     * 二维码地址
     */
    private String qrcodeUrl;

    /**
     * 二维码状态，默认0：正常，1：失效
     */
    private Integer qrcodeStatus;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 商品明细列表
     */
    private List<PurchaseItemPrintVo> purchaseItemPrintVos;

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

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

    public String getTotalPriceCN() {
        return NumberUtil.number2CN(this.totalPrice);
    }

    public void setTotalPriceCN(String totalPriceCN) {
        this.totalPriceCN = totalPriceCN;
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

    public List<PurchaseItemPrintVo> getPurchaseItemPrintVos() {
        return purchaseItemPrintVos;
    }

    public void setPurchaseItemPrintVos(List<PurchaseItemPrintVo> purchaseItemPrintVos) {
        this.purchaseItemPrintVos = purchaseItemPrintVos;
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

    public String getTotalPriceFormat() {
        return NumberUtil.number2Thousand(this.totalPrice);
    }

    public void setTotalPriceFormat(String totalPriceFormat) {
        this.totalPriceFormat = totalPriceFormat;
    }

    public String getOrderCreateTimeStr() {
        return StringUtil.fomateData(this.orderCreateTime, "yyyy年MM月dd日");
    }

    public void setOrderCreateTimeStr(String orderCreateTimeStr) {
        this.orderCreateTimeStr = orderCreateTimeStr;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
}
