package so.sao.shop.supplier.job;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.vo.PurchaseInfoVo;
import so.sao.shop.supplier.service.LogisticsService;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by acer on 2017/10/11.
 */
@Component
public class OrderAutomaticReceiveJob {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * redisTemplate
     */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private LogisticsService logisticsService;

    @Scheduled(cron = "0 0 3 * * ?")
    public void artificialAutomaticReceive() {
        /**
         * 1.从received_purchase表中检索出所有据当前时间大于等于7天的订单ID
         * 2.从purchase表中取出订单状态为3的订单（已发货）
         * 3.将received_purchase表中买家已收货的订单过滤，将过滤后的订单集合确认收货
         * 4.将这些订单ID将purchase表中的相应订单状态修改为4（已完成）
         * 5.更改订单状态完成后将received_purchase中这些订单ID相关的数据delete
         */
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(Constant.REDIS_KEY_PREFIX + "ARTIFICIAL_AUTOMATIC_RECEIVE", "1");
        try {
            if (null != lock && lock) {
                List<String> orderIds = logisticsService.findOrderIdByTime();
                //获取已发货的所有订单
                List<PurchaseInfoVo> purchaseInfoVoList = logisticsService.findOrderInfoByOrderStatus(Constant.OrderStatusConfig.CONFIRM_RECEIVED);
                List<String> orderIdsInput = new ArrayList<>();//存储入参订单ID
                //过滤订单
                for (String orderId : orderIds) {
                    for (PurchaseInfoVo purchaseInfoVo: purchaseInfoVoList) {
                        if(orderId.equals(purchaseInfoVo.getOrderId())){
                            orderIdsInput.add(orderId);
                        }
                    }
                }
                Integer count = 0;
                if (null != orderIds && orderIds.size() > 0) {
                    count = logisticsService.receiveOrder(orderIdsInput);//自动确认收货
                }
                if (count != 0) {
                    logisticsService.deleteReceivedOrderByOrderId(orderIds);//删除中间表中符合时间的订单信息
                }
            }
        } catch (Exception e) {
            logger.error("系统异常", e);
        } finally {
            if (null != lock && lock) {
                redisTemplate.delete(Constant.REDIS_KEY_PREFIX + "ARTIFICIAL_AUTOMATIC_RECEIVE");
            }
        }
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void logisticsAutomaticReceive() {
        /**
         * 1. 首先获取所有已发货的订单ID和物流单号；
         * 2. 其次通过物流单号检索物流信息，将物流信息中状态为已签收且签收时间为当前时间差7天的订单ID存入list;
         * 3. 再通过该订单ID的list批量确认收货
         */
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(Constant.REDIS_KEY_PREFIX + "LOGISTICS_AUTOMATIC_RECEIVE", "1");
        try {
            List<PurchaseInfoVo> purchaseInfoVoList = logisticsService.findOrderInfoByOrderStatus(Constant.OrderStatusConfig.ISSUE_SHIP);
            List<String> orderIds = new ArrayList<>();
            for (PurchaseInfoVo purchaseInfoVo : purchaseInfoVoList) {
                Result result = logisticsService.findLogisticInfo(purchaseInfoVo.getOrderShipmentNumber());
                JSONObject jsonObject = JSONObject.fromObject(result.getData());
                if(null != jsonObject && jsonObject.size() > 0){
                    String state = String.valueOf(jsonObject.get("state"));
                    if("3".equals(state)){
                        //计算时间差
                        if(compareDate(jsonObject)){
                            orderIds.add(purchaseInfoVo.getOrderId());
                        }
                    }
                }
            }
            logisticsService.receiveOrder(orderIds);//自动确认收货
        } catch (Exception e){
            logger.error("系统异常", e);
        } finally {
            if (null != lock && lock) {
                redisTemplate.delete(Constant.REDIS_KEY_PREFIX + "LOGISTICS_AUTOMATIC_RECEIVE");
            }
        }
    }

    //计算时间差
    private boolean compareDate(JSONObject jsonObject) throws ParseException {
        Object followInfo = jsonObject.get("data");//获取物流跟踪信息
        JSONArray jsonArray = JSONArray.fromObject(followInfo);
        Object lastFollowInfo = jsonArray.get(0);//获取最近物流信息
        JSONObject jsonObjectReceiveDate = JSONObject.fromObject(lastFollowInfo);//把最近物流信息JSON化
        String followTime = String.valueOf(jsonObjectReceiveDate.get("time"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date receiveT = simpleDateFormat.parse(followTime);
        Long timeDiff = new Date().getTime() - receiveT.getTime();
        Long days = timeDiff/(3600*24*1000);
        if(days >= 7){
            return true;
        }
        return false;
    }
}
