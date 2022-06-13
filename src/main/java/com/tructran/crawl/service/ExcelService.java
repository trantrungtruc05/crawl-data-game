package com.tructran.crawl.service;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ExcelService {

    @Autowired
    private EtopExcelService etopExcelService;

    @Autowired
    private ConfigCurrencyExcelService configCurrencyExcelService;

    @Autowired
    private BuffExcelService buffExcelService;

    @Autowired
    private DotaWriteExcelService dotaWriteExcelService;

    @Value("${export.filePath}")
    private String filePath;

    public String export(String filename) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Currency");

        configCurrencyExcelService.writeHeader(sheet);
        configCurrencyExcelService.writeData(workbook, sheet);

        System.out.println("Export Currency Excel Success");

        sheet = workbook.createSheet("Buff");

        buffExcelService.writeHeader(sheet);
        buffExcelService.writeData(workbook, sheet);

        System.out.println("Export Buff Excel Success");

        sheet = workbook.createSheet("Etop");

        etopExcelService.writeHeader(sheet);
        etopExcelService.writeData(workbook, sheet, "page");

        sheet = workbook.createSheet("Etop Order");

        etopExcelService.writeHeader(sheet);
        etopExcelService.writeData(workbook, sheet, "order");

        System.out.println("Export Etop Excel Success");

        sheet = workbook.createSheet("Dota2");
        dotaWriteExcelService.writeHeader(sheet);
        dotaWriteExcelService.writeData(workbook, sheet);

        System.out.println("Export Dota Excel Success");

        FileOutputStream outputStream = new FileOutputStream(filePath + filename);
        workbook.write(outputStream);
        workbook.close();

        return "export_success";
    }
}
