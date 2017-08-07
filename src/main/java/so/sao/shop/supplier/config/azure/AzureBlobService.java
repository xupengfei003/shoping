package so.sao.shop.supplier.config.azure;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.util.UUIDGenerator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyanyong on 2017/4/7.
 * azure blob存储相关
 */
public class AzureBlobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureBlobService.class);

    private static final String STORAGE_CONNECTION_STRING = "DefaultEndpointsProtocol=%s;AccountName=%s;AccountKey=%s;EndpointSuffix=%s";

    /**
     * storage account
     */
    private static CloudStorageAccount account = null;

    private AzureBlobProperties azureBlobProperties;

    /**
     * 构造函数
     * 初始化storage account
     * @param azureBlobProperties blob配置参数
     */
    public AzureBlobService(AzureBlobProperties azureBlobProperties) {
        this.azureBlobProperties = azureBlobProperties;
        try {
            // Retrieve storage account from connection-string.
            // 初始化storage account
            account = CloudStorageAccount.parse(storageConnection());
        } catch (URISyntaxException | InvalidKeyException e) {
            LOGGER.info("初始化 CloudStorageAccount 异常： \n"+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 单文件上传
     * @param multipartFile  文件内容
     * @param containerName  容器名称
     * @return BlobUpload
     */
    public BlobUpload uploadFile(MultipartFile multipartFile, String containerName) {
        try {
            if (!multipartFile.isEmpty()) {
                // Retrieve reference to a previously created container.
                CloudBlobContainer container = getBlobContainer(containerName);

                String fileExtension = getFileExtension(multipartFile.getOriginalFilename()).toLowerCase();
                // 使用UUID方式命名
                LocalDate now = LocalDate.now();
                String blobName = now.getYear()+"/"+ now.getMonthValue()+"/"+ now.getDayOfMonth()+"/"
                        + UUIDGenerator.getUUID()+ fileExtension;

                // 设置文件类型，并且上传到azure blob
                CloudBlockBlob blob = container.getBlockBlobReference(blobName);
                blob.getProperties().setContentType(multipartFile.getContentType());
                blob.upload(multipartFile.getInputStream(), multipartFile.getSize());

                return new BlobUpload(blobName, blob.getUri().getPath());
            }
        } catch (URISyntaxException | StorageException | IOException e) {
            LOGGER.info("上传到Blob异常： \n"+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 多文件上传
     * @param multipartFile  文件内容
     * @param containerName  容器名称
     * @return List<BlobUpload>
     */
    public List<BlobUpload> uploadFiles(MultipartFile[] multipartFile, String containerName) {
        List<BlobUpload> buList = new ArrayList<BlobUpload>();
        try {
            // Retrieve reference to a previously created container.
            CloudBlobContainer container = getBlobContainer(containerName);
            // 循环文件
            for (MultipartFile tempMultipartFile : multipartFile) {
                if (!tempMultipartFile.isEmpty()) {
                    String fileExtension = getFileExtension(tempMultipartFile.getOriginalFilename()).toLowerCase();
                    // 使用UUID方式命名
                    LocalDate now = LocalDate.now();
                    String blobName = now.getYear() + "/" + now.getMonthValue() + "/" + now.getDayOfMonth()
                            + "/" + UUIDGenerator.getUUID() + fileExtension;

                    // 设置文件类型，并且上传到azure blob
                    CloudBlockBlob blob = container.getBlockBlobReference(blobName);
                    blob.getProperties().setContentType(tempMultipartFile.getContentType());
                    blob.upload(tempMultipartFile.getInputStream(), tempMultipartFile.getSize());

                    buList.add(new BlobUpload(blobName, blob.getUri().toString()));
                }
            }
            return buList;
        } catch (URISyntaxException | StorageException | IOException e) {
            LOGGER.info("上传到Blob异常： \n"+e.getMessage());
        }
        return null;
    }

    /**
     * 删除blob
     * @param containerName  容器名称
     * @param blobName        文件名称
     */
    public void deleteFile(String containerName,String blobName) {
        try {
            // Retrieve reference to a previously created container.
            CloudBlobContainer container = getBlobContainer(containerName);
            // Retrieve reference to a blob named blobName.
            CloudBlockBlob blob = container.getBlockBlobReference(blobName);
            // 删除
            blob.deleteIfExists();
        } catch (Exception e) {
            LOGGER.info("删除Blob异常： \n"+e.getMessage());
        }
    }

    /**
     * 删除blob容器
     * @param containerName  容器名称
     */
    public void deleteContainerFile(String containerName) {
        try {
            // Retrieve reference to a previously created container.
            CloudBlobContainer container = getBlobContainer(containerName);
            // 删除
            container.deleteIfExists();
        } catch (Exception e) {
            LOGGER.info("删除Blob容器异常："+e.getMessage());
        }
    }

    /**
     * 获取容器
     * @param containerName 容器名称
     * @return 容器
     */
    private CloudBlobContainer getBlobContainer(String containerName) {
        try{
            // Create the blob client.
            CloudBlobClient serviceClient = account.createCloudBlobClient();
            // Get a reference to a container.
            // The container name must be lower case
            return serviceClient.getContainerReference(containerName);
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * 获取或者创建容器
     * @param containerName 容器名称
     * @return 容器
     */
    private CloudBlobContainer getOrCreateBlobContainer(String containerName) {
        try{
            // Create the blob client.
            CloudBlobClient serviceClient = account.createCloudBlobClient();
            // Get a reference to a container.
            // The container name must be lower case
            CloudBlobContainer container = serviceClient.getContainerReference(containerName);

            // Create a permissions object.
            BlobContainerPermissions containerPermissions = new BlobContainerPermissions();
            // Include public access in the permissions object.
            containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
            // Set the permissions on the container.
            container.uploadPermissions(containerPermissions);
            // Create the container if it does not exist.
            container.createIfNotExists();
            return container;
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * 存储连接字符串
     * @return 连接azure blob的字符串
     */
    private String storageConnection() {
        return String.format(STORAGE_CONNECTION_STRING, azureBlobProperties.getDefaultEndpointsProtocol(),
                azureBlobProperties.getAccountName(), azureBlobProperties.getAccountKey(), azureBlobProperties.getEndpointSuffix());
    }

    /**
     * 得到文件扩展名
     * @param fileName 文件名
     * @return
     */
    private static String getFileExtension(String fileName) {
        int position = fileName.indexOf('.');
        if (position > 0) {
            return fileName.substring(position);
        }
        return "";
    }

    /**
     * 文件路径名称
     * @param fileType   文件类型
     * @param thumbnail
     * @return
     */
    private static String getBlobPreName(int fileType, Boolean thumbnail) {
        String afterName = "";
        if (thumbnail) {
            afterName = "thumbnail/";
        }
        switch (fileType) {
            case 1:
                return "logo/" + afterName;
            case 2:
                return "food/" + afterName;
            case 3:
                return "head/" + afterName;
            case 4:
                return "ads/" + afterName;
            default :
                return "";
        }
    }

}
