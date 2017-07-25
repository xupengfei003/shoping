package so.sao.shop.supplier.util;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import so.sao.shop.supplier.config.StorageConfig;

/**
 * Created by acer on 2017/7/25.
 * BlobHelper.java 用来获取或创建Blob所在容器
 */
public class BlobHelper {
    public static CloudBlobContainer getBlobContainer(String containerName, StorageConfig storageConfig)
    {
        try
        {
            String blobStorageConnectionString = String.format("DefaultEndpointsProtocol=%s;"
                            + "AccountName=%s;"
                            + "AccountKey=%s"
                            + "EndpointSuffix=%s",
                    storageConfig.getDefaultEndpointsProtocol(),
                    storageConfig.getAccountName(), storageConfig.getAccountKey(),storageConfig.getEndpointSuffix());

            CloudStorageAccount account = CloudStorageAccount.parse(blobStorageConnectionString);
            CloudBlobClient serviceClient = account.createCloudBlobClient();

            CloudBlobContainer container = serviceClient.getContainerReference(containerName);

            // Create a permissions object.
            BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

            // Include public access in the permissions object.
            containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

            // Set the permissions on the container.
            container.uploadPermissions(containerPermissions);
            container.createIfNotExists();

            return container;
        }
        catch(Exception e)
        {
            return null;
        }
    }
}
