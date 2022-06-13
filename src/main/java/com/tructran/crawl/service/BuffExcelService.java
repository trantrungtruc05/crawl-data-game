package com.tructran.crawl.service;

import com.tructran.crawl.model.CompareResultEntity;
import com.tructran.crawl.model.ExcelFileEntity;
import com.tructran.crawl.repository.BuffPageRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BuffExcelService  {


    @Autowired
    private BuffPageRepository buffPageRepository;

    @Value("${export.filePath}")
    private String filePath;

    public List<ExcelFileEntity> generateExcelList() {
        try{
            List<CompareResultEntity>  compareResultEntityList = buffPageRepository.getResult();
            List<ExcelFileEntity> excelFileEntityList = new ArrayList<>();

            for(CompareResultEntity compareResultEntity : compareResultEntityList){

                Long percentEmpire;
                Long percentBuffSell;

                if(compareResultEntity.getEmpirePrice() != 0){
                    Long v1 = compareResultEntity.getBuffPrice() - compareResultEntity.getEmpirePrice();
                    Double v2 = v1 / Double.valueOf(compareResultEntity.getBuffPrice());
                    percentEmpire = Math.round(v2 * 100);

                }else{
                    percentEmpire = 0L;
                }

                if(compareResultEntity.getBuffSellPriceVnd() != null){
                    Long diffBuff = compareResultEntity.getBuffSellPriceVnd() - compareResultEntity.getEmpirePrice();
                    Double rateBuff = diffBuff / Double.valueOf(compareResultEntity.getEmpirePrice());
                    percentBuffSell = Math.round(rateBuff * 100);
                }else{
                    percentBuffSell = 0L;
                }


                ExcelFileEntity excelFileEntity = new ExcelFileEntity();

                excelFileEntity.setName(compareResultEntity.getName());
                excelFileEntity.setBuffOriginPrice(compareResultEntity.getBuffOriginalPrice());
                excelFileEntity.setBuffOriginPriceVnd(compareResultEntity.getBuffOriginalPriceVnd());
                excelFileEntity.setBuffPriceVnd(compareResultEntity.getBuffPrice());

                excelFileEntity.setBuffOriginSellPrice(compareResultEntity.getBuffOriginSellPrice());
                excelFileEntity.setBuffSellPriceVnd(compareResultEntity.getBuffSellPriceVnd());
                excelFileEntity.setPercentBuffSell(percentBuffSell);


                if(compareResultEntity.getEmpireOriginalPrice() == null){
                    excelFileEntity.setEmpireOriginPrice(0D);
                    excelFileEntity.setEmpirePriceVnd(0L);
                    excelFileEntity.setPercentEmpire(0L);
                }else{
                    excelFileEntity.setEmpireOriginPrice(compareResultEntity.getEmpireOriginalPrice());
                    excelFileEntity.setEmpirePriceVnd(compareResultEntity.getEmpirePrice());
                    excelFileEntity.setPercentEmpire(percentEmpire);

                }

                excelFileEntityList.add(excelFileEntity);

            }

            return excelFileEntityList;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    private CellStyle createStyleForHeader(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // font size
        font.setColor(IndexedColors.WHITE.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }

    public void writeHeader(XSSFSheet sheet){
        Row headerRow = sheet.createRow(0);
        CellStyle cellStyle = this.createStyleForHeader(sheet);

        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("Name");

        headerCell = headerRow.createCell(1);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("Buff Origin");

        headerCell = headerRow.createCell(2);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("Buff Origin Vnd");

        headerCell = headerRow.createCell(3);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("Buff VND");

        headerCell = headerRow.createCell(4);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("Buff Origin Sell");

        headerCell = headerRow.createCell(5);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("Buff Sell VND");

        headerCell = headerRow.createCell(6);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("Empire Origin");

        headerCell = headerRow.createCell(7);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("Empire VND");

        headerCell = headerRow.createCell(8);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("% different Empire vs Buff");

        headerCell = headerRow.createCell(8);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("% different Buff Sell vs Empire");

    }

    public void writeData(XSSFWorkbook workbook, XSSFSheet sheet){

        int rowCount = 1;

        List<ExcelFileEntity> excelFileEntityList = this.generateExcelList();
        for(ExcelFileEntity excelFileEntity : excelFileEntityList){
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            Cell cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getName());

            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getBuffOriginPrice());

            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getBuffOriginPriceVnd());

            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getBuffPriceVnd());

            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getBuffOriginSellPrice());


            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getBuffSellPriceVnd());

            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getEmpireOriginPrice());

            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getEmpirePriceVnd());

            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getPercentEmpire());

            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getPercentBuffSell());
        }

    }
}
