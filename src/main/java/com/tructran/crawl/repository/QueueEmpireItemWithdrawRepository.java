package com.tructran.crawl.repository;

import com.tructran.crawl.model.QueueEmpireItemWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QueueEmpireItemWithdrawRepository extends JpaRepository<QueueEmpireItemWithdraw, Long> {

    @Query(nativeQuery = true, value = "select * from queue_empire_item_withdraw where status = false order by empire_price_custom desc")
    List<QueueEmpireItemWithdraw> getItemWaitingWithdraw();

    @Query(nativeQuery = true, value = "select * from queue_empire_item_withdraw where status = true order by empire_price_custom desc")
    List<QueueEmpireItemWithdraw> getItemWithdraw();

    List<QueueEmpireItemWithdraw> findByName(String name);
}
