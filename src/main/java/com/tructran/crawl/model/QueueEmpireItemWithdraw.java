package com.tructran.crawl.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "queue_empire_item_withdraw")
public class QueueEmpireItemWithdraw {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "empire_price_custom")
    private Double empirePriceCustom;

    @Column(name = "response_withdraw")
    private String responseWithdraw;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "update_at")
    private Date updateAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getEmpirePriceCustom() {
        return empirePriceCustom;
    }

    public void setEmpirePriceCustom(Double empirePriceCustom) {
        this.empirePriceCustom = empirePriceCustom;
    }

    public String getResponseWithdraw() {
        return responseWithdraw;
    }

    public void setResponseWithdraw(String responseWithdraw) {
        this.responseWithdraw = responseWithdraw;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
