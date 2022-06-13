package com.tructran.crawl.service;

import com.tructran.crawl.model.*;
import com.tructran.crawl.repository.ConfigCurrencyRepository;
import com.tructran.crawl.repository.ConfigurationRepository;
import com.tructran.crawl.repository.EmpirePageRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WithdrawEmpireExcelService implements IWriteExcelService {

    public static final int COLUMN_INDEX_NAME         = 0;
    public static final int COLUMN_INDEX_BUFF_ORIGIN_VND = 1;
    public static final int COLUMN_INDEX_EMPIRE_ORIGIN_VND = 2;
    public static final int COLUMN_INDEX_EMPIRE_CUSTOM = 3;
    private static CellStyle cellStyleFormatNumber = null;
    private static CellStyle cellStyleFormatNumberDecimal = null;

    @Autowired
    private EmpirePageRepository empirePageRepository;

    @Value("${export.filePath}")
    private String filePath;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private ConfigCurrencyRepository configCurrencyRepository;


    @Override
    public String exportFileCrawl(String fileName) {
        try{
            writeExcel(this.generateExcelList(), filePath + fileName);
            return "export_success";
        }catch(Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public void writeExcel(List<ExcelFileEntity> excelFileEntityList, String excelFilePath) throws IOException {
        // Create Workbook
        Workbook workbook = BaseWriteExcelService.getWorkbook(excelFilePath);

        // Create sheet
        Sheet sheet = workbook.createSheet("Buff"); // Create sheet with sheet name

        int rowIndex = 0;

        // Write header
        writeHeader(sheet, rowIndex);


        // Write data
        rowIndex++;
        for (ExcelFileEntity excelFileEntity : excelFileEntityList) {
            // Create row
            Row row = sheet.createRow(rowIndex);
            // Write data on row
            writeBook(excelFileEntity, row, null, null);
            rowIndex++;
        }


        // Auto resize column witdth
        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
        BaseWriteExcelService.autosizeColumn(sheet, numberOfColumn);

        BaseWriteExcelService.createOutputFile(workbook, excelFilePath);
        System.out.println("Done!!!");
    }

    @Override
    public void writeBook(ExcelFileEntity excelFileEntity, Row row, CellStyle number, CellStyle decimal) {
        // Format number
        short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
        // DataFormat df = workbook.createDataFormat();
        // short format = df.getFormat("#,##0");

        //Create CellStyle
        Workbook workbook = row.getSheet().getWorkbook();
        cellStyleFormatNumber = workbook.createCellStyle();
        cellStyleFormatNumber.setDataFormat(format);

        cellStyleFormatNumberDecimal = workbook.createCellStyle();
        cellStyleFormatNumberDecimal.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

        CellStyle newStyle = workbook.createCellStyle();
        newStyle.cloneStyleFrom(cellStyleFormatNumberDecimal);

        CellStyle newStyleNumberDecimal = workbook.createCellStyle();
        newStyleNumberDecimal.cloneStyleFrom(cellStyleFormatNumberDecimal);

        Cell cell = row.createCell(COLUMN_INDEX_NAME);
        cell.setCellValue(excelFileEntity.getName());

        cell = row.createCell(COLUMN_INDEX_BUFF_ORIGIN_VND);
        cell.setCellValue(excelFileEntity.getBuffOriginPriceVnd());
        cell.setCellStyle(newStyle);

        cell = row.createCell(COLUMN_INDEX_EMPIRE_ORIGIN_VND);
        cell.setCellValue(excelFileEntity.getEmpireOriginPrice());
        cell.setCellStyle(newStyle);

        cell = row.createCell(COLUMN_INDEX_EMPIRE_CUSTOM);
        cell.setCellValue(excelFileEntity.getEmpirePriceCustom());
        cell.setCellStyle(newStyle);


    }

    @Override
    public void writeHeader(Sheet sheet, int rowIndex) {
        // create CellStyle
        CellStyle cellStyle = BaseWriteExcelService.createStyleForHeader(sheet);

        // Create row
        Row row = sheet.createRow(rowIndex);

        // Create cells
        Cell cell = row.createCell(COLUMN_INDEX_NAME);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Name");

        cell = row.createCell(COLUMN_INDEX_BUFF_ORIGIN_VND);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Giá Buff thấp nhất");

        cell = row.createCell(COLUMN_INDEX_EMPIRE_ORIGIN_VND);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Giá Empire");

        cell = row.createCell(COLUMN_INDEX_EMPIRE_CUSTOM);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Giá Empire tùy chỉnh");

    }

    @Override
    public List<ExcelFileEntity> generateExcelList() {
        try{
            Configuration yuanCurrency = configurationRepository.findByKeyAndType("yuan", "currency");

            Configuration customPriceEmpire = configurationRepository.findByKeyAndType("custom", "empire_withdraw");

            Configuration currencyEtop = configurationRepository.findByKeyAndType("etop", "currency");

            List<ConfigCurrency>  configCurrencyList = configCurrencyRepository.findAll();

            Double empireConfigVnd = configCurrencyList.get(0).getGoldToLitecoin() * configCurrencyList.get(0).getLiteCoinToUsd() * configCurrencyList.get(0).getUsdToVnd();

            List<WithdrawEmpireEntity> withdrawEmpireEntityList = empirePageRepository.calcWithdrawEmpire(Double.valueOf(yuanCurrency.getValue()), Double.valueOf(currencyEtop.getValue()), empireConfigVnd);
            List<ExcelFileEntity> excelFileEntityList = new ArrayList<>();

            for(WithdrawEmpireEntity withdrawEmpireEntity : withdrawEmpireEntityList){
                ExcelFileEntity excelFileEntity = new ExcelFileEntity();

                excelFileEntity.setName(withdrawEmpireEntity.getName());
                excelFileEntity.setBuffOriginPriceVnd(withdrawEmpireEntity.getMinBuffPrice());
                excelFileEntity.setEtopOriginPrice(withdrawEmpireEntity.getMinEtopPrice());
                excelFileEntity.setEmpireOriginPrice(withdrawEmpireEntity.getEmpireOriginalPriceBuff());
                excelFileEntity.setEmpireOriginPriceEtop(withdrawEmpireEntity.getEmpireOriginalPriceEtop());

                Double customEmpirePrice;

                if(withdrawEmpireEntity.getEmpireOriginalPriceEtop() >= withdrawEmpireEntity.getEmpireOriginalPriceBuff()){
                    customEmpirePrice = withdrawEmpireEntity.getEmpireOriginalPriceEtop() * Double.valueOf(customPriceEmpire.getValue());
                }else{
                    customEmpirePrice = withdrawEmpireEntity.getEmpireOriginalPriceBuff() * Double.valueOf(customPriceEmpire.getValue());
                }

                excelFileEntity.setEmpirePriceCustom(customEmpirePrice);
                excelFileEntity.setTempEmpireWithdraw(withdrawEmpireEntity.getName() + "###" + customEmpirePrice);

                excelFileEntityList.add(excelFileEntity);

            }

            return excelFileEntityList;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
