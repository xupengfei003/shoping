package so.sao.shop.supplier.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by acer on 2017/7/25.
 */
@Configuration
@ConfigurationProperties(prefix = "azureblob")
public class StorageConfig {
    private String defaultEndpointsProtocol;
    private String accountName;
    private String accountKey;
    private String endpointSuffix;
    public String getDefaultEndpointsProtocol() {
        return defaultEndpointsProtocol;
    }
    public void setDefaultEndpointsProtocol(String defaultEndpointsProtocol) {
        this.defaultEndpointsProtocol = defaultEndpointsProtocol;
    }

    public String getAccountName() {
        return accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public String getAccountKey() {
        return accountKey;
    }
    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getEndpointSuffix() {
        return endpointSuffix;
    }

    public void setEndpointSuffix(String endpointSuffix) {
        this.endpointSuffix = endpointSuffix;
    }
}
