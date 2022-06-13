package com.tructran.crawl.service;

import com.tructran.crawl.dto.InfoDTO;
import com.tructran.crawl.model.CronJobLog;
import com.tructran.crawl.repository.CronJobLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoDataService {

    @Autowired
    private CronJobLogRepository cronJobLogRepository;

    public InfoDTO crawlingStatus(){

        CronJobLog buffCrawl = cronJobLogRepository.findByPageAndType("buff", "job").get(0);
        CronJobLog empireCrawl = cronJobLogRepository.findByPageAndType("empire", "job").get(0);
        CronJobLog etopCrawl = cronJobLogRepository.findByPageAndType("etop", "job").get(0);
        CronJobLog etopWithdraw = cronJobLogRepository.findByPageAndType("etop", "withdraw").get(0);
        CronJobLog empireWithdraw = cronJobLogRepository.findByPageAndType("empire", "withdraw").get(0);

        InfoDTO infoDTO = new InfoDTO();
        infoDTO.setBuffCrawlStatus(buffCrawl.getStatus().toUpperCase());
        infoDTO.setEmpireCrawlStatus(empireCrawl.getStatus().toUpperCase());
        infoDTO.setEtopCrawlStatus(etopCrawl.getStatus().toUpperCase());
        infoDTO.setEtopWithdrawStatus(etopWithdraw.getStatus().toUpperCase());
        infoDTO.setEmpireWithdrawStatus(empireWithdraw.getStatus().toUpperCase());

        return infoDTO;
    }
}
