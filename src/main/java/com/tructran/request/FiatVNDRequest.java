package com.tructran.request;

public class FiatVNDRequest {

    private int page;
    private int rows;
    private String asset;
    private String tradeType;
    private String fiat;
    private String publisherType;
    private boolean merchantCheck;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getFiat() {
        return fiat;
    }

    public void setFiat(String fiat) {
        this.fiat = fiat;
    }

    public String getPublisherType() {
        return publisherType;
    }

    public void setPublisherType(String publisherType) {
        this.publisherType = publisherType;
    }

    public boolean isMerchantCheck() {
        return merchantCheck;
    }

    public void setMerchantCheck(boolean merchantCheck) {
        this.merchantCheck = merchantCheck;
    }
}
