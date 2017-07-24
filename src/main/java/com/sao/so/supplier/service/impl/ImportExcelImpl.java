package com.sao.so.supplier.service.impl;

import com.sao.so.supplier.dao.AccountDao;
import com.sao.so.supplier.domain.Account;
import com.sao.so.supplier.domain.User;
import com.sao.so.supplier.service.AccountService;
import com.sao.so.supplier.service.ImportExcel;
import com.sao.so.supplier.util.ExcelImportUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by acer on 2017/7/19.
 */
@Service
public class ImportExcelImpl implements ImportExcel {
    private static Logger log = Logger.getLogger(ImportExcelImpl.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");
    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AccountService accountService;

    @Override
    public String batchImport(String fileName, MultipartFile mfile) {
        File uploadDir = new  File("C:\\fileupload");
        //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
        if (!uploadDir.exists()){
            uploadDir.mkdirs();
        }
        //得到上传文件后缀名
        String str = fileName.substring(fileName.lastIndexOf(".")+1);
        //新建一个文件
        File tempFile = new File("C:\\fileupload\\" + new Date().getTime() + "."+str);
        //初始化输入流
        InputStream is = null;
        try {
            //将上传的文件写入新建的文件中
            mfile.transferTo(tempFile);
            //根据新建的文件实例化输入流
            is = new FileInputStream(tempFile);
            //根据版本选择创建Workbook的方式
            Workbook wb = null;
            //根据文件名判断文件是2003版本还是2007版本
            if(ExcelImportUtils.isExcel2007(fileName)){
                wb = new XSSFWorkbook(is);
            }else{
                wb = new HSSFWorkbook(is);
            }
            //根据excel里面的内容读取知识库信息
            return readExcelValue(wb,tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(is !=null)
            {
                try{
                    is.close();
                }catch(IOException e){
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        return "导入出错！请检查数据格式！";
    }

    @Override
    public String readExcelValue(Workbook wb, File tempFile) {
        //错误信息接收器
        String errorMsg = "";
        //得到第一个shell
        Sheet sheet=wb.getSheetAt(0);
        //得到Excel的行数
        int totalRows=sheet.getPhysicalNumberOfRows();
        //总列数
        int totalCells = 0;
        //得到Excel的列数(前提是有行数)，从第二行算起
        if(totalRows>=3 && sheet.getRow(2) != null){
            totalCells=sheet.getRow(2).getPhysicalNumberOfCells();
        }
        List<Account> userKnowledgeBaseList=new ArrayList<Account>();
        Account tempUserKB;

        //循环Excel行数,从第四行开始。标题不入库
        for(int r=3;r<totalRows;r++){
            String rowMessage = "";
            Row row = sheet.getRow(r);
            tempUserKB = new Account();
            try{
                User user = new User();
                //循环Excel的列
                for(int c = 0; c <totalCells; c++){
                    Cell cell = row.getCell(c);

                    if(null!=cell){
                        if(c==0)
                            tempUserKB.setProviderName(cell.getStringCellValue());
                        else if(c==1)
                            tempUserKB.setResponsible(cell.getStringCellValue());
                        else if(c==2)
                            tempUserKB.setResponsiblePhone(cell.getStringCellValue());
                        else if(c==3)
                            tempUserKB.setLicense(cell.getStringCellValue());
                        else if(c==4){
                            if(cell.getStringCellValue()!=null){
                                tempUserKB.setLicenseTimeCreate(sdf.parse(cell.getStringCellValue()).getTime());
                            }
                        }
                        else if(c==5){
                            if(cell.getStringCellValue()!=null){
                                tempUserKB.setLicenseTimeEnd(sdf.parse(cell.getStringCellValue()).getTime());
                            }
                        }
                        else if(c==6)
                            tempUserKB.setRegistAddress(cell.getStringCellValue());
                        else if(c==7)
                            tempUserKB.setRegisterAddressDetail(cell.getStringCellValue());
                        else if(c==8)
                            tempUserKB.setBusinessType(cell.getStringCellValue());
                        else if(c==9)
                            tempUserKB.setContractResponsible(cell.getStringCellValue());
                        else if(c==10)
                            tempUserKB.setContractResponsiblePhone(cell.getStringCellValue());
                        else if(c==11)
                            tempUserKB.setContractLicense(cell.getStringCellValue());
                        else if(c==12) {
                            if (cell.getStringCellValue() != null) {
                                tempUserKB.setContractLicenseCreate(sdf.parse(cell.getStringCellValue()).getTime());
                            }
                        }
                        else if(c==13) {
                            if (cell.getStringCellValue() != null) {
                                tempUserKB.setContractLicenseEnd(sdf.parse(cell.getStringCellValue()).getTime());
                            }
                        }
                        else if(c==14)
                            tempUserKB.setContractRegisterAddress(cell.getStringCellValue());
                        else if(c==15)
                            tempUserKB.setContractRegisterAddressDetail(cell.getStringCellValue());
                        else if(c==16) {
                            if(cell.getStringCellValue()!=null){
                                tempUserKB.setContractCreateDate(sdf.parse(cell.getStringCellValue()).getTime());
                            }
                        }
                        else if(c==17) {
                            if(cell.getStringCellValue()!=null){
                                tempUserKB.setContractEndDate(sdf.parse(cell.getStringCellValue()).getTime());
                            }
                        }
                        else if(c==18)
                            tempUserKB.setBankName(cell.getStringCellValue());
                        else if(c==19)
                            tempUserKB.setBankNum(cell.getStringCellValue());
                        else if(c==20)
                            tempUserKB.setBankUserName(cell.getStringCellValue());
                        else if(c==21)
                            tempUserKB.setRemittanced(cell.getStringCellValue());
                    }
                    /**
                     * 此处增加用户信息，根据tempUserKB.getResponsiblePhone插入用户信息
                     * 查询用户id增加进tempUserKB
                     */
                    Long id = accountService.saveUser(tempUserKB.getContractResponsiblePhone());
                    if(id!=0l){
                        tempUserKB.setUserId(id);
                    }else{
                        return "插入用户信息失败!";
                    }
                }
                tempUserKB.setUploadMode("2");
                tempUserKB.setCreateDate(new Date().getTime());
                userKnowledgeBaseList.add(tempUserKB);
            }catch(Exception e){
                log.error("excel数据解析异常"+e);
            }
        }
        //删除上传的临时文件
        if(tempFile.exists()){
            tempFile.delete();
        }

        //导入到数据库
        int i = accountDao.saveBatch(userKnowledgeBaseList);
        errorMsg = "导入"+(totalRows-3)+"行,成功插入"+i+"行";
        return errorMsg;
    }
}
