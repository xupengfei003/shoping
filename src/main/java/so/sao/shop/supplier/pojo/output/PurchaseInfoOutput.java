package so.sao.shop.supplier.pojo.output;

import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.vo.PurchaseItemVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 订单返回实体
 * </p>
 *
 * @author 透云-中软-西安项目组-wh
 * @since 2017-07-19
 */
public class PurchaseInfoOutput extends BaseResult {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 收货人姓名
     */
    private String orderReceiverName;

    /**
     * 收货人电话
     */
    private String orderReceiverMobile;

    /**
     * 订单状态
     */
    private Short orderStatus;

    /**
     * 订单金额
     */
    private BigDecimal orderPrice;

    /**
     * 下单时间
     */
    private Long orderCreateTime;

    /**
     * 支付方式
     */
    private Integer orderPaymentMethod;

    /**
     * 支付时间
     */
    private Long orderPaymentTime;

    /**
     * 支付流水号
     */
    private String orderPaymentNum;

    /**
     * 配送方式
     */
    private Integer orderShipMethod;

    /**
     * 退款时间
     */
    private Long drawbackTime;

    /**
     * 配送人姓名
     */
    private String distributorName;

    /**
     * 配送人电话
     */
    private String distributorMobile;

    /**
     * 物流公司
     */
    private String logisticsCompany;

    /**
     * 物流单号
     */
    private String orderShipmentNumber;

    /**
     * 订单明细列表
     */
    private List<PurchaseItemVo> purchaseItemVoList;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

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

    public Short getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Short orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Long getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Long orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public Integer getOrderPaymentMethod() {
        return orderPaymentMethod;
    }

    public void setOrderPaymentMethod(Integer orderPaymentMethod) {
        this.orderPaymentMethod = orderPaymentMethod;
    }

    public Long getOrderPaymentTime() {
        return orderPaymentTime;
    }

    public void setOrderPaymentTime(Long orderPaymentTime) {
        this.orderPaymentTime = orderPaymentTime;
    }

    public String getOrderPaymentNum() {
        return orderPaymentNum;
    }

    public void setOrderPaymentNum(String orderPaymentNum) {
        this.orderPaymentNum = orderPaymentNum;
    }

    public Integer getOrderShipMethod() {
        return orderShipMethod;
    }

    public void setOrderShipMethod(Integer orderShipMethod) {
        this.orderShipMethod = orderShipMethod;
    }

    public Long getDrawbackTime() {
        return drawbackTime;
    }

    public void setDrawbackTime(Long drawbackTime) {
        this.drawbackTime = drawbackTime;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getDistributorMobile() {
        return distributorMobile;
    }

    public void setDistributorMobile(String distributorMobile) {
        this.distributorMobile = distributorMobile;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getOrderShipmentNumber() {
        return orderShipmentNumber;
    }

    public void setOrderShipmentNumber(String orderShipmentNumber) {
        this.orderShipmentNumber = orderShipmentNumber;
    }

    public List<PurchaseItemVo> getPurchaseItemVoList() {
        return purchaseItemVoList;
    }

    public void setPurchaseItemVoList(List<PurchaseItemVo> purchaseItemVoList) {
        this.purchaseItemVoList = purchaseItemVoList;
    }
}
