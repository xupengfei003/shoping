package so.sao.shop.supplier.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.config.azure.AzureBlobService;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.vo.CommBlobUpload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件上传 Controller
 * Created by QuJunLong on 2017/7/19.
 */
@RestController
@RequestMapping("/upload")
public class UploadController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AzureBlobService azureBlobService;

    @PostMapping(value = "/file")
    public Result loadFile(@RequestPart("file") MultipartFile[] multipartFile) {
        List<CommBlobUpload> blobUploadList = new ArrayList<CommBlobUpload>();
        try {
            if (multipartFile != null && multipartFile.length > 0) {
                Result result = azureBlobService.uploadFilesComm(multipartFile, blobUploadList);
                if(blobUploadList.isEmpty()){
                    return result;
                }else{
                    return Result.success("文件上传成功", blobUploadList);
                }
            }else{
                return Result.fail("请选择需要上传的文件");
            }
        } catch (Exception e) {
            logger.error("文件上传异常", e);
            return Result.fail("文件上传异常");
        }
    }

    /**
     * 获取图片名父目录 (判定是否为缩略图)
     * @param fileType
     * @param thumbnail
     * @return
     */
    public static String getBlobPreName(String fileType, Boolean thumbnail)
    {
        String afterName = "";
        if (thumbnail) {
            afterName = "thumbnail" + File.separator;//缩略图路径
        }
        return fileType + File.separator + afterName;
    }
}
