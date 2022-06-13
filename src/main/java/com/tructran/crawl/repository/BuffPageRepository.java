package com.tructran.crawl.repository;

import com.tructran.crawl.model.BuffPage;
import com.tructran.crawl.model.CompareResultEntity;
import com.tructran.crawl.model.DuplicateValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BuffPageRepository extends JpaRepository<BuffPage, Long> {

    @Query(nativeQuery = true, value = "select bp.name, bp.original_price buffOriginalPrice, bp.original_price_by_vnd  buffOriginalPriceVnd, bp.price_by_vnd buffPrice,\n" +
            "ep.original_price empireOriginalPrice, ep.price_by_vnd empirePrice, bp.origin_buff_sell_price buffOriginSellPrice, bp.buff_sell_price_vnd buffSellPriceVnd\n" +
            "from buff_page bp inner join empire_page ep  on ep.name = bp.name where bp.category = 'csgo'")
    List<CompareResultEntity> getResult();

    @Query(nativeQuery = true, value = "SELECT name, COUNT(*) dupValue FROM buff_page bp  GROUP BY name HAVING COUNT(*) > 1")
    List<DuplicateValue> getDupValue();

    @Query(nativeQuery = true, value = "select * from buff_page bp  where bp.name = :name and bp.category = 'csgo' order by original_price asc")
    List<BuffPage> findByNameAsc(@Param("name") String name);

    @Query(nativeQuery = true, value = "select * from buff_page bp where original_price  = 0;")
    List<BuffPage> findBuffPriceZero();

//    @Query(nativeQuery = true, value = "select * from buff_page bp where  name like '%™%';")
//    List<BuffPage> findBuffHaveSpecialCharacter();

    List<BuffPage> findByCategory(String category);

    @Modifying
    @Query(nativeQuery = true, value = "update buff_page set original_price_by_vnd = original_price * :yuanCurrency, buff_sell_price_vnd = origin_buff_sell_price * :yuanCurrency")
    void updatePricingBuff(@Param("yuanCurrency") Double yuanCurrency);
}