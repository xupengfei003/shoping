package so.sao.shop.supplier.config.azure;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.config.CommConstant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.vo.CommBlobUpload;
import so.sao.shop.supplier.util.UUIDGenerator;
import so.sao.shop.supplier.web.UploadController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.time.LocalDate;
import java.util.*;
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
     * 多文件上传（商品图片）
     * @param multipartFile  文件内容
     * @return List<BlobUpload>
     */
    public Result uploadFilesComm(MultipartFile[] multipartFile, List<CommBlobUpload> blobUploadList) {
        try {
            CloudBlobContainer container = getBlobContainer(CommConstant.AZURE_CONTAINER.toLowerCase());
            Map<String,String> map = new HashMap<>();
            for (MultipartFile file: multipartFile) {
                if (!file.isEmpty()) {
                    //获取上传文件的名称及文件类型
                    CommBlobUpload commBlobUpload = new CommBlobUpload();
                    String fileName = file.getOriginalFilename();
                    String extensionName = StringUtils.substringAfter(fileName, ".");

                    //过滤非jpg,png,jpeg,gif格式的文件
                    if(!(Arrays.binarySearch(CommConstant.IMAGE_TYPE, file.getContentType().toLowerCase()) >= 0)){
                        map.put("fileName",fileName);
                        return Result.fail("上传的文件中包含非jpg/png/jpeg/gif格式", map);
                    }

                    //拼装blob的名称(新的图片文件名 =UUID+"."图片扩展名)
                    String newFileName = UUIDGenerator.getUUID() + "." + extensionName;
                    String preName = UploadController.getBlobPreName(extensionName, false).toLowerCase();//大图路径
                    String blobName = preName + newFileName;

                    //设置文件类型，并且上传到azure blob
                    CloudBlockBlob blob = container.getBlockBlobReference(blobName);
                    blob.getProperties().setContentType(file.getContentType());
                    blob.upload(file.getInputStream(), file.getSize());

                    //生成缩略图，并上传至AzureStorage
                    BufferedImage img = new BufferedImage(CommConstant.THUMBNAIL_DEFAULT_WIDTH, CommConstant.THUMBNAIL_DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
                    img.createGraphics().drawImage(ImageIO.read(file.getInputStream()).getScaledInstance(CommConstant.THUMBNAIL_DEFAULT_WIDTH, CommConstant.THUMBNAIL_DEFAULT_HEIGHT, Image.SCALE_SMOOTH), 0, 0, null);
                    ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream();
                    ImageIO.write(img, CommConstant.IMAGE_JPG, thumbnailStream);
                    String thumbnailPreName = UploadController.getBlobPreName(extensionName, true).toLowerCase();
                    String blobThumbnail = thumbnailPreName + UUIDGenerator.getUUID() + CommConstant.IMG_FILE_JPG;
                    CloudBlockBlob thumbnailBlob = container.getBlockBlobReference(blobThumbnail);
                    thumbnailBlob.getProperties().setContentType(CommConstant.IMAGE_JPEG);
                    InputStream inputStream = new ByteArrayInputStream(thumbnailStream.toByteArray());
                    thumbnailBlob.upload(inputStream, thumbnailStream.toByteArray().length);

                    //将上传后的图片URL返
                    commBlobUpload.setFileName(fileName);
                    commBlobUpload.setUrl(blob.getUri().toString());
                    commBlobUpload.setMinImgUrl(thumbnailBlob.getUri().toString());
                    commBlobUpload.setType(extensionName);
                    BufferedImage sourceImg = ImageIO.read(file.getInputStream());
                    commBlobUpload.setSize(sourceImg.getWidth() + "*" + sourceImg.getHeight());

                    blobUploadList.add(commBlobUpload);
                }else {
                    return Result.fail("上传文件为空");
                }
            }
        } catch (URISyntaxException | StorageException | IOException e) {
            LOGGER.info("文件上传异常", e);
            return Result.fail("文件上传异常");
        }
        return Result.fail("文件上传异常");
    }


    /**
     * 解压后上传文件
     */
    public List<Result> uploadFilesComm(String realzippath ,List<String> files) {
        List<CommBlobUpload> blobUploadEntities = new ArrayList<CommBlobUpload>();
        Map<String,String> map = new HashMap<>();
        List<Result> results = new ArrayList<>();
        try {
            if (files.size() != 0) {
                CloudBlobContainer container = getBlobContainer(CommConstant.AZURE_CONTAINER.toLowerCase());
                File dir = new File(realzippath+"/img");
                if  (!dir .exists()  && !dir .isDirectory()) {
                    dir .mkdir();
                }

                for (String fileStr : files) {
                    //图片尺寸不变，压缩图片文件大小outputQuality实现,参数1为最高质量
                    Thumbnails.of(realzippath+"/"+ fileStr.trim())
                            .scale(1f).outputQuality(0.25f)
                            .toFile(realzippath+"/img/"+ fileStr.trim());
                    File file = new File(realzippath+"/img/"+ fileStr.trim());
                    String fileName = file.getName();
                    if (file.exists()) {
                        try {
                            //获取上传文件的名称及文件类型
                            CommBlobUpload blobUploadEntity = new CommBlobUpload();
                            String extensionName = StringUtils.substringAfter(fileName, ".");

                            //过滤非jpg,png,jpeg,gif格式的文件
                            if (!(fileName.endsWith(CommConstant.IMG_FILE_JPG)
                                    || fileName.endsWith(CommConstant.IMG_FILE_JPEG)
                                    || fileName.endsWith(CommConstant.IMG_FILE_PNG)
                                    || fileName.endsWith(CommConstant.IMG_FILE_GIF))) {
                                map.put("fileName",fileName);
                                results.add(Result.fail("上传的文件中包含非jpg/png/jpeg/gif格式",map));
                                continue;
                            }

                            //拼装blob的名称(新的图片文件名 =UUID+"."图片扩展名)
                            String newFileName = UUIDGenerator.getUUID() + "." + extensionName;
                            String preName = UploadController.getBlobPreName(extensionName, false).toLowerCase();
                            String blobName = preName + newFileName;

                            //设置文件类型，并且上传到azure blob
                            CloudBlockBlob blob = container.getBlockBlobReference(blobName);
                            blob.getProperties().setContentType(fileName.substring(fileName.lastIndexOf("."),fileName.length()));
                            blob.upload(new FileInputStream(file), file.length());

                            //生成缩略图，并上传至AzureStorage
                            BufferedImage img = new BufferedImage(CommConstant.THUMBNAIL_DEFAULT_WIDTH, CommConstant.THUMBNAIL_DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
                            img.createGraphics().drawImage(ImageIO.read(new FileInputStream(file)).getScaledInstance(CommConstant.THUMBNAIL_DEFAULT_WIDTH, CommConstant.THUMBNAIL_DEFAULT_HEIGHT, Image.SCALE_SMOOTH), 0, 0, null);
                            ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream();
                            ImageIO.write(img, CommConstant.IMAGE_JPG, thumbnailStream);
                            InputStream inputStream = new ByteArrayInputStream(thumbnailStream.toByteArray());

                            String thumbnailPreName = UploadController.getBlobPreName(extensionName, true).toLowerCase();
                            String newThumbnailName = UUID.randomUUID().toString();
                            String blobThumbnail = thumbnailPreName + newThumbnailName + CommConstant.IMG_FILE_JPG;;
                            CloudBlockBlob thumbnailBlob = container.getBlockBlobReference(blobThumbnail);
                            thumbnailBlob.getProperties().setContentType(CommConstant.IMAGE_JPEG);
                            thumbnailBlob.upload(inputStream, thumbnailStream.toByteArray().length);

                            //将上传后的图片URL返
                            blobUploadEntity.setFileName(fileName);
                            blobUploadEntity.setUrl(blob.getUri().toString());
                            blobUploadEntity.setMinImgUrl(thumbnailBlob.getUri().toString());
                            blobUploadEntity.setType(extensionName);
                            BufferedImage sourceImg = ImageIO.read(new FileInputStream(file));
                            blobUploadEntity.setSize(sourceImg.getWidth() + "*" + sourceImg.getHeight());

                            blobUploadEntities.add(blobUploadEntity);
                        } catch (Exception e) {
                            map.put("fileName:",fileName);
                            results.add(Result.fail("文件上传异常", map));
                            continue;
                        }
                    }else {
                        map.put("fileName",fileName);
                        results.add(Result.fail("上传文件为空", map));
                        continue;
                    }
                }
                results.add(Result.success("文件上传成功", blobUploadEntities));
            }else{
                map.put("错误原因：","未选择上传的文件");
                results.add(Result.fail("未选择上传的文件", map));
            }
        } catch (Exception e) {
            results.add(Result.fail("文件上传异常", map));
        }
        return results;
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
