package so.sao.shop.supplier.config.azure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AzureBlobProperties.class)
@ConditionalOnProperty(prefix = "shop.azure.blob", name = { "account-name", "account-key", "default-endpoints-protocol", "endpoint-suffix"})
public class AzureBlobConfiguration {

    @Autowired
    private AzureBlobProperties properties;

    @Bean
    public AzureBlobService blobPushService() {
        return new AzureBlobService(properties);
    }
}
