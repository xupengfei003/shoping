package so.sao.shop.supplier.util;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * excel表格样式设置实现
 */
public class DefaultCellStyleImpl implements DefaultCellStyle {

    @Override
    public CellStyle setCellStyle(Workbook workbook) {
        // create style for header cells
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
        cellStyle.setFillPattern((short) 1);
        font.setBold(true);
        font.setColor(HSSFColor.WHITE.index);
        cellStyle.setFont(font);
        return cellStyle;
    }

    @Override
    public CellStyle setDateCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(workbook.createDataFormat().getFormat("yyyy年m月d日"));
        return cellStyle;
    }

    /*@Override
    public CellStyle setNumberCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00E+00"));
        return cellStyle;
    }*/
}