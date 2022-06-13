package com.tructran.crawl.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "config_currency")
public class ConfigCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "gold_to_litecoin")
    private Double goldToLitecoin;

    @Column(name = "litecoin_to_usd")
    private Double liteCoinToUsd;

    @Column(name = "usd_to_vnd")
    private Double usdToVnd;

    @Column(name = "type")
    private String type;

    @Column(name = "update_at")
    private Date updateAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getGoldToLitecoin() {
        return goldToLitecoin;
    }

    public void setGoldToLitecoin(Double goldToLitecoin) {
        this.goldToLitecoin = goldToLitecoin;
    }

    public Double getLiteCoinToUsd() {
        return liteCoinToUsd;
    }

    public void setLiteCoinToUsd(Double liteCoinToUsd) {
        this.liteCoinToUsd = liteCoinToUsd;
    }

    public Double getUsdToVnd() {
        return usdToVnd;
    }

    public void setUsdToVnd(Double usdToVnd) {
        this.usdToVnd = usdToVnd;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
