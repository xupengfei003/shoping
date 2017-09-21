package so.sao.shop.supplier.web;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.alipay.AlipayConfig;
import so.sao.shop.supplier.alipay.AlipayRefundInfo;
import so.sao.shop.supplier.alipay.JsonUtils;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.PurchaseService;
import so.sao.shop.supplier.util.Ognl;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author gxy on 2017/9/15.
 */
@RestController
@RequestMapping("/alipay")
public class AlipayRefundController {
    @Resource
    private PurchaseService purchaseService;
    /**
     * ＜支付宝退款请求＞
     * @param out_trade_no 订单支付时传入的商户订单号,不能和 trade_no同时为空。
     * @param trade_no  支付宝交易号，和商户订单号不能同时为空
     * @param refund_amount 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
     * @return String
     */
    @GetMapping("/alipayrefund")
    public String alipayRefundRequest(String out_trade_no,String trade_no,Double refund_amount) {
        // 发送请求
        String strResponse = null;
        try {
            AlipayClient alipayClient = new DefaultAlipayClient
                    (AlipayConfig.alipayurl, AlipayConfig.appid,AlipayConfig.private_key, AlipayConfig.content_type,
                            AlipayConfig.input_charset, AlipayConfig.ali_public_key);
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            AlipayRefundInfo alidata = new AlipayRefundInfo();
            alidata.setOut_trade_no(out_trade_no);//商户订单号
            alidata.setRefund_amount(refund_amount);//需要退款的金额
            alidata.setTrade_no(trade_no);//支付宝交易号
            alidata.setRefund_reason("测试退款");//退款原因
            alidata.setOut_request_no("1234566");//标识一次退款请求
            alidata.setOperator_id("OP001");//商户的操作员编号
            alidata.setStore_id("NJ_S_001");//商户的门店编号
            alidata.setTerminal_id("NJ_T_001");//商户的终端编号
            request.setBizContent(JsonUtils.convertToString(alidata));
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            strResponse = response.getCode();
            if (response.isSuccess()) {
                strResponse = "refund success";
            } else {
                strResponse = "refund faild";
            }
        } catch (Exception e) {
            Logger.getLogger(getClass()).error("alipayRefundRequest", e);
        }
        return strResponse;
    }

    /**
     * 退款接口
     * <p>
     * 根据订单编号调用退款接口退款并修改订单状态。
     * 1.验证参数合法性；
     * 2.调用退款方法。
     *
     * @param orderId 订单编号
     * @return 返回一个Result对象
     * @throws Exception 异常
     */
    @ApiOperation(value = "退款接口", notes = "根据订单编号调用退款接口退款并修改订单状态【负责人：杨恒乐】")
    @PostMapping("/refund/{orderId}")
    public Result refund(@PathVariable("orderId") String orderId) throws Exception {
        // 1.验证参数合法性
        if (Ognl.isEmpty(orderId)) {
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY); // 不允许为空
        }

        // 判断订单状态
        if (!verifyOrderStatus(orderId, Constant.OrderStatusConfig.REFUNDED)) {
            return Result.fail(Constant.MessageConfig.ORDER_STATUS_EERO);
        }

        // 2.调用退款方法
        Map map = purchaseService.refundByOrderId(orderId);
        boolean flag = (boolean) map.get("flag");
        if (flag) { // 退款成功
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }

        return Result.fail((String) map.get("message")); // 退款失败，返回失败原因
    }

    //验证订单状态
    private boolean verifyOrderStatus(String orderId, Integer orderStatus) {
        Integer getOrderStatus = purchaseService.findOrderStatus(orderId);
        if(null == getOrderStatus){
            return false;
        }
        String status = Constant.OrderStatusRule.RULES[getOrderStatus - 1];
        return status.contains(String.valueOf(orderStatus));
    }
}
