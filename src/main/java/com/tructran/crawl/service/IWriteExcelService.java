package com.tructran.crawl.service;

import com.tructran.crawl.model.ExcelFileEntity;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.util.List;

public interface IWriteExcelService {

     String exportFileCrawl(String fileName);

     void writeExcel(List<ExcelFileEntity> excelFileEntityList, String excelFilePath) throws IOException;

     void writeBook(ExcelFileEntity excelFileEntity, Row row, CellStyle number, CellStyle decimal);

     void writeHeader(Sheet sheet, int rowIndex);

     List<ExcelFileEntity> generateExcelList();





}
