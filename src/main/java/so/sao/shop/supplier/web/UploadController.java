package so.sao.shop.supplier.web;

import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.config.StorageConfig;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.vo.BlobUpload;
import so.sao.shop.supplier.util.BlobHelper;
import so.sao.shop.supplier.util.Constant;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * 文件上传 Controller
 * Created by QuJunLong on 2017/7/19.
 */
@RestController
@RequestMapping("/upload")
@Api(description = "图片上传接口")
public class UploadController {
    @Autowired
    private StorageConfig storageConfig;

    @ApiOperation(value = "文件批量上传", notes = "")
    @PostMapping(value = "/file")
    public Object loadFile(@RequestPart("file") MultipartFile[] multipartFile) {
        List<BlobUpload> blobUploadEntities = new ArrayList<BlobUpload>();
        Map<String,String> map = new HashMap<>();
        try {
            if (multipartFile != null) {
                //获取或创建container
                CloudBlobContainer blobContainer = BlobHelper.getBlobContainer(Constant.AZURE_CONTAINER.toLowerCase(), storageConfig);
                for (int i = 0; i < multipartFile.length; i++) {
                    MultipartFile tempMultipartFile = multipartFile[i];
                    if (!tempMultipartFile.isEmpty()) {
                        try {
                           //获取上传文件的名称及文件类型
                            BlobUpload blobUploadEntity = new BlobUpload();
                            String fileName = tempMultipartFile.getOriginalFilename();
                            String extensionName = StringUtils.substringAfter(fileName, ".");

                            //过滤非jpg,png,jpeg,gif格式的文件
                            if (!(tempMultipartFile.getContentType().toLowerCase().equals("image/jpg")
                                    || tempMultipartFile.getContentType().toLowerCase().equals("image/jpeg")
                                    || tempMultipartFile.getContentType().toLowerCase().equals("image/png")
                                    || tempMultipartFile.getContentType().toLowerCase().equals("image/gif"))) {
                                map.put("fileName",fileName);
                                return new Result(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE, "上传的文件中包含非jpg/png/jpeg/gif格式",map);
                            }

                            //拼装blob的名称(新的图片文件名 =UUID+"."图片扩展名)
                            String newFileName = UUID.randomUUID() + "." + extensionName;
                            String preName = getBlobPreName(extensionName, false).toLowerCase();
                            String blobName = preName + newFileName;

                            //设置文件类型，并且上传到azure blob
                            CloudBlockBlob blob = blobContainer.getBlockBlobReference(blobName);
                            blob.getProperties().setContentType(tempMultipartFile.getContentType());
                            blob.upload(tempMultipartFile.getInputStream(), tempMultipartFile.getSize());

                            //生成缩略图，并上传至AzureStorage
                            BufferedImage img = new BufferedImage(Constant.THUMBNAIL_DEFAULT_WIDTH, Constant.THUMBNAIL_DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
                            img.createGraphics().drawImage(ImageIO.read(tempMultipartFile.getInputStream()).getScaledInstance(Constant.THUMBNAIL_DEFAULT_WIDTH, Constant.THUMBNAIL_DEFAULT_HEIGHT, Image.SCALE_SMOOTH), 0, 0, null);
                            ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream();
                            ImageIO.write(img, "jpg", thumbnailStream);
                            InputStream inputStream = new ByteArrayInputStream(thumbnailStream.toByteArray());

                            String thumbnailPreName = getBlobPreName(extensionName, true).toLowerCase();
                            String newThumbnailName = UUID.randomUUID().toString();
                            String blobThumbnail = thumbnailPreName + newThumbnailName + ".jpg";
                            CloudBlockBlob thumbnailBlob = blobContainer.getBlockBlobReference(blobThumbnail);
                            thumbnailBlob.getProperties().setContentType("image/jpeg");
                            thumbnailBlob.upload(inputStream, thumbnailStream.toByteArray().length);

                            //将上传后的图片URL返
                            blobUploadEntity.setFileName(fileName);
                            blobUploadEntity.setUrl(blob.getUri().toString());
                            blobUploadEntity.setMinImgUrl(thumbnailBlob.getUri().toString());
                            blobUploadEntity.setType(extensionName);
                            try {
                                BufferedImage sourceImg = ImageIO.read(tempMultipartFile.getInputStream());
                                blobUploadEntity.setSize(sourceImg.getWidth() + "*" + sourceImg.getHeight());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            blobUploadEntities.add(blobUploadEntity);
                        } catch (Exception e) {
                            return new BaseResult(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE, "文件上传异常");
                        }
                    }else {
                        map.put("fileName",tempMultipartFile.getOriginalFilename());
                        return new Result(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE, "上传文件为空",map);
                    }
                }
                return new Result(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS, "文件上传成功", blobUploadEntities);
            }else{
                return new BaseResult(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE, "请选择需要上传的文件");
            }
        } catch (Exception e) {
            return new BaseResult(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE, "文件上传异常");
        }
    }

    /**
     * 获取图片名父目录
     * @param fileType
     * @param thumbnail
     * @return
     */
    private String getBlobPreName(String fileType, Boolean thumbnail)
    {
        String afterName = "";
        if (thumbnail)
        {
            afterName = "thumbnail/";
        }
        switch (fileType)
        {
            case "png":
                return "png/" + afterName;
            case "jpg":
                return "jpg/" + afterName;
            case "jpeg":
                return "jpeg/" + afterName;
            case "gif":
                return "gif/" + afterName;
            default :
                return "";
        }
    }
}
