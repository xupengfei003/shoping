package so.sao.shop.supplier.service.impl;

import com.aliyun.mns.model.TopicMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.config.sms.SmsService;
import so.sao.shop.supplier.dao.AccountDao;
import so.sao.shop.supplier.dao.NotificationDao;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.domain.Notification;
import so.sao.shop.supplier.pojo.input.AccountUpdateInput;
import so.sao.shop.supplier.service.AccountService;
import so.sao.shop.supplier.util.NumberGenerate;

import java.util.*;

/**
 * Created by taowang on 2017/9/7.
 */
@Service
public class ContractScheduledService {
    public static final String INFORM = "您的合同到期时间仅剩30天，合同到期后无法接收新订单";
    public static final String ENDINFORM = "您的账号已被禁用，暂时无法接收新订单，如有疑问请联系管理员";

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AccountService accountService;
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
    @Autowired
    private NotificationDao notificationDao;

    public void contractScheduled(){
        //合同剩余30天到期的供应商
        List<Account> listInform = accountDao.findMonthAgo();
        //合同已到期的供应商
        List<Account> list = accountDao.findContractEndDate();
        int len = listInform.size();
        int len1 = list.size();
        if(listInform != null && len > 0) {
            List<Notification> notificationList = new ArrayList<>();
            String sigin = NumberGenerate.generateId(); //系统消息批次处理标记
            for (int i = 0; i < len; i++) {
                Account account = listInform.get(i);
                TopicMessage topicMessage = smsService.sendSms(Collections.singletonList(account.getContractResponsiblePhone()), Arrays.asList("phone","password"), Arrays.asList(account.getContractResponsiblePhone(),""), smsTemplateCode4);
                if (topicMessage == null) {
                    logger.error(account.getContractResponsiblePhone() + "发送短信异常");
                }
                //TODO 给合同到期得供应商发消息
                Notification notification = new Notification();
                notification.setAccountId(account.getAccountId());
                notification.setNotifiType(2);  //消息类型 0订单1系统
                notification.setNotifiDetail(INFORM);   //消息内容
                notification.setCreatedAt(new Date());
                notification.setNotifiStatus(0);    //已读未读 0未读1已读
                notification.setSigin(sigin);
                notificationList.add(notification);
            }
            notificationDao.saveNotifications(notificationList);
        }
        if (list != null && len1 > 0) {
            List<Notification> notificationList = new ArrayList<>();
            String sigin = NumberGenerate.generateId(); //系统消息批次处理标记
            AccountUpdateInput accountUpdateInput=new AccountUpdateInput();
            for (int i = 0; i < len1; i++) {
                Account accountEnd = list.get(i);
                accountUpdateInput.setAccountId(accountEnd.getAccountId());
                accountUpdateInput.setAccountStatus(2);
                //供应商合同过期自动禁用该供应商
                accountService.updateAccountStatus(accountUpdateInput);
                TopicMessage topicMessage1 = smsService.sendSms(Collections.singletonList(accountEnd.getContractResponsiblePhone()), Arrays.asList("phone","password"), Arrays.asList(accountEnd.getContractResponsiblePhone(),""), smsTemplateCode5);
                if (topicMessage1 == null) {
                    logger.error(accountEnd.getContractResponsiblePhone() + "发送短信异常");
                }
                //TODO 给合同到期得供应商发消息
                Notification notification = new Notification();
                notification.setAccountId(accountEnd.getAccountId());
                notification.setNotifiType(2);  //消息类型 0订单1系统
                notification.setNotifiDetail(ENDINFORM);   //消息内容
                notification.setCreatedAt(new Date());
                notification.setNotifiStatus(0);    //已读未读 0未读1已读
                notification.setSigin(sigin);
                notificationList.add(notification);
            }
            notificationDao.saveNotifications(notificationList);
        }
    }
}
