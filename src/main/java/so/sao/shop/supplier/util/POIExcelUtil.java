package so.sao.shop.supplier.util;


import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import so.sao.shop.supplier.config.CommConstant;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
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
	public static void writeOutExcel(String urlFile, List<Object[]> dataList, int startRow, HttpServletResponse response,
									 String sheetName ,String fileName) {

		try {
			XSSFWorkbook workbook1 =new XSSFWorkbook(getInputStream(urlFile));
			//内存中只创建100个对象，写临时文件，当超过100条，就将内存中不用的对象释放。
			SXSSFWorkbook wb = new SXSSFWorkbook(workbook1, 100);
			Sheet sheet = null;     //工作表对象
			Row nRow = null;        //行对象
			Cell nCell = null;      //列对象
			int rowNo = 0;      //总行号
			int pageRowNo = startRow;  //页行号
			for (Object[] o : dataList) {


				if(rowNo% CommConstant.MAX_ROWNUM==0){
					if(rowNo != 0){
						sheet = wb.createSheet(sheetName+(rowNo/ CommConstant.MAX_ROWNUM));//建立新的sheet对象
					}else {
						sheet = wb.getSheetAt(rowNo/ CommConstant.MAX_ROWNUM);        //动态指定当前的工作表
					}


					if(rowNo != 0){
						nRow = sheet.createRow(0);    //新建行对象
						nRow.setHeightInPoints(20);
						// 标题赋值
						for (int i = 0; i < CommConstant.EXCEL_OUT_TITLES.length; i++) {
							nCell = nRow.createCell(i);
							sheet.setColumnWidth((short) i, (short) 6060);
							CellStyle newStyle = wb.createCellStyle();
							newStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
							newStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
							nCell.setCellStyle(newStyle);
							fillCellValue(nCell, CommConstant.EXCEL_OUT_TITLES[i]);

						}
					}
					pageRowNo = startRow; //每当新建了工作表就将当前工作表的行号重置为0
				}
				rowNo++;
				nRow = sheet.createRow(pageRowNo++);    //新建行对象
				nRow.setHeightInPoints(20);

				for (int i = 0; i < o.length; i++) {
					nCell = nRow.createCell(i);
					if(i == 0){
						fillCellValue(nCell, rowNo);
					}else {
						fillCellValue(nCell, o[i]);
					}

				}
			}

			//输出excel
			response.setHeader("content-disposition", String.format("attachment;filename*=utf-8'zh_cn'%s",
					URLEncoder.encode(fileName+".xlsx", "utf-8")));
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
	 * 填充单元格
	 * @param cell
	 * @param val
	 */
	private static void fillCellValue(Cell cell, Object val) {
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
//	private static void fillCellValue(HSSFCell cell, Object val) {
//		try{
//			if(val!=null) {
//				if(val instanceof Double) {
//					cell.setCellValue((Double)val);
//				} else if(val instanceof BigDecimal) {
//					cell.setCellValue(((BigDecimal)val).doubleValue());
//				} else if(val instanceof Integer) {
//					cell.setCellValue((Integer)val);
//				} else {
//					cell.setCellValue(val+"");
//				}
//			}
//		} catch (Exception ex) {
//			logger.error("写入单元格数据失败: [Row: " + cell.getRowIndex() + "; Cell: " + cell.getColumnIndex() + " | data: "
//					+ val + "]. ErrorMsg: " , ex);
//		}
//	}

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

	/**
	 * 生成excl文件
	 * @param data 数据
	 * @param titles 标题
	 * @param filename 文件名
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public static void exportExcel(List<Object[]> data, String[] titles, String filename, HttpServletResponse response) throws Exception {
		/**
		 * 1、声明excel位置
		 * 2、创建excel文件
		 * 3、创建excel标题
		 * 4、写入单元格内容
		 * 5、返回生成的文件
		 */
		XSSFWorkbook workbook = null;
		try {

			//设置响应参数
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			filename = new String(filename.getBytes("Utf-8"), "iso-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Type", "application/octet-stream");
			ServletOutputStream out = response.getOutputStream();

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
			workbook.write(out);
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}
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

}
