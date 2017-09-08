package so.sao.shop.supplier.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import so.sao.shop.supplier.service.impl.ContractScheduledService;

/**
 * Created by taowang on 2017/9/7.
 * 设置定时器。每天执行检查合同过期和即将过期的用户。
 */
@Component
public class ContractScheduledJob {

    @Autowired
    private ContractScheduledService Scheduled;

    //设定每天上午9点定时检查合同过期供应商
    @Scheduled(cron = "0 0 9 * * ?")
    public void ContractEnd(){
        Scheduled.contractScheduled();
    }
}
