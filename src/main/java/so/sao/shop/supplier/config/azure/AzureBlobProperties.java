package so.sao.shop.supplier.config.azure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "shop.azure.blob")
public class AzureBlobProperties {

    private String accountName;

    private String accountKey;

    private String defaultEndpointsProtocol;

    private String endpointSuffix;

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

    public String getDefaultEndpointsProtocol() {
        return defaultEndpointsProtocol;
    }

    public void setDefaultEndpointsProtocol(String defaultEndpointsProtocol) {
        this.defaultEndpointsProtocol = defaultEndpointsProtocol;
    }

    public String getEndpointSuffix() {
        return endpointSuffix;
    }

    public void setEndpointSuffix(String endpointSuffix) {
        this.endpointSuffix = endpointSuffix;
    }
}
