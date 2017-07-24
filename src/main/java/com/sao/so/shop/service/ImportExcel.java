package com.sao.so.shop.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Created by acer on 2017/7/19.
 */
public interface ImportExcel {
    /**
     * 上传excel文件到临时目录后并开始解析
     * @param fileName
     * @param mfile
     * @return
     */
    public String batchImport(String fileName, MultipartFile mfile);
    /**
     * 解析Excel里面的数据
     * @param wb 上传的excel版本
     * @return 插库条数
     */
    public String readExcelValue(Workbook wb, File tempFile);
}
