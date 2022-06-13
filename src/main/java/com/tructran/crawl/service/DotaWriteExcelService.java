package com.tructran.crawl.service;

import com.tructran.crawl.model.CompareResultEntity;
import com.tructran.crawl.model.ExcelFileEntity;
import com.tructran.crawl.repository.EtopfunPageRepository;
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
import java.util.stream.Collectors;

@Service
public class DotaWriteExcelService {

    @Autowired
    private EtopfunPageRepository etopfunPageRepository;

    @Value("${export.filePath}")
    private String filePath;

    public List<ExcelFileEntity> generateExcelList() {
        try{
            List<CompareResultEntity>  compareResultEntityList = etopfunPageRepository.getResultDota();
            List<ExcelFileEntity> excelFileEntityList = new ArrayList<>();

            for(CompareResultEntity compareResultEntity : compareResultEntityList){

                Long percentEmpire;
                Long percentBuff;
                Long percentBuffSell;

                if(compareResultEntity.getEmpirePrice() != null){
                    Long diffEmpire = compareResultEntity.getEtopPrice() - compareResultEntity.getEmpirePrice();
                    Double rateEmpire = diffEmpire / Double.valueOf(compareResultEntity.getEtopPrice());
                    percentEmpire = Math.round(rateEmpire * 100);

                }else{
                    percentEmpire = 0L;
                }

                if(compareResultEntity.getBuffPrice() != null){
                    Long diffBuff = compareResultEntity.getEtopPrice() - compareResultEntity.getBuffPrice();
                    Double rateBuff = diffBuff / Double.valueOf(compareResultEntity.getEtopPrice());
                    percentBuff = Math.round(rateBuff * 100);
                }else{
                    percentBuff = 0L;
                }

                if(compareResultEntity.getBuffSellPriceVnd() != null){
                    Long diffBuff = compareResultEntity.getEtopPrice() - compareResultEntity.getBuffSellPriceVnd();
                    Double rateBuff = diffBuff / Double.valueOf(compareResultEntity.getEtopPrice());
                    percentBuffSell = Math.round(rateBuff * 100);
                }else{
                    percentBuffSell = 0L;
                }

                ExcelFileEntity excelFileEntity = new ExcelFileEntity();

                excelFileEntity.setName(compareResultEntity.getName());
                excelFileEntity.setEtopOriginPrice(compareResultEntity.getEtopOriginalPrice());
                excelFileEntity.setEtopPriceVnd(compareResultEntity.getEtopPrice());

                if(compareResultEntity.getEmpireOriginalPrice() == null){
                    excelFileEntity.setEmpireOriginPrice(0D);
                    excelFileEntity.setEmpirePriceVnd(0L);
                    excelFileEntity.setPercentEmpire(0L);
                }else{
                    excelFileEntity.setEmpireOriginPrice(compareResultEntity.getEmpireOriginalPrice());
                    excelFileEntity.setEmpirePriceVnd(compareResultEntity.getEmpirePrice());
                    excelFileEntity.setPercentEmpire(percentEmpire);

                }

                if(compareResultEntity.getBuffOriginalPrice() == null){
                    excelFileEntity.setBuffOriginPrice(0D);
                    excelFileEntity.setBuffOriginPriceVnd(0L);
                    excelFileEntity.setBuffPriceVnd(0L);

                    excelFileEntity.setPercentBuff(0L);

                    excelFileEntity.setBuffOriginSellPrice(0D);
                    excelFileEntity.setBuffSellPriceVnd(0L);
                    excelFileEntity.setPercentBuffSell(0L);
                }else{
                    excelFileEntity.setBuffOriginPrice(compareResultEntity.getBuffOriginalPrice());
                    excelFileEntity.setBuffOriginPriceVnd(compareResultEntity.getBuffOriginalPriceVnd());
                    excelFileEntity.setBuffPriceVnd(compareResultEntity.getBuffPrice());
                    excelFileEntity.setPercentBuff(percentBuff);

                    excelFileEntity.setBuffOriginSellPrice(compareResultEntity.getBuffOriginSellPrice());
                    excelFileEntity.setBuffSellPriceVnd(compareResultEntity.getBuffSellPriceVnd());
                    excelFileEntity.setPercentBuffSell(percentBuffSell);
                }
                excelFileEntityList.add(excelFileEntity);

            }


            return excelFileEntityList;
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<ExcelFileEntity> generateExcelListFilter(int min, int max) {
        try{
            List<CompareResultEntity>  compareResultEntityList = etopfunPageRepository.getResultDota();
            List<ExcelFileEntity> excelFileEntityList = new ArrayList<>();

            for(CompareResultEntity compareResultEntity : compareResultEntityList){

                Long percentEmpire;
                Long percentBuff;
                Long percentBuffSell;

                if(compareResultEntity.getEmpirePrice() != null){
                    Long diffEmpire = compareResultEntity.getEtopPrice() - compareResultEntity.getEmpirePrice();
                    Double rateEmpire = diffEmpire / Double.valueOf(compareResultEntity.getEtopPrice());
                    percentEmpire = Math.round(rateEmpire * 100);

                }else{
                    percentEmpire = 0L;
                }

                if(compareResultEntity.getBuffPrice() != null){
                    Long diffBuff = compareResultEntity.getEtopPrice() - compareResultEntity.getBuffPrice();
                    Double rateBuff = diffBuff / Double.valueOf(compareResultEntity.getEtopPrice());
                    percentBuff = Math.round(rateBuff * 100);
                }else{
                    percentBuff = 0L;
                }

                if(compareResultEntity.getBuffSellPriceVnd() != null){
                    Long diffBuff = compareResultEntity.getEtopPrice() - compareResultEntity.getBuffSellPriceVnd();
                    Double rateBuff = diffBuff / Double.valueOf(compareResultEntity.getEtopPrice());
                    percentBuffSell = Math.round(rateBuff * 100);
                }else{
                    percentBuffSell = 0L;
                }

                ExcelFileEntity excelFileEntity = new ExcelFileEntity();

                excelFileEntity.setName(compareResultEntity.getName());
                excelFileEntity.setEtopOriginPrice(compareResultEntity.getEtopOriginalPrice());
                excelFileEntity.setEtopPriceVnd(compareResultEntity.getEtopPrice());

                if(compareResultEntity.getEmpireOriginalPrice() == null){
                    excelFileEntity.setEmpireOriginPrice(0D);
                    excelFileEntity.setEmpirePriceVnd(0L);
                    excelFileEntity.setPercentEmpire(0L);
                }else{
                    excelFileEntity.setEmpireOriginPrice(compareResultEntity.getEmpireOriginalPrice());
                    excelFileEntity.setEmpirePriceVnd(compareResultEntity.getEmpirePrice());
                    excelFileEntity.setPercentEmpire(percentEmpire);

                }

                if(compareResultEntity.getBuffOriginalPrice() == null){
                    excelFileEntity.setBuffOriginPrice(0D);
                    excelFileEntity.setBuffOriginPriceVnd(0L);
                    excelFileEntity.setBuffPriceVnd(0L);

                    excelFileEntity.setPercentBuff(0L);

                    excelFileEntity.setBuffOriginSellPrice(0D);
                    excelFileEntity.setBuffSellPriceVnd(0L);
                    excelFileEntity.setPercentBuffSell(0L);
                }else{
                    excelFileEntity.setBuffOriginPrice(compareResultEntity.getBuffOriginalPrice());
                    excelFileEntity.setBuffOriginPriceVnd(compareResultEntity.getBuffOriginalPriceVnd());
                    excelFileEntity.setBuffPriceVnd(compareResultEntity.getBuffPrice());
                    excelFileEntity.setPercentBuff(percentBuff);

                    excelFileEntity.setBuffOriginSellPrice(compareResultEntity.getBuffOriginSellPrice());
                    excelFileEntity.setBuffSellPriceVnd(compareResultEntity.getBuffSellPriceVnd());
                    excelFileEntity.setPercentBuffSell(percentBuffSell);
                }
                excelFileEntityList.add(excelFileEntity);

            }

            List<ExcelFileEntity> result = excelFileEntityList.stream().filter(excelFileEntity -> excelFileEntity.getPercentBuff() >= min && excelFileEntity.getPercentBuff() <= max).collect(Collectors.toList());

            return result;
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
        headerCell.setCellValue("Name");

        headerCell = headerRow.createCell(1);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("Etop Origin");

        headerCell = headerRow.createCell(2);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("Etop VND");

        headerCell = headerRow.createCell(3);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("Buff Origin");

        headerCell = headerRow.createCell(4);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("Buff Origin VND");

        headerCell = headerRow.createCell(5);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("Buff VND");

        headerCell = headerRow.createCell(6);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("% different Buff vs Etop");

        headerCell = headerRow.createCell(7);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("Buff Origin Sell");

        headerCell = headerRow.createCell(8);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("Buff Sell VND");

        headerCell = headerRow.createCell(9);
        headerCell.setCellStyle(cellStyle);
        headerCell.setCellValue("% different Buff Sell vs Etop");
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
            cell.setCellValue(excelFileEntity.getEtopOriginPrice());

            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getEtopPriceVnd());

            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getBuffOriginPrice());

            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getBuffOriginPriceVnd());


            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getBuffPriceVnd());

            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getPercentBuff());

            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getBuffOriginSellPrice());

            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getBuffSellPriceVnd());

            cell = row.createCell(columnCount++);
            cell.setCellValue(excelFileEntity.getPercentBuffSell());
        }

    }




}
