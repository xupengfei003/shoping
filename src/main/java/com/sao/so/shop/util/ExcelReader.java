package com.sao.so.shop.util;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by QuJunLong on 2017/7/12.
 */
public class ExcelReader {
    static private Workbook wb;
    static private Sheet sheet;
    static private Row row;
    private static DecimalFormat df = new DecimalFormat("0");             //数字格式，防止长数字成为科学计数法形式，或者int变为double形式

    /**
     * @param fileName ：Excel 文件路径
     * @return String[]
     * @method ：readExcelTitle<br>
     * @describe ：读取 Excel 文件<br>
     * @author ：wanglongjie<br>
     * @createDate ：2015年8月31日下午2:41:25 <br>
     */
    public static String[] readExcelTitle(String fileName, int titleRow) {
        InputStream is;
        try {
            is = new FileInputStream(fileName);
            String postfix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            if (postfix.equals(".xls")) {
                // 针对 2003 Excel 文件
                wb = new HSSFWorkbook(new POIFSFileSystem(is));
                sheet = wb.getSheetAt(0);
            } else {
                // 针对2007 Excel 文件
                wb = new XSSFWorkbook(is);
                sheet = wb.getSheetAt(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(titleRow);// 获取第一行（约定第一行是标题行）
        int colNum = row.getPhysicalNumberOfCells();// 获取行的列数
        String[] titles = new String[colNum];
        for (int i = 0; i < titles.length; i++) {
            titles[i] = getCellFormatValue(row.getCell(i));
        }
        return titles;
    }

    /**
     * @param fileName ：Excel 文件路径
     * @return List<Map<String,String>>
     * @method ：readExcelContent<br>
     * @describe ：读取 Excel 内容<br>
     * @author ：wanglongjie<br>
     * @createDate ：2015年8月31日下午3:12:06 <br>
     */
    public static List<Map<String, String>> readExcelContent(String fileName, int contentStartRow) {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> content = null;
        try {
            InputStream is;
            is = new FileInputStream(fileName);
            String postfix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            if (postfix.equals(".xls")) {
                // 针对 2003 Excel 文件
                wb = new HSSFWorkbook(new POIFSFileSystem(is));
                sheet = wb.getSheetAt(0);
            } else {
                // 针对2007 Excel 文件
                wb = new XSSFWorkbook(is);
                sheet = wb.getSheetAt(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = wb.getSheetAt(0);
        int rowNum = sheet.getLastRowNum();// 得到总行数
        row = sheet.getRow(contentStartRow - 1);//和title一样
        int colNum = row.getPhysicalNumberOfCells();
        String titles[] = readExcelTitle(fileName, contentStartRow - 1);
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = contentStartRow; i <rowNum; i++) {
            int j = 0;
            row = sheet.getRow(i);
            content = new LinkedHashMap<>();
            do {
                content.put(titles[j], getCellFormatValue(row.getCell(j)).trim());
                j++;
            } while (j < colNum);
            list.add(content);
        }
        return list;
    }
	
	 /**
     * @param fileName ：Excel 文件路径
     * @return List<Map<String,String>>
     * @method ：readExcelContent<br>
     * @describe ：读取 Excel 内容<br>
     * @author ：wanglongjie<br>
     * @createDate ：2015年8月31日下午3:12:06 <br>
     */
    public static String [][] readExcel(String fileName, int contentStartRow) {
        List<Map<String, String>> list = new ArrayList<>();
		String [][] strings;
        Map<String, String> content = null;
        try {
            InputStream is;
            is = new FileInputStream(fileName);
            String postfix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            if (postfix.equals(".xls")) {
                // 针对 2003 Excel 文件
                wb = new HSSFWorkbook(new POIFSFileSystem(is));
                sheet = wb.getSheetAt(0);
            } else {
                // 针对2007 Excel 文件
                wb = new XSSFWorkbook(is);
                sheet = wb.getSheetAt(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = wb.getSheetAt(0);
        int rowNum = sheet.getLastRowNum();// 得到总行数
        row = sheet.getRow(contentStartRow - 1);//和title一样
        int colNum = row.getPhysicalNumberOfCells();
		strings=new String [rowNum][colNum];
        String titles[] = readExcelTitle(fileName, contentStartRow - 1);
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 0; i <rowNum; i++) {
           
            row = sheet.getRow(i);
            content = new LinkedHashMap<>();
			
			for (int j=0;j<colNum;j++){
				strings[i][j]=getCellFormatValue(row.getCell(j)).trim();
			}
           
           
        }
        return strings;
    }

    /**
     * 根据Cell类型设置数据
     *
     * @param cell
     * @return
     */
    private static String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case Cell.CELL_TYPE_NUMERIC:
                    // 如果是纯数字取得当前Cell的数值
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        //  如果是date类型则 ，获取该cell的date值
                        cellvalue = HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();
                    } else { // 纯数字
                        cellvalue = df.format(cell.getNumericCellValue());
                    }

                    break;
                case Cell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        // 方法2：这样子的data格式是不带带时分秒的：2011-10-12
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue = sdf.format(date);
                    } else {
                        // 如果是纯数字取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case Cell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getStringCellValue();
                    break;
                default:
                    // 默认的Cell值
                    cellvalue = cell.getRichStringCellValue() == null ? null : cell.getRichStringCellValue().toString();

            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }

    /* public static void main(String[] args) {
        String file = "C://Users//acer//Desktop//Commodity.xls";
      //  List<Map<String, String>> list = ExcelReader.readExcelContent(file,2);
        Map<String, String> map = null;
        String [][] strings2=ExcelReader.readExcel(file,2);
       for (int i = 0; i < list.size(); i++) {
            map = list.get(i);
            Entry<String, String> entry = null;
            for (Iterator<Entry<String, String>> it = map.entrySet().iterator(); it.hasNext(); ) {
                entry = it.next();
                System.out.println(entry.getKey() + "-->" + entry.getValue());
            }
            System.out.println("............");
        }
        for(int m=0;m<strings2.length;m++){
            for(int n=0;n<strings2[m].length;n++){
                System.out.print(strings2[m][n]);
            }

        }
    }*/
}
