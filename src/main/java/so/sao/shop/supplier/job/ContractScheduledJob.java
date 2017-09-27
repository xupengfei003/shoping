package so.sao.shop.supplier.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.service.impl.ContractScheduledService;

/**
 * Created by taowang on 2017/9/7.
 * 设置定时器：每天定时给合同过期和即将过期的用户发送短信通知和系统消息。
 */
@Component
public class ContractScheduledJob {
    /**
     * 初始化日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ContractScheduledService Scheduled;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //设定每天中午12点定时检查合同过期供应商
    @Scheduled(cron = "0 0 12 * * ?")
    public void ContractEnd() throws Exception {
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(Constant.REDIS_SMSCODE_KEY_PREFIX + "CONTRACT_END_INFORM", "1");
        try {
            //添加redis同步锁，如果该key不存在，则执行下面操作
            if (null != lock && lock) {
                Scheduled.contractScheduled();
            }
        } catch (Exception e) {
            logger.error("系统异常", e);
        } finally {
            //判断其余服务是否获得key
            if (null != lock && lock) {
                //执行完毕删除key
                redisTemplate.delete(Constant.REDIS_SMSCODE_KEY_PREFIX + "CONTRACT_END_INFORM");
            }
        }
    }
}
