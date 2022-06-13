package com.tructran.crawl.service;

import com.tructran.crawl.model.ConfigCurrency;
import com.tructran.crawl.model.Configuration;
import com.tructran.crawl.model.ExcelFileEntity;
import com.tructran.crawl.model.WithdrawEmpireEntity;
import com.tructran.crawl.repository.ConfigCurrencyRepository;
import com.tructran.crawl.repository.ConfigurationRepository;
import com.tructran.crawl.repository.EmpirePageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CheckEtopBuffWithdrawService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private ConfigCurrencyRepository configCurrencyRepository;

    @Autowired
    private EmpirePageRepository empirePageRepository;

    public List<ExcelFileEntity> generateExcelList(String category) {
        try{
            Configuration yuanCurrency = configurationRepository.findByKeyAndType("yuan", "currency");

            Configuration customPriceEmpire = configurationRepository.findByKeyAndType("custom", "empire_withdraw");

            Configuration currencyEtop = configurationRepository.findByKeyAndType("etop", "currency");

            List<ConfigCurrency>  configCurrencyList = configCurrencyRepository.findAll();

            Double empireConfigVnd = configCurrencyList.get(0).getGoldToLitecoin() * configCurrencyList.get(0).getLiteCoinToUsd() * configCurrencyList.get(0).getUsdToVnd();

            List<WithdrawEmpireEntity> withdrawEmpireEntityList = empirePageRepository.calcWithdrawEmpire(Double.valueOf(yuanCurrency.getValue()), Double.valueOf(currencyEtop.getValue()), empireConfigVnd);
            List<ExcelFileEntity> excelFileEntityList = new ArrayList<>();

            for(WithdrawEmpireEntity withdrawEmpireEntity : withdrawEmpireEntityList){
                ExcelFileEntity excelFileEntity = new ExcelFileEntity();

                excelFileEntity.setName(withdrawEmpireEntity.getName());

                Double customEmpirePrice;

                if(category.equals("etop")){
                    excelFileEntity.setBuffOriginPriceVnd(0L);
                    excelFileEntity.setEtopOriginPrice(withdrawEmpireEntity.getMinEtopPrice());
                    excelFileEntity.setEmpireOriginPrice(0D);
                    excelFileEntity.setEmpireOriginPriceEtop(withdrawEmpireEntity.getEmpireOriginalPriceEtop());
                    customEmpirePrice = withdrawEmpireEntity.getEmpireOriginalPriceEtop() * Double.valueOf(customPriceEmpire.getValue());
                }else{
                    excelFileEntity.setBuffOriginPriceVnd(withdrawEmpireEntity.getMinBuffPrice());
                    excelFileEntity.setEtopOriginPrice(0D);
                    excelFileEntity.setEmpireOriginPrice(withdrawEmpireEntity.getEmpireOriginalPriceBuff());
                    excelFileEntity.setEmpireOriginPriceEtop(0D);

                    customEmpirePrice = withdrawEmpireEntity.getEmpireOriginalPriceBuff() * Double.valueOf(customPriceEmpire.getValue());
                }

                excelFileEntity.setEmpirePriceCustom(customEmpirePrice);
                excelFileEntity.setTempEmpireWithdraw(withdrawEmpireEntity.getName() + "###" + customEmpirePrice);

                excelFileEntityList.add(excelFileEntity);

            }

            return excelFileEntityList;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
