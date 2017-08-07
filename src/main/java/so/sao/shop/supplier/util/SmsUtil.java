package so.sao.shop.supplier.util;
//
//import com.aliyuncs.DefaultAcsClient;
//import com.aliyuncs.IAcsClient;
//import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
//import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
//import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
//import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
//import com.aliyuncs.exceptions.ClientException;
//import com.aliyuncs.profile.DefaultProfile;
//import com.aliyuncs.profile.IClientProfile;
//import org.codehaus.jackson.map.ObjectMapper;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by acer on 2017/7/18.
// */
//public class SmsUtil {
//    //产品名称:云通信短信API产品,开发者无需替换
//    private static final String product = "Dysmsapi";
//    //产品域名,开发者无需替换
//    public static final String domain = "dysmsapi.aliyuncs.com";
//
//    // 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
//    private static final String accessKeyId = "LTAIJ5WCST5NTxKK";
//    private static final String accessKeySecret = "w5ZgY1edGDrCB3NlDt5IseCsnlJZLV";
//    private static final String signName = "透云";
//    private static final String templateCode = "SMS_78585197";
//
//    public static String getVerCode(){
//        String vcode = "";
//        return vcode + (int)((Math.random()*9+1)*100000);
//    }
//
//    /**
//     * 短信发送调用接口
//     * @param phoneNo 要发送的手机号码
//     * @param code 验证码
//     * @return 返回请求结果
//     * @throws ClientException 短信平台异常信息
//     */
//    public static SendSmsResponse sendSms(String phoneNo, String code) throws ClientException, IOException {
//        //可自助调整超时时间
//        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
//        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
//
//        //初始化acsClient,暂不支持region化
//        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", SmsUtil.accessKeyId, SmsUtil.accessKeySecret);
//        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", SmsUtil.product, SmsUtil.domain);
//        IAcsClient acsClient = new DefaultAcsClient(profile);
//
//        //组装请求对象-具体描述见控制台-文档部分内容
//        SendSmsRequest request = new SendSmsRequest();
//        //必填:待发送手机号
//        request.setPhoneNumbers(phoneNo);
//        //必填:短信签名-可在短信控制台中找到
//        request.setSignName(SmsUtil.signName);
//        //必填:短信模板-可在短信控制台中找到
//        request.setTemplateCode(SmsUtil.templateCode);
//        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
//        Map<String, String> jsonCode = new HashMap<String, String>();
//        jsonCode.put("code",code);// 改成线上模板
//        String jsonObject = new ObjectMapper().writeValueAsString(jsonCode);
//        request.setTemplateParam(jsonObject);
//        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//        request.setOutId("yourOutId");
//
//        //hint 此处可能会抛出异常，注意catch
//        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
//
//        return sendSmsResponse;
//    }
//    /**
//     * 短信发送记录查询接口
//     * @param bizId 发送短信成功返回的bizId
//     * @param phoneNo 要查询的手机号码
//     * @return 返回查询结果
//     * @throws ClientException 短信平台异常信息
//     */
//    public static QuerySendDetailsResponse querySendDetails(String bizId, String phoneNo) throws ClientException {
//        //可自助调整超时时间
//        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
//        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
//
//        //初始化acsClient,暂不支持region化
//        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", SmsUtil.accessKeyId, SmsUtil.accessKeySecret);
//        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", SmsUtil.product, SmsUtil.domain);
//        IAcsClient acsClient = new DefaultAcsClient(profile);
//
//        //组装请求对象
//        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
//        //必填-号码
//        request.setPhoneNumber(phoneNo);
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
//        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);
//
//        return querySendDetailsResponse;
//    }
//}
