package so.sao.shop.supplier.config.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "shop.aliyun.sms")
public class AliyunSmsProperties {

    private String accessKeyId;

    private String accessKeySecret;

    private String mnsEndpoint;

    private String topic;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 短信模板(验证码)
     */
    private String smsTemplateCode1;

    /**
     * 短信模板(第一次发送密码)
     */
    private String smsTemplateCode2;

    /**
     * 短信模板(找回密码)
     */
    private String smsTemplateCode3;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getMnsEndpoint() {
        return mnsEndpoint;
    }

    public void setMnsEndpoint(String mnsEndpoint) {
        this.mnsEndpoint = mnsEndpoint;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getSmsTemplateCode1() {
        return smsTemplateCode1;
    }

    public void setSmsTemplateCode1(String smsTemplateCode1) {
        this.smsTemplateCode1 = smsTemplateCode1;
    }

    public String getSmsTemplateCode2() {
        return smsTemplateCode2;
    }

    public void setSmsTemplateCode2(String smsTemplateCode2) {
        this.smsTemplateCode2 = smsTemplateCode2;
    }

    public String getSmsTemplateCode3() {
        return smsTemplateCode3;
    }

    public void setSmsTemplateCode3(String smsTemplateCode3) {
        this.smsTemplateCode3 = smsTemplateCode3;
    }
}
