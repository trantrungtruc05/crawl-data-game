package com.tructran.crawl.dto;

public class ConfigurationDTO {

    private String buffCooikie;

    private String empireCrawlCookie;

    private String etopCrawlCookie;

    private String empireWithdrawCookie;

    private String etopWithdrawCookie;

    private String etopWithdrawDotaCookie;

    private String etopCurrency;

    private String yuanCurrency;

    private String empirePricingCustom;

    private String empirePricing;

    public ConfigurationDTO(){}

    public ConfigurationDTO(String buffCooikie, String empireCrawlCookie, String etopCrawlCookie, String empireWithdrawCookie, String etopWithdrawCookie, String etopCurrency, String yuanCurrency, String empirePricingCustom, String empirePricing, String etopWithdrawDotaCookie) {
        this.buffCooikie = buffCooikie;
        this.etopCurrency = etopCurrency;
        this.yuanCurrency = yuanCurrency;
        this.empirePricingCustom = empirePricingCustom;
        this.empirePricing = empirePricing;
        this.empireCrawlCookie = empireCrawlCookie;
        this.empireWithdrawCookie = empireWithdrawCookie;
        this.etopCrawlCookie = etopCrawlCookie;
        this.etopWithdrawCookie = etopWithdrawCookie;
        this.etopWithdrawDotaCookie = etopWithdrawDotaCookie;
    }

    public String getBuffCooikie() {
        return buffCooikie;
    }

    public void setBuffCooikie(String buffCooikie) {
        this.buffCooikie = buffCooikie;
    }

    public String getEtopCurrency() {
        return etopCurrency;
    }

    public void setEtopCurrency(String etopCurrency) {
        this.etopCurrency = etopCurrency;
    }

    public String getYuanCurrency() {
        return yuanCurrency;
    }

    public void setYuanCurrency(String yuanCurrency) {
        this.yuanCurrency = yuanCurrency;
    }

    public String getEmpirePricingCustom() {
        return empirePricingCustom;
    }

    public void setEmpirePricingCustom(String empirePricingCustom) {
        this.empirePricingCustom = empirePricingCustom;
    }

    public String getEmpirePricing() {
        return empirePricing;
    }

    public void setEmpirePricing(String empirePricing) {
        this.empirePricing = empirePricing;
    }

    public String getEmpireCrawlCookie() {
        return empireCrawlCookie;
    }

    public void setEmpireCrawlCookie(String empireCrawlCookie) {
        this.empireCrawlCookie = empireCrawlCookie;
    }

    public String getEtopCrawlCookie() {
        return etopCrawlCookie;
    }

    public void setEtopCrawlCookie(String etopCrawlCookie) {
        this.etopCrawlCookie = etopCrawlCookie;
    }

    public String getEmpireWithdrawCookie() {
        return empireWithdrawCookie;
    }

    public void setEmpireWithdrawCookie(String empireWithdrawCookie) {
        this.empireWithdrawCookie = empireWithdrawCookie;
    }

    public String getEtopWithdrawCookie() {
        return etopWithdrawCookie;
    }

    public void setEtopWithdrawCookie(String etopWithdrawCookie) {
        this.etopWithdrawCookie = etopWithdrawCookie;
    }

    public String getEtopWithdrawDotaCookie() {
        return etopWithdrawDotaCookie;
    }

    public void setEtopWithdrawDotaCookie(String etopWithdrawDotaCookie) {
        this.etopWithdrawDotaCookie = etopWithdrawDotaCookie;
    }
}
