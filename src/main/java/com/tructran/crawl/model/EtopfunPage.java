package com.tructran.crawl.model;

import javax.persistence.*;

@Entity
@Table(name = "etopfun_page")
public class EtopfunPage extends BasePage {

    @Column(name = "category")
    private String category;

    @Column(name = "id_item")
    private Integer idItem;

    @Column(name = "type")
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
