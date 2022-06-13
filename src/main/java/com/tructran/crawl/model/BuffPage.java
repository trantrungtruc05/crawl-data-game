package com.tructran.crawl.model;

import javax.persistence.*;

@Entity
@Table(name = "buff_page")
public class BuffPage extends BasePage {

    @Column(name = "original_price_by_vnd")
    private Long originalPriceByVnd;


    @Column(name = "category")
    private String category;

    @Column(name = "origin_buff_sell_price")
    private Double originBuffSellPrice;

    @Column(name = "buff_sell_price_vnd")
    private Long buffSellPriceVnd;

    public Long getOriginalPriceByVnd() {
        return originalPriceByVnd;
    }

    public void setOriginalPriceByVnd(Long originalPriceByVnd) {
        this.originalPriceByVnd = originalPriceByVnd;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getOriginBuffSellPrice() {
        return originBuffSellPrice;
    }

    public void setOriginBuffSellPrice(Double originBuffSellPrice) {
        this.originBuffSellPrice = originBuffSellPrice;
    }

    public Long getBuffSellPriceVnd() {
        return buffSellPriceVnd;
    }

    public void setBuffSellPriceVnd(Long buffSellPriceVnd) {
        this.buffSellPriceVnd = buffSellPriceVnd;
    }
}
