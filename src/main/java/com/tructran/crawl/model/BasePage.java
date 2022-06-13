package com.tructran.crawl.model;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public class BasePage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "original_price")
    private Double originalPrice;

    @Column(name = "price_by_vnd")
    private Long priceByVnd;

    @Column(name = "create_at")
    private Date createAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Long getPriceByVnd() {
        return priceByVnd;
    }

    public void setPriceByVnd(Long priceByVnd) {
        this.priceByVnd = priceByVnd;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "BasePage{" +
                "name='" + name + '\'' +
                ", originalPrice=" + originalPrice +
                '}';
    }
}
