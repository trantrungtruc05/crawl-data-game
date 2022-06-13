package com.tructran.crawl.repository;

import com.tructran.crawl.model.CheckEmpireEntity;
import com.tructran.crawl.model.DuplicateValue;
import com.tructran.crawl.model.EmpirePage;
import com.tructran.crawl.model.WithdrawEmpireEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmpirePageRepository extends JpaRepository<EmpirePage, Long> {

    @Query(nativeQuery = true, value = "SELECT name, COUNT(*) dupValue FROM empire_page ep  GROUP BY name HAVING COUNT(*) > 1")
    List<DuplicateValue> getDupValue();

    @Query(nativeQuery = true, value = "select * from empire_page ep  where ep.name = :name order by original_price_not_percentage asc")
    List<EmpirePage> findByNameAsc(@Param("name") String name);

//    @Query(nativeQuery = true, value = "select * from empire_page  where  name like '%™%' or name like '%Sticker%';")
//    List<EmpirePage> findEmpireInvalidData();

    @Query(nativeQuery = true, value = "select * from empire_page  where name like '%Sticker%';")
    List<EmpirePage> findEmpireInvalidData();

    @Query(nativeQuery = true, value = "select bp.name, bp.original_price  minBuffPrice , ep.original_price minEtopPrice\n" +
            ", (bp.original_price  * :yuanCurrency) / :rateEmpire empireOriginalPriceBuff\n" +
            ", (ep.original_price  * :currencyEtop) / :rateEmpire empireOriginalPriceEtop\n" +
            "from buff_page bp inner join etopfun_page ep on bp.name = ep.name\n" +
            "where bp.category = 'csgo';")
    List<WithdrawEmpireEntity> calcWithdrawEmpire(@Param("yuanCurrency") Double yuanCurrency
            , @Param("currencyEtop") Double currencyEtop
            , @Param("rateEmpire") Double rateEmpire);

    @Modifying
    @Query(nativeQuery = true, value = "update empire_page set price_by_vnd = original_price * :empirePricing")
    void updatePricingEmpire(@Param("empirePricing") Double empirePricing);

    @Query(nativeQuery = true, value = "select ep.name, qw.empire_price_custom empirePriceCustom, ep.original_price_not_percentage originalPriceNotPercentage, ep.item_id itemId from empire_page ep inner join queue_empire_item_withdraw qw on ep.name = qw.name where qw.empire_price_custom >= ep.original_price_not_percentage;")
    List<CheckEmpireEntity> checkEmpireCondition();

}
