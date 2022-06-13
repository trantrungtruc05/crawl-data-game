package com.tructran.crawl.service;

import com.tructran.crawl.model.CheckEmpireEntity;
import com.tructran.crawl.model.Configuration;
import com.tructran.crawl.model.QueueEmpireItemWithdraw;
import com.tructran.crawl.repository.ConfigurationRepository;
import com.tructran.crawl.repository.EmpirePageRepository;
import com.tructran.crawl.repository.QueueEmpireItemWithdrawRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class WithdrawEmpireService {

    @Autowired
    private QueueEmpireItemWithdrawRepository queueEmpireItemWithdrawRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private EmpirePageRepository empirePageRepository;

    public String addQueueItemWithDrawEmpire(List<String> lsInfo) {


        List<QueueEmpireItemWithdraw> queueEmpireItemWithdrawList = new ArrayList<>();

        for(String info : lsInfo){
            QueueEmpireItemWithdraw queueEmpireItemWithdraw = new QueueEmpireItemWithdraw();
            queueEmpireItemWithdraw.setName(info.split("###")[0]);
            queueEmpireItemWithdraw.setEmpirePriceCustom(Double.valueOf(info.split("###")[1]));
            queueEmpireItemWithdraw.setStatus(false);
            queueEmpireItemWithdraw.setUpdateAt(new Date());

            queueEmpireItemWithdrawList.add(queueEmpireItemWithdraw);
        }

        queueEmpireItemWithdrawRepository.saveAll(queueEmpireItemWithdrawList);
        return "success";
    }


    public void autoWithDraw(){

        try{
            Integer checkEmpirePersist = configurationRepository.checkEmpirePersist();
            if(checkEmpirePersist <= 0){
                List<CheckEmpireEntity> checkEmpireEntityList = empirePageRepository.checkEmpireCondition();
                System.out.println("Auto withdraw empire");

                for (CheckEmpireEntity checkEmpireEntity : checkEmpireEntityList) {
                    Thread.sleep(1000);
                    String token = this.generateToken();
                    if (token != null) {
                        Thread.sleep(1000);
                        this.withdraw(checkEmpireEntity.getItemId(), token, checkEmpireEntity.getOriginalPriceNotPercentage() * 100, checkEmpireEntity.getName());
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private String generateToken(){

        try{
            Configuration empireCookie = configurationRepository.findByKeyAndType("empire_withdraw", "cookie");

            JSONObject body = new JSONObject();
            body.put("code", "0000");
            body.put("uuid", String.valueOf(UUID.randomUUID()));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Cookie", empireCookie.getValue());

            HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

            String response = restTemplate.postForObject("https://csgoempire.com/api/v2/user/security/token"
                    , entity, String.class);

            JSONObject tokenJsonObject = new JSONObject(response);
            if(tokenJsonObject.getBoolean("success")){
                System.out.println("token response: " + response);
                return tokenJsonObject.getString("token");
            }else{
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private String withdraw(Long itemId, String token, Double value, String name){
        try{
            Configuration empireCookie = configurationRepository.findByKeyAndType("empire_withdraw", "cookie");

            JSONObject request = new JSONObject();
            request.put("security_token", token);
            request.put("coin_value", value);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Cookie", empireCookie.getValue());

            HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);
            System.out.println(String.format("Request empire withdraw item %s : ", name) + request);

            String response = restTemplate.postForObject(String.format("https://csgoempire.com/api/v2/trading/deposit/%d/withdraw", itemId)
                    , entity, String.class);

            JSONObject tokenJsonObject = new JSONObject(response);
            if(tokenJsonObject.getBoolean("success")){

                System.out.println(String.format("Withdraw Sucess with name %s response: ", name));

                QueueEmpireItemWithdraw queueEmpireItemWithdraw = queueEmpireItemWithdrawRepository.findByName(name).get(0);
//                queueEmpireItemWithdraw.setResponseWithdraw(response);
                queueEmpireItemWithdraw.setUpdateAt(new Date());
                queueEmpireItemWithdraw.setStatus(true);

                queueEmpireItemWithdrawRepository.save(queueEmpireItemWithdraw);
                return "withdraw_sucess";
            }else{
                System.out.println(String.format("Withdraw FAIL with name %s response: ", name) + response);
                return "withdraw_fail";
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            if(e.getMessage().contains("nested exception is java.net.SocketTimeoutException")){
                QueueEmpireItemWithdraw queueEmpireItemWithdraw = queueEmpireItemWithdrawRepository.findByName(name).get(0);
                queueEmpireItemWithdraw.setResponseWithdraw("Read time out");
                queueEmpireItemWithdraw.setUpdateAt(new Date());
                queueEmpireItemWithdraw.setStatus(true);

                queueEmpireItemWithdrawRepository.save(queueEmpireItemWithdraw);
                return "withdraw_sucess";
            }
            return "withdraw_fail";
        }
    }

    public List<QueueEmpireItemWithdraw> listItemWaitingWithdraw(){
        List<QueueEmpireItemWithdraw> queueEmpireItemWithdrawList = queueEmpireItemWithdrawRepository.getItemWaitingWithdraw();
        return queueEmpireItemWithdrawList;
    }

    public void deleteQueueItem(String name){
        List<QueueEmpireItemWithdraw> queueEmpireItemWithdrawList = queueEmpireItemWithdrawRepository.findByName(name);
        queueEmpireItemWithdrawRepository.deleteAll(queueEmpireItemWithdrawList);
    }

    public List<QueueEmpireItemWithdraw> listItemWithdraw(){
        List<QueueEmpireItemWithdraw> queueEmpireItemWithdrawList = queueEmpireItemWithdrawRepository.getItemWithdraw();
        return queueEmpireItemWithdrawList;
    }

    public void deleteAllQueueItem(){
        List<QueueEmpireItemWithdraw> queueEmpireItemWithdrawList = queueEmpireItemWithdrawRepository.findAll();
        queueEmpireItemWithdrawRepository.deleteAll(queueEmpireItemWithdrawList);

    }
}
