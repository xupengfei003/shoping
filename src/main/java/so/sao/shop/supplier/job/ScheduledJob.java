package so.sao.shop.supplier.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.pojo.input.CancelReasonInput;
import so.sao.shop.supplier.service.AccountService;
import so.sao.shop.supplier.service.OrderMoneyRecordService;
import so.sao.shop.supplier.service.PurchaseService;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by niewenchao on 2017/8/11.
 * 定时任务
 */
@Component
public class ScheduledJob {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AccountService accountService;
    @Autowired
    private OrderMoneyRecordService orderMoneyRecordService;
    @Resource
    private PurchaseService purchaseService;
    /**
     * redisTemplate
     */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 定时执行结算明细新增数据
     * @throws Exception
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void OrderMoneyRecordJob () throws Exception {
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(Constant.REDIS_KEY_PREFIX + "COUNT_BILLING_DETAILS", "1");
        try {
            if (null != lock && lock) {
                //获取当天是本月的第几天
                Calendar calendar = Calendar.getInstance();
                int days = calendar.get(Calendar.DAY_OF_MONTH);

                //根据当天时间是本月的第几天和当前时间，查询当天要结算的商家信息列表
                List<Account> accountList = accountService.findAccountList(days, new Date());
                if (null != accountList && !accountList.isEmpty()) {
                    orderMoneyRecordService.saveOrderMoneyRecord(accountList);
                }
            }
        } catch (Exception e) {
            logger.error("系统异常", e);
        } finally {
            redisTemplate.delete(Constant.REDIS_KEY_PREFIX + "COUNT_BILLING_DETAILS");
        }
    }

    /**
     * 定时执行取消到期待付款的订单
     * @throws Exception
     */
    //定位每1小时检索一次待付款的订单
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void CancelOrderJob () throws Exception {
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(Constant.REDIS_KEY_PREFIX + "CANCEL_PAYMENT_ORDER", "1");
        try {
            if (null != lock && lock) {
                //根据订单状态查询所有为待付款的订单ID
                List<String> orderIdList = purchaseService.findOrderIdByOrderStatus(Constant.OrderStatusConfig.PAYMENT);
                if (null != orderIdList && !orderIdList.isEmpty()) {
                    CancelReasonInput cancelReasonInput = new CancelReasonInput();
                    for(String orderId : orderIdList){
                        cancelReasonInput.setOrderId(orderId);
                        purchaseService.cancelOrder(cancelReasonInput);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("系统异常", e);
        } finally {
            redisTemplate.delete(Constant.REDIS_KEY_PREFIX + "CANCEL_PAYMENT_ORDER");
        }
    }
}
