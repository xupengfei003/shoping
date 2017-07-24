package so.sao.shop.supplier.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import so.sao.shop.supplier.pojo.output.CommodityExportOutput;

import java.util.List;
import java.util.Map;

/**
 * 商品表对应的导出工具类
 */
public class CommodityExcelView extends ExcelView{
    DefaultCellStyle defaultCellStyle = new DefaultCellStyleImpl();
    @Override
    public void setRow(Sheet sheet, Map<String, Object> map) {

        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("商品编码");
        header.getCell(0).setCellStyle(super.cellStyle);
        header.createCell(1).setCellValue("商品品牌");
        header.getCell(1).setCellStyle(super.cellStyle);
        header.createCell(2).setCellValue("商品名称");
        header.getCell(2).setCellStyle(super.cellStyle);
        header.createCell(3).setCellValue("商家编码");
        header.getCell(3).setCellStyle(super.cellStyle);
        header.createCell(4).setCellValue("商品ID");
        header.getCell(4).setCellStyle(super.cellStyle);
        header.createCell(5).setCellValue("单位");
        header.getCell(5).setCellStyle(super.cellStyle);
        header.createCell(6).setCellValue("规格");
        header.getCell(6).setCellStyle(super.cellStyle);
        header.createCell(7).setCellValue("规格值");
        header.getCell(7).setCellStyle(super.cellStyle);
        header.createCell(8).setCellValue("成本价");
        header.getCell(8).setCellStyle(super.cellStyle);
        header.createCell(9).setCellValue("市场价");
        header.getCell(9).setCellStyle(super.cellStyle);
        header.createCell(10).setCellValue("库存量");
        header.getCell(10).setCellStyle(super.cellStyle);
        header.createCell(11).setCellValue("创建时间");
        header.getCell(11).setCellStyle(super.cellStyle);
        header.createCell(12).setCellValue("更新时间");
        header.getCell(12).setCellStyle(super.cellStyle);
        header.createCell(13).setCellValue("商品状态");
        header.getCell(13).setCellStyle(super.cellStyle);

        List<CommodityExportOutput> list = (List<CommodityExportOutput>) map.get("members");
        int rowCount = 1;
        for (CommodityExportOutput commoditys : list) {
            CommodityExportOutput commodity = check(commoditys);
            Row commodityRow = sheet.createRow(rowCount++);
            commodityRow.createCell(0).setCellValue(commodity.getCode69());//商品编码
            commodityRow.createCell(1).setCellValue(commodity.getBrandName());//商品品牌
            commodityRow.createCell(2).setCellValue(commodity.getCommName());//商品名称
            commodityRow.createCell(3).setCellValue(commodity.getSupplierCode());//商家编码
            commodityRow.createCell(4).setCellValue(commodity.getCommId());//商品ID
            commodityRow.createCell(5).setCellValue(commodity.getUnit());//单位
            commodityRow.createCell(6).setCellValue(commodity.getRuleName());//规格名称
            commodityRow.createCell(7).setCellValue(commodity.getRuleValue());//规格值
            commodityRow.createCell(8).setCellValue(commodity.getPrice());//成本价
            commodityRow.createCell(9).setCellValue(commodity.getUnitPrice());//市场价
            commodityRow.createCell(10).setCellValue(commodity.getInventory());//库存量
            commodityRow.createCell(11).setCellValue(commodity.getCreatedAt());//创建时间
            commodityRow.createCell(12).setCellValue(commodity.getUpdatedAt());//更新时间
            int status = commodity.getStatus();
            commodityRow.createCell(13).setCellValue(Constant.getStatus(status));//商品状态
        }
    }

    protected CommodityExportOutput check(CommodityExportOutput commodityExportOutput){
        if(commodityExportOutput.getCode69() == null){
            commodityExportOutput.setCode69("");
        }else if(commodityExportOutput.getBrandName() == null){
            commodityExportOutput.setBrandName("");
        }else if(commodityExportOutput.getCommName() == null){
            commodityExportOutput.setCommName("");
        }else if(commodityExportOutput.getSupplierCode() == null){
            commodityExportOutput.setSupplierCode("");
        }else if(commodityExportOutput.getCommId() == null){
            commodityExportOutput.setCommId(0l);
        }else if(commodityExportOutput.getUnit() == null){
            commodityExportOutput.setUnit("");
        }else if(commodityExportOutput.getRuleName() == null){
            commodityExportOutput.setRuleName("");
        }else if(commodityExportOutput.getRuleValue() == null){
            commodityExportOutput.setRuleValue("");
        }else if(commodityExportOutput.getPrice() == null){
            commodityExportOutput.setPrice(0.0);
        }else if(commodityExportOutput.getUnitPrice() == null){
            commodityExportOutput.setUnitPrice(0.0);
        }else if(commodityExportOutput.getInventory() == null){
            commodityExportOutput.setInventory(0.0);
        }else if(commodityExportOutput.getCreatedAt() == null){
            commodityExportOutput.setCreatedAt(0l);
        }else if(commodityExportOutput.getUpdatedAt() == null){
            commodityExportOutput.setUpdatedAt(0l);
        }
        return commodityExportOutput;

    }
    @Override
    protected void setStyle(Workbook workbook) {
        super.cellStyle = defaultCellStyle.setCellStyle(workbook);
    }

    @Override
    protected void setDateCellStyle(Workbook workbook) {
        super.dateCellStyle = defaultCellStyle.setDateCellStyle(workbook);
    }
/*    @Override
    protected void setNumberCellStyle(Workbook workbook) {
        super.dateCellStyle = defaultCellStyle.setNumberCellStyle(workbook);
    }*/

}
