package so.sao.shop.supplier.job;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.AmzScheduledJobService;
import so.sao.shop.supplier.service.app.AppAccountCouponService;
import so.sao.shop.supplier.service.external.CouponService;
import so.sao.shop.supplier.util.DateUtil;

import java.util.Date;

/**
 * Created by acer on 2017/9/11.
 * 亚马劲数据对接定时任务
 * 每天凌晨1点获取亚马劲商品数据
 */
@Component
public class CouponJob {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private AppAccountCouponService appAccountCouponService;

    @Autowired
    private CouponService couponService;

    /**
     * 每天零点更新优惠券表状态
     */
    @Scheduled(cron = "0 * * * * *")
    public void excuteCouponStatusUpdate() {
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(Constant.REDIS_KEY_PREFIX + "", "1");

        try{
            if (null != lock && lock) {
                logger.info("优惠券更新完开始");
                appAccountCouponService.dailyUpdateStatus();
                couponService.dailyUpdateStatus();
                logger.info("优惠券更新完成");
            }
        }catch (Exception e){
            logger.error("系统异常",e);
        }finally {
            if (null != lock && lock) {

                redisTemplate.delete(Constant.REDIS_KEY_PREFIX + "");
            }
        }

    }

}



