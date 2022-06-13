package com.tructran.crawl.repository;

import com.tructran.crawl.model.CronJobLog;
import com.tructran.crawl.model.QueueEtopItemWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CronJobLogRepository extends JpaRepository<CronJobLog, Long> {

    List<CronJobLog> findByPageAndType(String page, String type);
    List<CronJobLog> findByType(String type);
    List<CronJobLog> findByStatus(String status);

    @Query(nativeQuery = true, value = "select count(*) from cron_job_log where page = :page and type = 'job' and status = 'running';")
    Integer checkJobRunning(@Param("page") String page);


}
