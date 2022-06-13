package com.tructran.crawl.repository;

import com.tructran.crawl.model.QueueEtopItemWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QueueEtopItemWithdrawRepository extends JpaRepository<QueueEtopItemWithdraw, Long> {

    @Query(nativeQuery = true, value = "select * from queue_etop_item_with_draw qd inner join etopfun_item_page eip on qd.name = eip.name where qd.status = false order by qd.price_by_vnd desc")
    List<QueueEtopItemWithdraw> getAvailableItemWithdraw();

    @Query(nativeQuery = true, value = "select * from queue_etop_item_with_draw where status = true order by original_price desc")
    List<QueueEtopItemWithdraw> getItemWithdraw();

    @Query(nativeQuery = true, value = "select * from queue_etop_item_with_draw where status = false order by original_price desc")
    List<QueueEtopItemWithdraw> getItemWaitingWithdraw();

    List<QueueEtopItemWithdraw> findByName(String name);
}
