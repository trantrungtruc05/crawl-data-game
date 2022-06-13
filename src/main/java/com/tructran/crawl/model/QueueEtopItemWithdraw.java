package com.tructran.crawl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "queue_etop_item_with_draw")
public class QueueEtopItemWithdraw extends BasePage{

    @Column(name = "status")
    private Boolean status;

    @Column(name = "update_at")
    private Date updateAt;

    @Column(name = "response_withdraw")
    private String responseWithdraw;

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

    public String getResponseWithdraw() {
        return responseWithdraw;
    }

    public void setResponseWithdraw(String responseWithdraw) {
        this.responseWithdraw = responseWithdraw;
    }
}
