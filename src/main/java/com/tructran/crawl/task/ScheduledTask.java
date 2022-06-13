package com.tructran.crawl.task;

import com.tructran.crawl.service.CrawlService;
import com.tructran.crawl.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ScheduledTask {

    @Autowired
    private CrawlService crawlService;

    @Scheduled(fixedDelay = 60 * 60 * 1000) // 1 hour
    public void scheduleTaskWithFixedDelayForCrawlEmpire() {
        System.out.println("-zzzz-");
        crawlService.empirePageCrawl();
    }

}
