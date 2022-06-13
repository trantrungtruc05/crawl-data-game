package com.tructran.crawl.model;

public class ExcelFileEntity {

    private String name;
    private Double buffOriginPrice;
    private Long buffOriginPriceVnd;
    private Long buffPriceVnd;
    private Double empireOriginPrice;
    private Long empirePriceVnd;
    private Double etopOriginPrice;
    private Long etopPriceVnd;
    private Long percentEmpire;
    private Long percentEtop;
    private Long percentBuff;
    private Double empireOriginPriceEtop;

    private Double buffOriginSellPrice;
    private Long buffSellPriceVnd;
    private Long percentBuffSell;

    private Double empireConfigVnd;
    private Double yuanVnd;
    private Double etopConfigVnd;

    private Double empirePriceCustom;
    private String tempEmpireWithdraw;
    private String slot;
    private String category;

    public  ExcelFileEntity(){}

    public ExcelFileEntity(String name, Double buffOriginPrice, Long buffOriginPriceVnd
            , Long buffPriceVnd, Double empireOriginPrice, Long empirePriceVnd, Double etopOriginPrice
            , Long etopPriceVnd, Long percentEmpire, Long percentEtop, Long percentBuff
            , Double buffOriginSellPrice, Long buffSellPriceVnd, Long percentBuffSell
            , Double empirePriceCustom, String slot, String category) {
        this.name = name;
        this.buffOriginPrice = buffOriginPrice;
        this.buffOriginPriceVnd = buffOriginPriceVnd;
        this.buffPriceVnd = buffPriceVnd;
        this.empireOriginPrice = empireOriginPrice;
        this.empirePriceVnd = empirePriceVnd;
        this.etopOriginPrice = etopOriginPrice;
        this.etopPriceVnd = etopPriceVnd;
        this.percentEmpire = percentEmpire;
        this.percentEtop = percentEtop;
        this.percentBuff = percentBuff;
        this.buffOriginSellPrice = buffOriginSellPrice;
        this.buffSellPriceVnd = buffSellPriceVnd;
        this.percentBuffSell = percentBuffSell;
        this.empirePriceCustom = empirePriceCustom;
        this.slot = slot;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBuffOriginPrice() {
        return buffOriginPrice;
    }

    public void setBuffOriginPrice(Double buffOriginPrice) {
        this.buffOriginPrice = buffOriginPrice;
    }

    public Long getBuffOriginPriceVnd() {
        return buffOriginPriceVnd;
    }

    public void setBuffOriginPriceVnd(Long buffOriginPriceVnd) {
        this.buffOriginPriceVnd = buffOriginPriceVnd;
    }

    public Long getBuffPriceVnd() {
        return buffPriceVnd;
    }

    public void setBuffPriceVnd(Long buffPriceVnd) {
        this.buffPriceVnd = buffPriceVnd;
    }

    public Double getEmpireOriginPrice() {
        return empireOriginPrice;
    }

    public void setEmpireOriginPrice(Double empireOriginPrice) {
        this.empireOriginPrice = empireOriginPrice;
    }

    public Long getEmpirePriceVnd() {
        return empirePriceVnd;
    }

    public void setEmpirePriceVnd(Long empirePriceVnd) {
        this.empirePriceVnd = empirePriceVnd;
    }

    public Double getEtopOriginPrice() {
        return etopOriginPrice;
    }

    public void setEtopOriginPrice(Double etopOriginPrice) {
        this.etopOriginPrice = etopOriginPrice;
    }

    public Long getEtopPriceVnd() {
        return etopPriceVnd;
    }

    public void setEtopPriceVnd(Long etopPriceVnd) {
        this.etopPriceVnd = etopPriceVnd;
    }

    public Long getPercentEmpire() {
        return percentEmpire;
    }

    public void setPercentEmpire(Long percentEmpire) {
        this.percentEmpire = percentEmpire;
    }

    public Long getPercentEtop() {
        return percentEtop;
    }

    public void setPercentEtop(Long percentEtop) {
        this.percentEtop = percentEtop;
    }

    public Long getPercentBuff() {
        return percentBuff;
    }

    public void setPercentBuff(Long percentBuff) {
        this.percentBuff = percentBuff;
    }

    public Double getEmpireConfigVnd() {
        return empireConfigVnd;
    }

    public void setEmpireConfigVnd(Double empireConfigVnd) {
        this.empireConfigVnd = empireConfigVnd;
    }

    public Double getYuanVnd() {
        return yuanVnd;
    }

    public void setYuanVnd(Double yuanVnd) {
        this.yuanVnd = yuanVnd;
    }

    public Double getBuffOriginSellPrice() {
        return buffOriginSellPrice;
    }

    public void setBuffOriginSellPrice(Double buffOriginSellPrice) {
        this.buffOriginSellPrice = buffOriginSellPrice;
    }

    public Long getBuffSellPriceVnd() {
        return buffSellPriceVnd;
    }

    public void setBuffSellPriceVnd(Long buffSellPriceVnd) {
        this.buffSellPriceVnd = buffSellPriceVnd;
    }

    public Long getPercentBuffSell() {
        return percentBuffSell;
    }

    public void setPercentBuffSell(Long percentBuffSell) {
        this.percentBuffSell = percentBuffSell;
    }

    public Double getEtopConfigVnd() {
        return etopConfigVnd;
    }

    public void setEtopConfigVnd(Double etopConfigVnd) {
        this.etopConfigVnd = etopConfigVnd;
    }

    public Double getEmpirePriceCustom() {
        return empirePriceCustom;
    }

    public void setEmpirePriceCustom(Double empirePriceCustom) {
        this.empirePriceCustom = empirePriceCustom;
    }

    public String getTempEmpireWithdraw() {
        return tempEmpireWithdraw;
    }

    public void setTempEmpireWithdraw(String tempEmpireWithdraw) {
        this.tempEmpireWithdraw = tempEmpireWithdraw;
    }

    public Double getEmpireOriginPriceEtop() {
        return empireOriginPriceEtop;
    }

    public void setEmpireOriginPriceEtop(Double empireOriginPriceEtop) {
        this.empireOriginPriceEtop = empireOriginPriceEtop;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
