package so.sao.shop.supplier.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.apache.log4j.Logger;

import java.math.BigDecimal;

/**
 * @author gxy on 2017/9/22.
 */
public class AlipayRefundUtil {

    /**
     * ＜支付宝退款请求＞
     * @param orderId 退款唯一标示，相同标识只能退款一次
     * @param out_trade_no 订单支付pay_id
     * @param trade_no  支付宝交易流水号
     * @param refund_amount 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
     * @param cancelReason
     * @return String
     */
    public static String alipayRefundRequest(String orderId, String out_trade_no, String trade_no, BigDecimal refund_amount, String cancelReason) {
        // 发送请求
        String strResponse = null;
        try {
            AlipayClient alipayClient = new DefaultAlipayClient
                    (AlipayConfig.alipayurl,
                            AlipayConfig.appid, AlipayConfig.private_key, AlipayConfig.format, AlipayConfig.input_charset,
                            AlipayConfig.ali_public_key, AlipayConfig.content_type);
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            AlipayRefundInfo alidata = new AlipayRefundInfo();
            alidata.setOut_trade_no(out_trade_no);//商户订单号
            alidata.setRefund_amount(refund_amount);//需要退款的金额
            alidata.setTrade_no(trade_no);//支付宝交易号
            alidata.setRefund_reason(cancelReason);//退款原因
            alidata.setOut_request_no(orderId);//标识一次退款请求 orderId区分
            request.setBizContent(JsonUtils.convertToString(alidata));
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            strResponse = response.getCode();
            if (response.isSuccess()) {
                strResponse = "SUCCESS";
            } else {
                strResponse = "FAILD";
            }
        } catch (Exception e) {
            Logger.getLogger(AlipayRefundUtil.class).error("alipayRefundRequest", e);
        }
        return strResponse;
    }

    /**
     * ＜支付宝退款查询请求＞
     * @param trade_no 支付宝交易流水号
     * @param out_trade_no 订单支付pay_id
     * @param out_request_no 订单支付pay_id
     * @return String
     * @throws Exception
     */
    public static String alipayRefundQuery(String trade_no, String out_trade_no, String out_request_no) {
        // 发送请求
        String strResponse = null;
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.alipayurl,AlipayConfig.appid,AlipayConfig.private_key,AlipayConfig.format,AlipayConfig.input_charset,AlipayConfig.ali_public_key,AlipayConfig.content_type);
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        AlipayRefundInfo alidata = new AlipayRefundInfo();
        alidata.setOut_trade_no(out_trade_no); //商户订单号
        alidata.setOut_request_no(out_request_no); //商户订单号
        alidata.setTrade_no(trade_no); //支付宝交易号
        request.setBizContent(JsonUtils.convertToString(alidata));
        AlipayTradeFastpayRefundQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
            if(response.isSuccess()){
                strResponse = "SUCCESS";
            } else {
                strResponse = "FAILD";
            }
        } catch (AlipayApiException e) {
            Logger.getLogger(AlipayRefundUtil.class).error("alipayRefundRequest", e);
        }
        return strResponse;
    }
}
