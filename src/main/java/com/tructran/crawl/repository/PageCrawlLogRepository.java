package com.tructran.crawl.repository;

import com.tructran.crawl.model.PageCrawlLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PageCrawlLogRepository extends JpaRepository<PageCrawlLog, Long> {

    List<PageCrawlLog> findByWebPage(String webpage);
}
