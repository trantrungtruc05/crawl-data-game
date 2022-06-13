package com.tructran.crawl.controller;

import com.tructran.crawl.dto.ConfigurationDTO;
import com.tructran.crawl.service.*;
import com.tructran.crawl.util.MediaTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;

@RestController
@RequestMapping("/api")
public class CrawlController {

    @Autowired
    private CrawlService crawlService;

    @Autowired
    private BuffExcelService buffExcelService;

    @Autowired
    private EtopExcelService etopExcelService;

    @Autowired
    private ConfigCurrencyExcelService configCurrencyExcelService;

    @Autowired
    private DotaWriteExcelService dotaWriteExcelService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private WithdrawEmpireExcelService withdrawEmpireExcelService;

    @Value("${export.filePath}")
    private String filePath;

    @Autowired
    private ExcelService excelService;

    @GetMapping("/crawlEmpire")
    public ResponseEntity<String> crawlEmpire()  {

        crawlService.empirePageCrawl();
        return new ResponseEntity<String>("crawl_empire_sucess", HttpStatus.OK);

    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> download()  {
        try {

//            if(crawlService.checkStatusPersist().equals("persist_true")){
//                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
//            }

            Long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
            excelService.export("/crawl_" + timestamp + ".xlsx");

            MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, "/crawl_" + timestamp + ".xlsx");
            File file = new File(filePath + "/crawl_" + timestamp + ".xlsx");
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    // Content-Disposition
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                    // Content-Type
                    .contentType(mediaType)
                    // Contet-Length
                    .contentLength(file.length()) //
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @RequestMapping("/checkCrawl")
    public ResponseEntity<String> checkCrawl() {
        String checkCrawl = configurationService.checkJobCrawlRunning();
        return new ResponseEntity<>(checkCrawl, HttpStatus.OK);
    }




    @GetMapping("/downloadWithdrawEmpireExcel")
    public ResponseEntity<InputStreamResource> downloadWithdrawEmpireExcel()  {

        try {

            if(crawlService.checkStatusPersist().equals("persist_true")){
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            Long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
            withdrawEmpireExcelService.exportFileCrawl("/withdraw_empire_" + timestamp + ".xlsx");

            MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, "/withdraw_empire_" + timestamp + ".xlsx");
            File file = new File(filePath + "/withdraw_empire_" + timestamp + ".xlsx");
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    // Content-Disposition
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                    // Content-Type
                    .contentType(mediaType)
                    // Contet-Length
                    .contentLength(file.length()) //
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
