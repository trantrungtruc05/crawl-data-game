package com.tructran.crawl.mapper;

public class EmpirePageMapper {

    private String marketName;
    private Double marketValue;

    public EmpirePageMapper(){}

    public EmpirePageMapper(String marketName, Double marketValue) {
        this.marketName = marketName;
        this.marketValue = marketValue;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public Double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(Double marketValue) {
        this.marketValue = marketValue;
    }

    @Override
    public String toString() {
        return "EmpirePageMapper{" +
                "marketName='" + marketName + '\'' +
                ", marketValue=" + marketValue +
                '}';
    }
}
