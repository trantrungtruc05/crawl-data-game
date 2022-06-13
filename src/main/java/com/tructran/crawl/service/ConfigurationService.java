package com.tructran.crawl.service;

import com.tructran.crawl.dto.ConfigurationDTO;
import com.tructran.crawl.model.Configuration;
import com.tructran.crawl.model.CronJobLog;
import com.tructran.crawl.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private CronJobLogRepository cronJobLogRepository;

    @Autowired
    private BuffPageRepository buffPageRepository;

    @Autowired
    private EtopfunPageRepository etopfunPageRepository;

    @Autowired
    private EtopfunPageItemStoreRepository etopfunPageItemStoreRepository;

    @Autowired
    private EmpirePageRepository empirePageRepository;

    @Autowired
    private CrawlService crawlService;

    public ConfigurationDTO getConfig(){
        Configuration buffCookie = configurationRepository.findByKeyAndType("buff", "cookie");

        Configuration empireCrawlCookie = configurationRepository.findByKeyAndType("empire_crawl", "cookie");
        Configuration etopCrawlCookie = configurationRepository.findByKeyAndType("etop_crawl", "cookie");

        Configuration empireWithdrawCookie = configurationRepository.findByKeyAndType("empire_withdraw", "cookie");
        Configuration etopWithdrawCookie = configurationRepository.findByKeyAndType("etop_withdraw", "cookie");
        Configuration etopWithdrawDotaCookie = configurationRepository.findByKeyAndType("etop_withdraw_dota", "cookie");

        Configuration yuanCurrency = configurationRepository.findByKeyAndType("yuan", "currency");
        Configuration etopCurrency = configurationRepository.findByKeyAndType("etop", "currency");
        Configuration customPriceEmpire = configurationRepository.findByKeyAndType("custom", "empire_withdraw");
        Configuration empirePricing = configurationRepository.findByKeyAndType("empire", "currency");

        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setBuffCooikie(buffCookie.getValue());
        configurationDTO.setEmpireCrawlCookie(empireCrawlCookie.getValue());
        configurationDTO.setEtopCrawlCookie(etopCrawlCookie.getValue());
        configurationDTO.setEmpireWithdrawCookie(empireWithdrawCookie.getValue());
        configurationDTO.setEtopWithdrawCookie(etopWithdrawCookie.getValue());
        configurationDTO.setEtopWithdrawDotaCookie(etopWithdrawDotaCookie.getValue());
        configurationDTO.setYuanCurrency(yuanCurrency.getValue());
        configurationDTO.setEtopCurrency(etopCurrency.getValue());
        configurationDTO.setEmpirePricing(empirePricing.getValue());

        Double customPriceEmpireView = 100 - (Double.valueOf(customPriceEmpire.getValue()) * 100);
        configurationDTO.setEmpirePricingCustom(String.valueOf(customPriceEmpireView.intValue()));


        return  configurationDTO;

    }

    @Transactional
    public void updateConfig(ConfigurationDTO configurationDTO){

        Configuration buffCookie = configurationRepository.findByKeyAndType("buff", "cookie");
        Configuration empireCrawlCookie = configurationRepository.findByKeyAndType("empire_crawl", "cookie");
        Configuration etopCrawlCookie = configurationRepository.findByKeyAndType("etop_crawl", "cookie");

        Configuration empireWithdrawCookie = configurationRepository.findByKeyAndType("empire_withdraw", "cookie");
        Configuration etopWithdrawCookie = configurationRepository.findByKeyAndType("etop_withdraw", "cookie");
        Configuration etopWithdrawDotaCookie = configurationRepository.findByKeyAndType("etop_withdraw_dota", "cookie");

        Configuration yuanCurrency = configurationRepository.findByKeyAndType("yuan", "currency");
        Configuration etopCurrency = configurationRepository.findByKeyAndType("etop", "currency");
        Configuration customPriceEmpire = configurationRepository.findByKeyAndType("custom", "empire_withdraw");
        Configuration empirePricing = configurationRepository.findByKeyAndType("empire", "currency");

        buffCookie.setValue(configurationDTO.getBuffCooikie());
        configurationRepository.save(buffCookie);

        empireCrawlCookie.setValue(configurationDTO.getEmpireCrawlCookie());
        configurationRepository.save(empireCrawlCookie);

        etopCrawlCookie.setValue(configurationDTO.getEtopCrawlCookie());
        configurationRepository.save(etopCrawlCookie);

        empireWithdrawCookie.setValue(configurationDTO.getEmpireWithdrawCookie());
        configurationRepository.save(empireWithdrawCookie);

        etopWithdrawCookie.setValue(configurationDTO.getEtopWithdrawCookie());
        configurationRepository.save(etopWithdrawCookie);

        etopWithdrawDotaCookie.setValue(configurationDTO.getEtopWithdrawDotaCookie());
        configurationRepository.save(etopWithdrawDotaCookie);

        yuanCurrency.setValue(configurationDTO.getYuanCurrency());
        configurationRepository.save(yuanCurrency);

        etopCurrency.setValue(configurationDTO.getEtopCurrency());
        configurationRepository.save(etopCurrency);

        empirePricing.setValue(configurationDTO.getEmpirePricing());
        configurationRepository.save(empirePricing);

        // update pricing empire
        empirePageRepository.updatePricingEmpire(Double.valueOf(empirePricing.getValue()));

        //update pricing buff
        buffPageRepository.updatePricingBuff(Double.valueOf(yuanCurrency.getValue()));

        // update pricing etop
        etopfunPageRepository.updatePricingEtop(Double.valueOf(etopCurrency.getValue()));

        // update pricing etop item
        etopfunPageItemStoreRepository.updatePricingEtopItem(Double.valueOf(etopCurrency.getValue()));

        Double empirePricingCustom = (100 - Double.valueOf(configurationDTO.getEmpirePricingCustom())) / 100;
        customPriceEmpire.setValue(String.valueOf(empirePricingCustom));

        configurationRepository.save(customPriceEmpire);


    }

    public String checkJobCrawlRunning(){

        List<CronJobLog> cronJobLogList = cronJobLogRepository.findByStatus("running");
        String s= "";

        if(!cronJobLogList.isEmpty()){
            for(CronJobLog cronJobLog : cronJobLogList){
                s+= cronJobLog.getPage() + " đang chạy ---- ";
            }
        }else{
            s = "Không có job nào chạy";
        }

        return s;
    }
}
