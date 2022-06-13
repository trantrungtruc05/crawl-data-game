package com.tructran.crawl.service;

import com.tructran.crawl.model.*;
import com.tructran.crawl.repository.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WithdrawService {

    @Autowired
    private EtopfunPageItemStoreRepository etopfunPageItemStoreRepository;

    @Autowired
    private EtopfunPageRepository etopfunPageRepository;

    @Autowired
    private QueueEtopItemWithdrawRepository queueEtopItemWithdrawRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private CronJobLogRepository cronJobLogRepository;

    public String addQueueItemWithDraw(List<String> name) {
        List<EtopfunPage> etopfunPageList  = etopfunPageRepository.findByNameAsc(name);

        List<QueueEtopItemWithdraw> queueEtopItemWithdrawList = new ArrayList<>();

        for(EtopfunPage etopfunPage : etopfunPageList){
            QueueEtopItemWithdraw queueEtopItemWithdraw = new QueueEtopItemWithdraw();
            queueEtopItemWithdraw.setCreateAt(new Date());
            queueEtopItemWithdraw.setName(etopfunPage.getName());
            queueEtopItemWithdraw.setOriginalPrice(etopfunPage.getOriginalPrice());
            queueEtopItemWithdraw.setPriceByVnd(etopfunPage.getPriceByVnd());
            queueEtopItemWithdraw.setStatus(false);

            queueEtopItemWithdrawList.add(queueEtopItemWithdraw);
        }

        queueEtopItemWithdrawRepository.saveAll(queueEtopItemWithdrawList);

        this.autoWithDraw();

        return "success";
    }

    public String autoWithDraw(){
        List<QueueEtopItemWithdraw> etopItemNameAvailableWithdraw = queueEtopItemWithdrawRepository.getAvailableItemWithdraw();

        List<String> name = new ArrayList<>();
        for(QueueEtopItemWithdraw queueEtopItemWithdraw : etopItemNameAvailableWithdraw){
            name.add(queueEtopItemWithdraw.getName());
        }

        if(etopItemNameAvailableWithdraw.isEmpty()){
            System.out.println("No item to withdraw");
            return "no_action";
        }else{
            System.out.println(" Connecting Withdraw");
            this.updateStatusWithdraw("running");
            this.withdrawItem(name);
            System.out.println(" Withdraw Success");
            this.updateStatusWithdraw("idle");
            return "auto_withdraw_success";
        }

    }

    private String withdrawItem(List<String> name){

        try{
            Configuration etopCookie = configurationRepository.findByKeyAndType("etop_withdraw", "cookie");

            List<EtopfunPageItemStore> etopfunPageItemStoreList =  etopfunPageItemStoreRepository.findByNameAsc(name);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Cookie", etopCookie.getValue());

            HttpEntity<String> entity = new HttpEntity<>(headers);

            for(EtopfunPageItemStore etopfunPageItemStore : etopfunPageItemStoreList){
                MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
                map.add("id", etopfunPageItemStore.getIdItem().toString());
                map.add("num", etopfunPageItemStore.getQuantity().toString());
                map.add("vip", "false");
                map.add("lang", "en");

                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

                // withdraw
                ResponseEntity<String> response = restTemplate.postForEntity( "https://www.etopfun.com/api/ingotitems/realitemback/exchange.do"
                        , request , String.class );

                System.out.println(response.getBody());

                JSONObject statusWithdraw = new JSONObject(response.getBody());

                if(statusWithdraw.getString("type").equals("success") && statusWithdraw.getInt("statusCode") == 200){
                    QueueEtopItemWithdraw queueEtopItemWithdraw  = queueEtopItemWithdrawRepository.findByName(etopfunPageItemStore.getName()).get(0);
                    queueEtopItemWithdraw.setStatus(true);
                    queueEtopItemWithdraw.setUpdateAt(new Date());
                    queueEtopItemWithdraw.setResponseWithdraw(response.getBody());

                    queueEtopItemWithdrawRepository.save(queueEtopItemWithdraw);

                    System.out.println("Rút thành công item tên : " + etopfunPageItemStore.getName());
                }


            }

            return "withdraw_success";
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }


    }

    public List<QueueEtopItemWithdraw> listItemWaitingWithdraw(){
        List<QueueEtopItemWithdraw> queueEtopItemWithdrawList = queueEtopItemWithdrawRepository.getItemWaitingWithdraw();
        return queueEtopItemWithdrawList;
    }

    public List<QueueEtopItemWithdraw> listItemWithdraw(){
        List<QueueEtopItemWithdraw> queueEtopItemWithdrawList = queueEtopItemWithdrawRepository.getItemWithdraw();
        return queueEtopItemWithdrawList;
    }

    public void deleteQueueItem(String name){
        List<QueueEtopItemWithdraw> queueEtopItemWithdrawList = queueEtopItemWithdrawRepository.findByName(name);
        queueEtopItemWithdrawRepository.deleteAll(queueEtopItemWithdrawList);
    }

    public void deleteAllQueueItem(){
        List<QueueEtopItemWithdraw> queueEtopItemWithdrawList = queueEtopItemWithdrawRepository.getItemWaitingWithdraw();
        queueEtopItemWithdrawRepository.deleteAll(queueEtopItemWithdrawList);
    }

    private void updateStatusWithdraw(String status){
        CronJobLog cronJobLog = cronJobLogRepository.findByPageAndType("etop", "withdraw").get(0);
        cronJobLog.setStatus(status);

        cronJobLogRepository.save(cronJobLog);

    }

}
