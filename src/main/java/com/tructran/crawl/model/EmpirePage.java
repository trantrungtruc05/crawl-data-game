package com.tructran.crawl.model;

import javax.persistence.*;

@Entity
@Table(name = "empire_page")
public class EmpirePage extends BasePage {

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "original_price_not_percentage")
    private Double originalPriceNotPercentage;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Double getOriginalPriceNotPercentage() {
        return originalPriceNotPercentage;
    }

    public void setOriginalPriceNotPercentage(Double originalPriceNotPercentage) {
        this.originalPriceNotPercentage = originalPriceNotPercentage;
    }
}
