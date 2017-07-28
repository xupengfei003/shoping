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
     * 短信模板
     */
    private String smsTemplateCode;

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

    public String getSmsTemplateCode() {
        return smsTemplateCode;
    }

    public void setSmsTemplateCode(String smsTemplateCode) {
        this.smsTemplateCode = smsTemplateCode;
    }
}
