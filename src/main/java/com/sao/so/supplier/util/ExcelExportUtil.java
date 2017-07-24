package com.sao.so.supplier.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by acer on 2017/7/19.
 */
public class ExcelExportUtil {

    public static HSSFWorkbook generateExcel(List<Map<String, String>> list, String title) {
        HSSFWorkbook book = new HSSFWorkbook();
        try{
            File desFile = new File("d:\\人员表.xls");
            FileOutputStream fos = new FileOutputStream(desFile);
            HSSFSheet sheet = book.createSheet("Sheet1");
            sheet.autoSizeColumn(1, true);//自适应列宽度
            //样式设置
            HSSFCellStyle style = book.createCellStyle();
            style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            // 生成一个字体
            HSSFFont font = book.createFont();
            font.setColor(HSSFColor.VIOLET.index);
            font.setFontHeightInPoints((short) 12);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            // 把字体应用到当前的样式
            style.setFont(font);


            HSSFCellStyle style2 = book.createCellStyle();
            style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            //设置上下左右边框
            style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            //填充表头标题
            int colSize = list.get(0).entrySet().size();
            System.out.println("size:" + colSize);
            //合并单元格供标题使用(表名)
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colSize-1));
            HSSFRow firstRow = sheet.createRow(0);//第几行（从0开始）
            HSSFCell firstCell = firstRow.createCell(0);
            firstCell.setCellValue(title);
            firstCell.setCellStyle(style);

            //填充表头header
            HSSFRow row = sheet.createRow(1);
            Set<Map.Entry<String, String>> set = list.get(0).entrySet();
            List<Map.Entry<String, String>> l = new ArrayList<Map.Entry<String,String>>(set);
            System.out.println("l:" + l.size());
            for(int i=0; i< l.size(); i++) {
                String key = l.get(i).getKey();
                System.out.println(key);
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(key);
                cell.setCellStyle(style2);
            }

            //填充表格内容
            System.out.println("list:" + list.size());
            for(int i=0; i<list.size(); i++) {
                HSSFRow row2 = sheet.createRow(i+2);//index：第几行
                Map<String, String> map = list.get(i);
                Set<Map.Entry<String, String>> set2 = map.entrySet();
                List<Map.Entry<String, String>> ll = new ArrayList(set2);
                for(int j=0; j<ll.size(); j++) {
                    String val = ll.get(j).getValue();
                    HSSFCell cell = row2.createCell(j);//第几列：从0开始
                    cell.setCellValue(val);
                    cell.setCellStyle(style2);
                }
            }

//           book.write(fos);
//           fos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return book;
    }


 /*   public static void main(String[] args) throws Exception{
       // ExcelExportUtil.generateExcel()
    }*/
}
