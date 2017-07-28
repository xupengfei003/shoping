package so.sao.shop.supplier.config.sms;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class SmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsService.class);

    private AliyunSmsProperties properties;

    /**
     * 短信发送调用接口
     * @param phoneNumbers 要发送的手机号码
     * @param code    验证码
     * @return 返回请求结果
     */
    public TopicMessage sendSms(List<String> phoneNumbers, String code) {
        /**
         * Step 1. 获取主题引用
         */
        CloudAccount account = new CloudAccount(properties.getAccessKeyId(), properties.getAccessKeySecret(), properties.getMnsEndpoint());
        MNSClient client = account.getMNSClient();
        CloudTopic topic = client.getTopicRef(properties.getTopic());
        /**
         * Step 2. 设置SMS消息体（必须）
         *
         * 注：目前暂时不支持消息内容为空，需要指定消息内容，不为空即可。
         */
        RawTopicMessage msg = new RawTopicMessage();
        msg.setMessageBody("sms-message");
        /**
         * Step 3. 生成SMS消息属性
         */
        MessageAttributes messageAttributes = new MessageAttributes();
        BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
        // 3.1 设置发送短信的签名（SMSSignName）
        batchSmsAttributes.setFreeSignName(properties.getSignName());
        // 3.2 设置发送短信使用的模板（SMSTempateCode）
        batchSmsAttributes.setTemplateCode(properties.getSmsTemplateCode());
        // 3.3 设置发送短信所使用的模板中参数对应的值（在短信模板中定义的，没有可以不用设置）
        BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
        smsReceiverParams.setParam("code", code);
        smsReceiverParams.setParam("product", "透云平台");
        // 3.4 增加接收短信的号码
        for (String phone : phoneNumbers) {
            batchSmsAttributes.addSmsReceiver(phone, smsReceiverParams);
        }
        messageAttributes.setBatchSmsAttributes(batchSmsAttributes);
        try {
            /**
             * Step 4. 发布SMS消息
             */
            TopicMessage topicMessage = topic.publishMessage(msg, messageAttributes);
            LOGGER.info("MessageId: {}", topicMessage.getMessageId());
            LOGGER.info("MessageMD5: {}", topicMessage.getMessageBodyMD5());
            return topicMessage;
        } catch (ServiceException se) {
            LOGGER.warn("短信发送失败{}--{}--{}", se.getErrorCode(), se.getRequestId(), se.getMessage());
        } catch (Exception e) {
            LOGGER.warn("短信发送失败", e);
        } finally {
            client.close();
        }
        return null;
    }

    /**
     * 短信发送记录查询接口
     * @return 返回查询结果
     */
//    public QuerySendDetailsResponse querySendDetails(String bizId, String phone) {
//        //可自助调整超时时间
//        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
//        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
//
//        //初始化acsClient,暂不支持region化
//        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", properties.getAccessKeyId(), properties.getAccessKeySecret());
//        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
//        IAcsClient acsClient = new DefaultAcsClient(profile);
//
//        //组装请求对象
//        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
//        //必填-号码
//        request.setPhoneNumber(phone);
//        //可选-流水号
//        request.setBizId(bizId);
//        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
//        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
//        request.setSendDate(ft.format(new Date()));
//        //必填-页大小
//        request.setPageSize(10L);
//        //必填-当前页码从1开始计数
//        request.setCurrentPage(1L);
//
//        //hint 此处可能会抛出异常，注意catch
//        return acsClient.getAcsResponse(request);
//    }

    public String getVerCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    public void setProperties(AliyunSmsProperties properties) {
        this.properties = properties;
    }
}
