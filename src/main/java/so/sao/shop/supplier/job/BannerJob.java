package so.sao.shop.supplier.job;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.external.BannerService;

/**
 * <p>Title: BannerJob</p>
 * <p>Description: 轮播图定时上下架任务</p>
 * <p>Company:透云-中软-西安项目组 </p>
 * @author tengfei.zhang
 * @date 2017年9月19日
 */
@Component
public class BannerJob {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
     * redisTemplate
     */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private BannerService bannerService;
	/**
	 * 每天凌晨两点执行轮播图上架
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void onShelvesBanner() {
		 Boolean lock = redisTemplate.opsForValue().setIfAbsent(Constant.REDIS_KEY_PREFIX + "ON_SHELVES_BANNER", "1");
	        try {
	            if (null != lock && lock) {
	            	Result result = bannerService.updateBanners(new Date(), "1");
	            	logger.info(result.getMessage());
	            }
	        } catch (Exception e) {
	            logger.error("系统异常", e);
	        } finally {
				//判断其余服务是否获得key
				if (null != lock && lock) {
					redisTemplate.delete(Constant.REDIS_KEY_PREFIX + "ON_SHELVES_BANNER");
				}
	        }
	}
	/**
	 * 每天凌晨两点执行轮播图下架
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void offShelfBanner() {
		Boolean lock = redisTemplate.opsForValue().setIfAbsent(Constant.REDIS_KEY_PREFIX + "OFF_SHELF_BANNER", "1");
		try {
			if (null != lock && lock) {
				Result result = bannerService.updateBanners(new Date(), "2");
				logger.info(result.getMessage());
			}
		} catch (Exception e) {
			logger.error("系统异常", e);
		} finally {
			//判断其余服务是否获得key
			if (null != lock && lock) {
				redisTemplate.delete(Constant.REDIS_KEY_PREFIX + "OFF_SHELF_BANNER");
			}
		}
	}
}
