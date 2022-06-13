package com.tructran.crawl.repository;

import com.tructran.crawl.model.EtopfunPageItemStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EtopfunPageItemStoreRepository extends JpaRepository<EtopfunPageItemStore, Long> {

    List<EtopfunPageItemStore> findByCategory(String category);

    @Query(nativeQuery = true, value = "SELECT * FROM etopfun_item_page ep WHERE original_price < 10")
    List<EtopfunPageItemStore> findEtopSmaller10();

//    @Query(nativeQuery = true, value = "select * from etopfun_item_page  where  name like '%™%';")
//    List<EtopfunPageItemStore> findEtopHaveSpecialCharacter();

    @Query(nativeQuery = true, value = "select * from etopfun_item_page ep  where ep.name in :name order by original_price desc")
    List<EtopfunPageItemStore> findByNameAsc(@Param("name") List<String> name);

    @Modifying
    @Query(nativeQuery = true, value = "update etopfun_item_page set price_by_vnd = original_price * :rateEtop")
    void updatePricingEtopItem(@Param("rateEtop") Double rateEtop);
}
