package com.sao.so.shop.web;

import com.sao.so.shop.pojo.output.UploadOutput;
import com.sao.so.shop.util.Constant;
import com.sao.so.shop.util.FtpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * 文件上传 Controller
 * Created by QuJunLong on 2017/7/19.
 */
@RestController
@RequestMapping("/upload")
@Api(description = "图片上传接口")
public class UploadController {

    @Value("${web.upload-path}")
    private String webUploadPath;
    //获取ip地址
    @Value("${FTP_ADDRESS}")
    private String FTP_ADDRESS;
    //端口号
    @Value("${FTP_PORT}")
    private String FTP_PORT;
    //用户名
    @Value("${FTP_USERNAME}")
    private String FTP_USERNAME;
    //密码
    @Value("${FTP_PASSWORD}")
    private String FTP_PASSWORD;
    //基本路径
    @Value("${FTP_BASEPATH}")
    private String FTP_BASEPATH;
    //下载地址地基础url
    @Value("${IMAGE_BASE_URL}")
    private String IMAGE_BASE_URL;

    @ApiOperation(value="文件批量上传", notes="")
    @PostMapping(value="/file")
    public UploadOutput loadFile(MultipartFile file){
        UploadOutput upload = new UploadOutput();
        // 获得原始文件名
        String fileName = file.getOriginalFilename();
        if(!isImageFile(fileName)){
           throw new RuntimeException("图片格式不正确！");
        }
        // 获取图片的扩展名
        String extensionName = StringUtils.substringAfter(fileName, ".");
        // 新的图片文件名 = 获取时间戳+"."图片扩展名
        String newFileName = String.valueOf(System.currentTimeMillis()) + "." + extensionName;
        upload.setFileName(newFileName);
        upload.setType(extensionName);
        upload.setUrl(extensionName+File.separator+newFileName);
        upload.setBaseUrl(IMAGE_BASE_URL);
        try {
            BufferedImage sourceImg =ImageIO.read(file.getInputStream());
            upload.setSize(sourceImg.getWidth()+"*"+sourceImg.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //端口号
        int port = Integer.parseInt(FTP_PORT);
        //调用方法，上传文件
        try {
            boolean result = FtpUtil.uploadFile(FTP_ADDRESS, port,
                    FTP_USERNAME, FTP_PASSWORD, FTP_BASEPATH, extensionName,
                    newFileName, file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return upload;
    }
    @ApiOperation(value="文件批量上传", notes="")
    @PostMapping(value="/files")
    public List<UploadOutput> loadFiles(HttpServletRequest request){
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("file");
        List uploadList=new ArrayList();
        for (int i = 0; i < files.size(); i++) {
            UploadOutput upload = loadFile(files.get(i));
            uploadList.add(upload);
        }
        return uploadList;
    }

    /**
     * 判断文件是否为图片文件
     * @param fileName
     * @return
     */
    private Boolean isImageFile(String fileName) {
        String [] imgType = new String[]{Constant.IMG_FILE_JPG,Constant.IMG_FILE_JPEG,Constant.IMG_FILE_PNG,Constant.IMG_FILE_BMP,Constant.IMG_FILE_GIF};
        if(fileName==null) {
            return false;
        }
        fileName = fileName.toLowerCase();
        for(String type : imgType) {
            if(fileName.endsWith(type)) {
                return true;
            }
        }
        return false;
    }
}
