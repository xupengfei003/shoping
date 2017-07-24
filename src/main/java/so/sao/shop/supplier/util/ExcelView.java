package so.sao.shop.supplier.util;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 生成excel工具类
 */
public abstract class ExcelView extends AbstractXlsView {
    /**
     * 默认单元格样式
     */
    public CellStyle cellStyle;
    /**
     * 日期单元格样式
     */
    public CellStyle dateCellStyle;
    /**
     * 数字单元格样式
     */
   /* public CellStyle numberCellStyle;*/

    /**
     * 设置样式
     *
     * @param workbook
     */
    protected abstract void setStyle(Workbook workbook);
    protected abstract void setDateCellStyle(Workbook workbook);
    /*protected abstract void setNumberCellStyle(Workbook workbook);*/

    /**
     * 设置Row，由子类实现
     *
     * @param sheet
     * @param map
     */
    protected abstract void setRow(Sheet sheet, Map<String, Object> map);

    @Override
    protected void buildExcelDocument(Map<String, Object> map,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        String excelName = map.get("name").toString() + ".xls";
        String Agent = request.getHeader("User-Agent");
        if (null != Agent) {
            Agent = Agent.toLowerCase();
            if (Agent.indexOf("firefox") != -1) {
                response.setHeader("content-disposition", String.format("attachment;filename*=utf-8'zh_cn'%s", URLEncoder.encode(excelName, "utf-8")));

            } else {
                response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(excelName, "utf-8"));
            }
        }
        response.setContentType("application/ms-excel; charset=UTF-8");
        Sheet sheet = workbook.createSheet("Order Detail");
        sheet.setDefaultColumnWidth(20);
        this.setStyle(workbook);
        this.setDateCellStyle(workbook);
        /*this.setNumberCellStyle(workbook);*/
        setRow(sheet, map);
    }
}