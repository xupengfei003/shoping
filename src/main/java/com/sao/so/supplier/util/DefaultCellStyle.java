package com.sao.so.supplier.util;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * excel 表格样式设置接口
 */
public interface DefaultCellStyle {
    /**|
     * 设置默认样式
     * @param workbook
     * @return
     */
    CellStyle setCellStyle(Workbook workbook);

    /**
     * 设置日期单元格样式
     * @param workbook
     * @return
     */
    CellStyle setDateCellStyle(Workbook workbook);

    /**
     * 设置数字单元格样式
     *
     * @param workbook
     * @return
     */
/*    CellStyle setNumberCellStyle(Workbook workbook);*/

}