package so.sao.shop.supplier.util;


import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * POI工具类
 */
public class POIExcelUtil {
	private static Logger logger = Logger.getLogger(POIExcelUtil.class);

	/**
	 * 将数据源导入到指定的Excel模板
	 * @param urlFile  Excel模板
	 * @param dataList 数据源
	 * @param startRow	Excel中从第几行开始导入
	 * @param response
	 * @param sheetName	sheet名字
	 * @param fileName	导出的文件名
	 */
	public static void writeExcel(String urlFile, List<Object[]> dataList, int startRow, HttpServletResponse response,
								  String sheetName ,String fileName) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook(getInputStream(urlFile));
			HSSFSheet sheet = wb.getSheetAt(0);
			wb.setSheetName(0, sheetName);
			for (Object[] o : dataList) {
				//先得到指定样式的行
				HSSFRow row = sheet.getRow(startRow);
				row.setHeightInPoints(20);
				for (int i = 0; i < o.length; i++) {
					HSSFCell cell = row.getCell(i);
					fillCellValue(cell, o[i]);
				}
				startRow = startRow + 1;
				//copy上一行，保持格子样式和上一行一致
				HSSFRow nextRow = sheet.createRow(startRow);
				copyRow(wb, row, nextRow);
			}
			/**
			 * 清空没用的row
			 */
			for(int i=startRow; i<=sheet.getLastRowNum(); i++) {
				sheet.removeRow(sheet.getRow(i));
			}
			//输出excel
			response.setHeader("content-disposition", String.format("attachment;filename*=utf-8'zh_cn'%s",
					URLEncoder.encode(fileName+".xls", "utf-8")));
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			wb.write(toClient);
			toClient.flush();
			toClient.close();
		} catch (Exception ex) {
			logger.error("写入文件失败: [File: " + urlFile + " | data: "
					+ dataList + "]. ", ex);
		}
	}

	/**
     * 得到模板
	 * @param urlFile 模板路径
	 * @return
     */
	private static InputStream getInputStream(String urlFile) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		InputStream inputStream = null;
		MediaType xls = MediaType.valueOf("application/x-xls");//.xls模式
		List list = new ArrayList<>();
		list.add(xls);
		headers.setAccept(list);
		ResponseEntity<byte[]> response = restTemplate.exchange(
				urlFile,
				HttpMethod.GET,
				new HttpEntity<byte[]>(headers),
				byte[].class);
		byte[] result = response.getBody();
		inputStream = new ByteArrayInputStream(result);
		try {
			inputStream.close();
		} catch (IOException e) {
			logger.error("读取文件异常！", e);
		}
		return inputStream;

	}

	/**
	 * 填充单元格
	 * @param cell
	 * @param val
	 */
	private static void fillCellValue(HSSFCell cell, Object val) {
		try{
			if(val!=null) {
				if(val instanceof Double) {
					cell.setCellValue((Double)val);
				} else if(val instanceof BigDecimal) {
					cell.setCellValue(((BigDecimal)val).doubleValue());
				} else if(val instanceof Integer) {
					cell.setCellValue((Integer)val);
				} else {
					cell.setCellValue(val+"");
				}
			}
		} catch (Exception ex) {
			logger.error("写入单元格数据失败: [Row: " + cell.getRowIndex() + "; Cell: " + cell.getColumnIndex() + " | data: "
					+ val + "]. ErrorMsg: " , ex);
		}
	}

	/**
	 * 行复制功能
	 *
	 * @param fromRow
	 * @param toRow
	 */
	public static void copyRow(Workbook wb, Row fromRow, Row toRow) {
		toRow.setHeight(fromRow.getHeight());
		for(Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext(); ) {
			Cell tmpCell = (Cell) cellIt.next();
			Cell newCell = toRow.createCell(tmpCell.getColumnIndex());
		}
		Sheet worksheet = fromRow.getSheet();
		for(int i = 0; i < worksheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if(cellRangeAddress.getFirstRow() == fromRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(toRow.getRowNum(), (toRow.getRowNum() +
						(cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())), cellRangeAddress
						.getFirstColumn(), cellRangeAddress.getLastColumn());
				worksheet.addMergedRegionUnsafe(newCellRangeAddress);
			}
		}
	}

	/**
	 * 复制单元格
	 *
	 * @param srcCell
	 * @param distCell
	 */
	public static void copyCell(Workbook wb, Cell srcCell, Cell distCell) {
		CellStyle newStyle = wb.createCellStyle();
		CellStyle srcStyle = srcCell.getCellStyle();
		newStyle.cloneStyleFrom(srcStyle);
		newStyle.setFont(wb.getFontAt(srcStyle.getFontIndex()));
		//样式
		distCell.setCellStyle(newStyle);
		// 不同数据类型处理
		CellType srcCellType = srcCell.getCellTypeEnum();
		distCell.setCellType(srcCellType);
	}

}
