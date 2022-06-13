package com.tructran.crawl.repository;

import com.tructran.crawl.model.ConfigCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConfigCurrencyRepository extends JpaRepository<ConfigCurrency, Long> {

    @Query(
            nativeQuery = true,
            value = "UPDATE config_currency set yuan_to_vnd = :result"
    )
    void updateChineseMoney(@Param("result") Double result);

    List<ConfigCurrency> findByType(String type);

}
