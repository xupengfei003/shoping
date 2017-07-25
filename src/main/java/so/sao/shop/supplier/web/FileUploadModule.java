package so.sao.shop.supplier.web;

import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.util.BlobHelper;
import so.sao.shop.supplier.pojo.vo.BlobUpload;
import so.sao.shop.supplier.util.MD5Util;
import so.sao.shop.supplier.config.StorageConfig;
import so.sao.shop.supplier.pojo.BaseResult;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/7/25.
 */
@RestController
@RequestMapping("file")
public class FileUploadModule {

    @Autowired
    private StorageConfig storageConfig;

    //设置缩略图的宽高
    private static int thumbnailWidth = 150;
    private static int thumbnailHeight = 100;

    @PostMapping(value = "/upload")
    public Object uploadFile(String id, int type, MultipartFile[] multipartFile)
    {
        List<BlobUpload> blobUploadEntities = new ArrayList<BlobUpload>();
        try
        {
            if(multipartFile != null)
            {

                //获取或创建container
                CloudBlobContainer blobContainer = BlobHelper.getBlobContainer(id.toLowerCase(), storageConfig);
                for (int i=0;i<multipartFile.length;i++)
                {
                    MultipartFile tempMultipartFile = multipartFile[i];
                    if (!tempMultipartFile.isEmpty())
                    {
                        try
                        {
                            //过滤非jpg,png格式的文件
                            if(!(tempMultipartFile.getContentType().toLowerCase().equals("image/jpg")
                                    || tempMultipartFile.getContentType().toLowerCase().equals("image/jpeg")
                                    || tempMultipartFile.getContentType().toLowerCase().equals("image/png")))
                            {
                                BaseResult resultMsg = new BaseResult(1,"上传的文件不是jpg或png格式");
                                return resultMsg;
                            }
                            //拼装blob的名称(前缀名称+文件的md5值+文件扩展名称)
                            String checkSum = MD5Util.getMD5(tempMultipartFile.getInputStream());
                            String fileExtension = getFileExtension(tempMultipartFile.getOriginalFilename()).toLowerCase();
                            String preName = getBlobPreName(type, false).toLowerCase();
                            String blobName = preName + checkSum + fileExtension;

                            //设置文件类型，并且上传到azure blob
                            CloudBlockBlob blob = blobContainer.getBlockBlobReference(blobName);
                            blob.getProperties().setContentType(tempMultipartFile.getContentType());
                            blob.upload(tempMultipartFile.getInputStream(), tempMultipartFile.getSize());

                            //生成缩略图，并上传至AzureStorage
                            BufferedImage img = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
                            img.createGraphics().drawImage(ImageIO.read(tempMultipartFile.getInputStream()).getScaledInstance(thumbnailWidth, thumbnailHeight, Image.SCALE_SMOOTH),0,0,null);
                            ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream();
                            ImageIO.write(img, "jpg", thumbnailStream);
                            InputStream inputStream = new ByteArrayInputStream(thumbnailStream.toByteArray());

                            String thumbnailPreName = getBlobPreName(type, true).toLowerCase();
                            String thumbnailCheckSum = MD5Util.getMD5(new ByteArrayInputStream(thumbnailStream.toByteArray()));
                            String blobThumbnail = thumbnailPreName + thumbnailCheckSum + ".jpg";
                            CloudBlockBlob thumbnailBlob = blobContainer.getBlockBlobReference(blobThumbnail);
                            thumbnailBlob.getProperties().setContentType("image/jpeg");
                            thumbnailBlob.upload(inputStream, thumbnailStream.toByteArray().length);

                            //将上传后的图片URL返回
                            BlobUpload blobUploadEntity = new BlobUpload();
                            blobUploadEntity.setFileName(tempMultipartFile.getOriginalFilename());
                            blobUploadEntity.setFileUrl(blob.getUri().toString());
                            blobUploadEntity.setThumbnailUrl(thumbnailBlob.getUri().toString());

                            blobUploadEntities.add(blobUploadEntity);
                        }
                        catch(Exception e)
                        {
                            BaseResult resultMsg = new BaseResult(2,"文件上传异常");
                            return resultMsg;
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            BaseResult resultMsg = new BaseResult(2,"文件上传异常");
            return resultMsg;
        }

        BaseResult resultMsg = new BaseResult(2,"文件上传异常");
        return resultMsg;
    }

    private String getFileExtension(String fileName)
    {
        int position = fileName.indexOf('.');
        if (position > 0)
        {
            String temp = fileName.substring(position);
            return temp;
        }
        return "";
    }

    private String getBlobPreName(int fileType, Boolean thumbnail)
    {
        String afterName = "";
        if (thumbnail)
        {
            afterName = "thumbnail/";
        }

        switch (fileType)
        {
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
