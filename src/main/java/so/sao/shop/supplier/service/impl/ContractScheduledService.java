package so.sao.shop.supplier.service.impl;

import com.aliyun.mns.model.TopicMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.config.sms.SmsService;
import so.sao.shop.supplier.dao.AccountDao;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.pojo.input.AccountUpdateInput;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by taowang on 2017/9/7.
 */
@Service
public class ContractScheduledService {
    @Autowired
    private AccountDao accountDao;
    /**
     * 初始化日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 合同到期30天前
     */
    @Value("${shop.aliyun.sms.sms-template-code4}")
    String smsTemplateCode4;

    /**
     * 合同到期
     */
    @Value("${shop.aliyun.sms.sms-template-code5}")
    String smsTemplateCode5;

    @Autowired
    private SmsService smsService;

    public void contractScheduled(){
         //合同剩余30天到期的供应商
        List<Account> listInform = accountDao.findMonthAgo();
        //合同已到期的供应商
        List<Account> list = accountDao.findContractEndDate();
        if(listInform!=null) {
            int len = listInform.size();
            for (int i = 0; i < len; i++) {
                Account account = listInform.get(i);
                TopicMessage topicMessage = smsService.sendSms(Collections.singletonList(account.getContractResponsiblePhone()), Arrays.asList("phone","password"), Arrays.asList(account.getContractResponsiblePhone(),""), smsTemplateCode4);
                if (topicMessage == null) {
                    logger.error(account.getContractResponsiblePhone() + "发送短信异常");
                }
            }
        }
        if (list!=null) {
            int len1 = list.size();
            AccountUpdateInput accountUpdateInput=new AccountUpdateInput();
            for (int i = 0; i < len1; i++) {
                Account accountEnd = list.get(i);
                accountUpdateInput.setAccountId(accountEnd.getAccountId());
                accountUpdateInput.setAccountStatus(2);
                accountUpdateInput.setUpdateDate(new Date());
                //供应商合同过期自动禁用该供应商
                accountDao.updateAccountStatusById(accountUpdateInput);
                TopicMessage topicMessage1 = smsService.sendSms(Collections.singletonList(accountEnd.getContractResponsiblePhone()), Arrays.asList("phone","password"), Arrays.asList(accountEnd.getContractResponsiblePhone(),""), smsTemplateCode5);
                if (topicMessage1 == null) {
                    logger.error(accountEnd.getContractResponsiblePhone() + "发送短信异常");
                }
            }
        }
    }
}
