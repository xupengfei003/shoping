package so.sao.shop.supplier.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.util.NumberUtil;
import so.sao.shop.supplier.util.StringUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单表对应的vo类
 * </p>
 *
 * @author 透云-中软-西安项目组-zhenhai.zheng
 * @since 2017年7月20日
 **/
public class AccountPurchaseVo {
    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 订单状态 1.待付款2.待发货3.已发货4.已完成5.已拒收退款审核6.已退款7.已支付退款审核8.待付款已取消19.确认送达
     */
    private Integer orderStatus;

    /**
     * 商品金额小计
     */
    private BigDecimal orderPrice;

    /**
     * 运费金额
     */
    private BigDecimal orderPostage;

    /**
     * 折扣优惠
     */
    private BigDecimal discount;

    /**
     * 总计金额
     */
    private BigDecimal orderTotalPrice;

    /**
     * 实付金额
     */
    private BigDecimal payAmount;

    /**
     * 透云进货价小计
     */
    private BigDecimal orderSettlemePrice;

    /**
     * 结算金额
     */
    private BigDecimal tempSettlemePrice;

    /**
     * 付款时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date orderPaymentTime;

    /**
     * 支付类型 1.支付宝 2.微信
     */
    private Integer orderPaymentMethod;

    /**
     * 付款流水号
     */
    private String orderPaymentNum;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderPrice() {
        return NumberUtil.number2Thousand(orderPrice);
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderPostage() {
        return NumberUtil.number2Thousand(orderPostage);
    }

    public void setOrderPostage(BigDecimal orderPostage) {
        this.orderPostage = orderPostage;
    }

    public String getDiscount() {
        return NumberUtil.number2Thousand(discount);
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getOrderTotalPrice() {
        return NumberUtil.number2Thousand(orderTotalPrice);
    }

    public void setOrderTotalPrice(BigDecimal orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public String getPayAmount() {
        return NumberUtil.number2Thousand(payAmount);
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getOrderSettlemePrice() {
        return NumberUtil.number2Thousand(orderSettlemePrice);
    }

    public void setOrderSettlemePrice(BigDecimal orderSettlemePrice) {
        this.orderSettlemePrice = orderSettlemePrice;
    }

    public String getTempSettlemePrice() {
        return NumberUtil.number2Thousand(tempSettlemePrice);
    }

    public void setTempSettlemePrice(BigDecimal tempSettlemePrice) {
        this.tempSettlemePrice = tempSettlemePrice;
    }

    public Date getOrderPaymentTime() {
        return orderPaymentTime;
    }

    public void setOrderPaymentTime(Date orderPaymentTime) {
        this.orderPaymentTime = orderPaymentTime;
    }

    public Integer getOrderPaymentMethod() {
        return orderPaymentMethod;
    }

    public void setOrderPaymentMethod(Integer orderPaymentMethod) {
        this.orderPaymentMethod = orderPaymentMethod;
    }

    public String getOrderPaymentNum() {
        return orderPaymentNum;
    }

    public void setOrderPaymentNum(String orderPaymentNum) {
        this.orderPaymentNum = orderPaymentNum;
    }

    public static Object[] converData(AccountPurchaseVo accountPurchaseVo){
        Object[] data = new Object[12];

        if (null != accountPurchaseVo) {
            //订单编号
            data[0] = accountPurchaseVo.getOrderId();
            //订单状态:1.待付款2.待发货3.已发货4.已完成5.已拒收退款审核6.已退款7.已支付退款审核8.待付款已取消19.确认送达
            switch (accountPurchaseVo.getOrderStatus()) {
                case 1:
                    data[1] = "待付款";
                    break;
                case 2:
                    data[1] = "待发货";
                    break;
                case 3:
                    data[1] = "已发货";
                    break;
                case 4:
                    data[1] = "已完成";
                    break;
                case 5:
                    data[1] = "已拒收退款审核";
                    break;
                case 6:
                    data[1] = "已退款";
                    break;
                case 7:
                    data[1] = "已支付退款审核";
                    break;
                case 8:
                    data[1] = "待付款已取消";
                    break;
                case 19:
                    data[1] = "确认送达";
                    break;
                default:
                    data[1] = "";
                    break;
            }

            //商品金额小计
            data[2] = accountPurchaseVo.getOrderPrice();
            //运费金额
            data[3] = accountPurchaseVo.getOrderPostage();
            //折扣优惠
            data[4] = accountPurchaseVo.getDiscount();
            //总计金额
            data[5] = accountPurchaseVo.getOrderTotalPrice();
            //实付金额
            data[6] = accountPurchaseVo.getPayAmount();
            //透云进货价小计
            data[7] = accountPurchaseVo.getOrderSettlemePrice();
            //结算金额
            data[8] = accountPurchaseVo.getTempSettlemePrice();
            //付款时间
            data[9] = accountPurchaseVo.getOrderPaymentTime() == null ? "" : StringUtil.fomateData(accountPurchaseVo.getOrderPaymentTime(), "yyyy-MM-dd HH:mm:ss");
            //支付类型 1.支付宝 2.微信
            Integer orderPaymentMethod = accountPurchaseVo.getOrderPaymentMethod();
            if (orderPaymentMethod != null && orderPaymentMethod.equals(Constant.PaymentStatusConfig.ALIPAY)) {
                data[10] = "支付宝";
            } else if (orderPaymentMethod != null && orderPaymentMethod.equals(Constant.PaymentStatusConfig.WECHAT)) {
                data[10] = "微信";
            } else {
                data[10] = "";
            }
            //付款流水号
            data[11] = accountPurchaseVo.getOrderPaymentNum();
        }
        return data;
    }

}
