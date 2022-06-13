package com.tructran.crawl.repository;

import com.tructran.crawl.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    Configuration findByKeyAndType(String key, String type);

    @Query(nativeQuery = true, value = "select count(*) from cron_job_log where status = 'running' and type = 'persist' and page = 'empire';")
    Integer checkEmpirePersist();

    @Query(nativeQuery = true, value = "select * from configuration where type = :type order by key;")
    List<Configuration> findTypeOrderKey(@Param("type") String type);

    @Modifying
    @Query(nativeQuery = true, value = "update configuration set value = :etopIndex where type = 'etop_index'")
    void updateEtopIndex(@Param("etopIndex") Integer etopIndex);
}
