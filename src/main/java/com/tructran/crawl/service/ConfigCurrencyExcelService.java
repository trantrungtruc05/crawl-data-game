package com.tructran.crawl.service;

import com.tructran.crawl.model.ConfigCurrency;
import com.tructran.crawl.model.Configuration;
import com.tructran.crawl.model.ExcelFileEntity;
import com.tructran.crawl.repository.ConfigCurrencyRepository;
import com.tructran.crawl.repository.ConfigurationRepository;
import org.apache.poi.openxml4j.util.ZipSecureFile;
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
public class ConfigCurrencyExcelService{


    @Autowired
    private ConfigCurrencyRepository configCurrencyRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Value("${export.filePath}")
    private String filePath;


    public List<ExcelFileEntity> generateExcelList() {
        try{
            List<ConfigCurrency>  configCurrencyList = configCurrencyRepository.findAll();
            List<ExcelFileEntity> excelFileEntityList = new ArrayList<>();

            Configuration yuanCurrency = configurationRepository.findByKeyAndType("yuan", "currency");
            Configuration etopCurrency = configurationRepository.findByKeyAndType("etop", "currency");

            for(ConfigCurrency configCurrency : configCurrencyList){

                ExcelFileEntity excelFileEntity = new ExcelFileEntity();

                Double empireConfigVnd = configCurrency.getGoldToLitecoin() * configCurrency.getLiteCoinToUsd() * configCurrency.getUsdToVnd();
                excelFileEntity.setEmpireConfigVnd(empireConfigVnd);
                excelFileEntity.setYuanVnd(Double.valueOf(yuanCurrency.getValue()));
                excelFileEntity.setEtopConfigVnd(Double.valueOf(etopCurrency.getValue()));

                excelFileEntityList.add(excelFileEntity);

            }

            return excelFileEntityList;
        }catch(Exception e){
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
        headerCell.setCellValue("Empire Config VND");

        headerCell = headerRow.createCell(1);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("Yuan To Vnd");

        headerCell = headerRow.createCell(2);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("Etop Config VND");
    }

    public void writeData(XSSFWorkbook workbook, XSSFSheet sheet){

        int rowCount = 1;

        List<ExcelFileEntity> excelFileEntityList = this.generateExcelList();
        for(ExcelFileEntity excelFileEntity : excelFileEntityList){
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            Cell cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getEmpireConfigVnd());

            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getYuanVnd());

            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getEtopConfigVnd());
        }

    }


}
