package com.tructran.crawl.controller;

import com.tructran.crawl.dto.InfoDTO;
import com.tructran.crawl.model.ExcelFileEntity;
import com.tructran.crawl.service.InfoDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "pages")
public class InfoController {

    @Autowired
    private InfoDataService infoDataService;

    @RequestMapping("/info")
    public String initPageCsgo(ModelMap map) {

        InfoDTO infoDTO = infoDataService.crawlingStatus();
        map.addAttribute("info", infoDTO);

        return "pages/info";
    }
}
