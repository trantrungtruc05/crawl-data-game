package com.tructran.crawl.repository;

import com.tructran.crawl.model.CompareResultEntity;
import com.tructran.crawl.model.EtopfunPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EtopfunPageRepository extends JpaRepository<EtopfunPage, Long> {

    @Query(nativeQuery = true, value = "select et.name \n" +
            ", et.category, et.original_price  etopOriginalPrice, et.price_by_vnd  etopPrice\n" +
            ", bp.original_price buffOriginalPrice, bp.original_price_by_vnd buffOriginalPriceVnd, bp.price_by_vnd buffPrice\n" +
            ", bp.origin_buff_sell_price buffOriginSellPrice, bp.buff_sell_price_vnd buffSellPriceVnd, ep.original_price empireOriginalPrice, ep.price_by_vnd empirePrice, et.slot slot " +
            "from etopfun_page et left join empire_page ep  on ep.name = et.name\n" +
            "left join buff_page bp  on bp.name  = et.name where et.type = :type")
    List<CompareResultEntity> getResult(@Param("type") String type);

//    @Query(nativeQuery = true, value = "select et.name \n" +
//            ", et.original_price  'etopOriginalPrice', et.price_by_vnd  'etopPrice'\n" +
//            ", bp.original_price 'buffOriginalPrice', bp.original_price_by_vnd 'buffOriginalPriceVnd', bp.price_by_vnd 'buffPrice'\n" +
//            ", bp.origin_buff_sell_price 'buffOriginSellPrice', bp.buff_sell_price_vnd 'buffSellPriceVnd', ep.original_price 'empireOriginalPrice', ep.price_by_vnd 'empirePrice'\n" +
//            "from etopfun_page et left join empire_page ep  on ep.name = et.name\n" +
//            "left join buff_page bp  on bp.name  = et.name where et.category = 'csgo' and et.original_price between :minPrice and :maxPrice")
//    List<CompareResultEntity> getResultWithFilter(@Param("minPrice") int minPrice, @Param("maxPrice") int maxPrice);


    @Query(nativeQuery = true, value = "select et.name \n" +
            ", et.original_price  etopOriginalPrice, et.price_by_vnd  etopPrice\n" +
            ", bp.original_price buffOriginalPrice, bp.original_price_by_vnd buffOriginalPriceVnd, bp.price_by_vnd buffPrice\n" +
            ", bp.origin_buff_sell_price buffOriginSellPrice, bp.buff_sell_price_vnd buffSellPriceVnd, ep.original_price empireOriginalPrice, ep.price_by_vnd empirePrice\n" +
            "from etopfun_page et left join empire_page ep  on ep.name = et.name\n" +
            "left join buff_page bp  on bp.name  = et.name where et.category = 'dota2';")
    List<CompareResultEntity> getResultDota();

    List<EtopfunPage> findByCategory(String category);

    @Query(nativeQuery = true, value = "SELECT * FROM etopfun_page ep WHERE original_price < 30")
    List<EtopfunPage> findEtopSmaller10();

    @Query(nativeQuery = true, value = "select * from etopfun_page ep  where ep.name in :name order by original_price asc")
    List<EtopfunPage> findByNameAsc(@Param("name") List<String> name);

//    @Query(nativeQuery = true, value = "select * from etopfun_page  where  name like '%™%';")
//    List<EtopfunPage> findEtopHaveSpecialCharacter();


    @Modifying
    @Query(nativeQuery = true, value = "update etopfun_page set price_by_vnd = original_price * :rateEtop")
    void updatePricingEtop(@Param("rateEtop") Double rateEtop);

}
