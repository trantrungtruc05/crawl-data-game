package com.tructran.crawl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "etopfun_item_page")
public class EtopfunPageItemStore extends BasePage{

    @Column(name = "category")
    private String category;

    @Column(name = "id_item")
    private Integer idItem;

    @Column(name = "quantity")
    private Integer quantity;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getIdItem() {
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
