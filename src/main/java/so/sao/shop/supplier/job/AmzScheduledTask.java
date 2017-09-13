package so.sao.shop.supplier.job;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import so.sao.shop.supplier.service.AmzScheduledJobService;
import so.sao.shop.supplier.util.DateUtil;

/**
 * Created by acer on 2017/9/11.
 * 亚马劲数据对接定时任务
 * 每天凌晨1点获取亚马劲商品数据
 */
@Component
public class AmzScheduledTask {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AmzScheduledJobService amzScheduledJobService;

    //每天凌晨1点执行 (0 0 1 * * ?)
    @Scheduled(cron = "${amz.jobs.cron.day}")
    public void excuteStoreInfoDayJob() {

        logger.info("亚马劲数据同步【开始】，当前时间：" + DateUtil.getStringDate());//获取当前时间

        amzScheduledJobService.amzScheduledJob();

        logger.info("亚马劲数据同步【结束】，当前时间：" + DateUtil.getStringDate());//获取当前时间
    }

}



