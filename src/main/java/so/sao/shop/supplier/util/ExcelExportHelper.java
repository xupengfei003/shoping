package so.sao.shop.supplier.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by wyy on 2017/8/11.
 */
public class ExcelExportHelper {

    /**
     * 生成excl文件
     * @param data 数据
     * @param titles 标题
     * @return
     * @throws IOException
     */
    public static XSSFWorkbook exportExcel(List<Object[]> data,String[] titles) {
        /**
         * 1、声明excel位置
         * 2、创建excel文件
         * 3、创建excel标题
         * 4、写入单元格内容
         * 5、返回生成的文件
         */

        //声明excel位置
//        Resource resource = new ClassPathResource("file/");
//        String filePath = resource.getFile().getCanonicalPath()+File.separator+System.currentTimeMillis()+".xlsx";
//        File file = new File(filePath);
        XSSFWorkbook workbook = null;
        CellStyle defaulCellStyle = null;
        CellStyle defaulTitleCellStyle = null;

            //创建excel文件
            workbook = new XSSFWorkbook();// 创建一个Excel文件
            defaulTitleCellStyle = getDefaultTitleCellStyle(workbook);
            defaulCellStyle = getDefaultCellStyle(workbook);
            XSSFSheet sheet = workbook.createSheet("sheet1");// 创建一个Excel的Sheet

            Row row = sheet.createRow(0);
            Cell cell = null;

            //创建标题行
            for (int i = 0; i < titles.length; i++) {
                cell = row.createCell(i);
                cell.setCellValue(titles[i]);
                sheet.setColumnWidth(i, 6000);//设置宽度
                cell.setCellStyle(defaulTitleCellStyle);
            }

            //写入单元格内容
            for (int i = 0; i < data.size(); i++) {
                row = sheet.createRow(i + 1);
                Object[] record = data.get(i);
                for (int j = 0; j < record.length; j++) {
                    cell = row.createCell(j);
                    setCellValue(record[j], cell);
                    cell.setCellStyle(defaulCellStyle);
                }
            }
        return workbook;
    }

    /**
     * 默认的excel
     * @param workbook
     * @return
     */
    private static CellStyle getDefaultTitleCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle cellStyle =  workbook.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        return cellStyle;
    }

    /**
     * 默认的单元个样式
     * @param workbook
     * @return
     */
    private static CellStyle getDefaultCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle cellStyle =  workbook.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        return cellStyle;
    }



    /**
     * 为单元格设置值
     * @param o 值
     * @param cell 单元格
     */
    private static void setCellValue(Object o, Cell cell) {
        if(o == null){
            cell.setCellValue("");
            return;
        }
        switch (o.getClass().getName()){
            case "java.lang.Integer":
                cell.setCellValue(((Integer)o).doubleValue());
                break;
            case "java.lang.Double":
                cell.setCellValue((Double)o);
                break;
            case "java.util.Date":
                cell.setCellValue((Date)o);
                break;
            case "java.lang.Boolean":
                cell.setCellValue((Boolean)o);
                break;
            default:
                cell.setCellValue(String.valueOf(o));
                break;
        }

    }

    public static void main(String[] args) {
        ExcelExportHelper exportExcel = new ExcelExportHelper();
        List<Object[]> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Object[] o = new Object[]{1,"a",3};
            data.add(o);
        }
        String[] titles = new String[]{"a","b","c"};


            ExcelExportHelper.exportExcel(data,titles);

    }

}

