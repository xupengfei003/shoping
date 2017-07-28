package so.sao.shop.supplier.config.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AliyunSmsProperties.class)
@ConditionalOnProperty(prefix = "shop.aliyun.sms", name = { "access-key-id", "access-key-secret"})
public class SmsConfiguration {

    @Autowired
    private AliyunSmsProperties properties;

    @Bean
    public SmsService smsService() {
        SmsService smsService = new SmsService();
        smsService.setProperties(properties);
        return smsService;
    }
}
