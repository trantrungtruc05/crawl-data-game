package com.tructran.crawl.service;

import com.tructran.crawl.model.*;
import com.tructran.crawl.repository.*;
import com.tructran.request.FiatVNDRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class CrawlService {

    @Autowired
    private EmpirePageRepository empirePageRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CronJobLogRepository cronJobLogRepository;

    @Autowired
    private PageCrawlLogRepository pageCrawlLogRepository;

    @Autowired
    private MailService mailService;

    private List<BuffPage> buffPageList;

    private String cat;

    @Value("${email.to}")
    private String emailTo;

    @Value("${number.server}")
    private String numberServer;

    // crawl empire page
    public String empirePageCrawl() {

        Configuration configEnableCrawl = configurationRepository.findByKeyAndType("enable_empire", "crawl");
        Boolean enable = Boolean.valueOf(configEnableCrawl.getValue());

        if (enable) {
            try {
                Configuration empireCookie = configurationRepository.findByKeyAndType("empire_crawl", "cookie");

                this.updateStatusCrawl("empire", "running");
                System.out.println("----- START CRAWL EMPIRE ----- AT: " + new Date());

                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.set("Cookie", empireCookie.getValue());
                HttpEntity<String> entity = new HttpEntity<String>(headers);
                JSONArray arrayData;

                List<EmpirePage> empirePageList = new ArrayList<>();

                PageCrawlLog pageCrawlLog = pageCrawlLogRepository.findByWebPage("empire").get(0);
                int page = pageCrawlLog.getPage();

                do {
                    System.out.println(">>>>>>> crawling Empire with page: " + page);
                    String response = restTemplate.exchange(
                            String.format("https://csgoempire.com/api/v2/trading/items?per_page=160&page=%d&price_min=1&price_max_above=999999&sort=desc&order=market_value", page)
                            , HttpMethod.GET, entity, String.class).getBody();

                    JSONObject empireJsonObject = new JSONObject(response);
                    arrayData = empireJsonObject.getJSONArray("data");
                    for (int i = 0; i < arrayData.length(); i++) {
                        String mName = arrayData.getJSONObject(i).getString("market_name").trim();
                        Integer mInt = arrayData.getJSONObject(i).getInt("market_value");
                        Long itemId = arrayData.getJSONObject(i).getLong("id");

                        Double mValue;

                        if (!arrayData.getJSONObject(i).isNull("custom_price_percentage")) {
                            Integer percent = arrayData.getJSONObject(i).getInt("custom_price_percentage") - 6;
                            mValue = (Double.valueOf(mInt) / 100) / (100 + percent) * 100;
                        } else {
                            mValue = (Double.valueOf(mInt) / 100);
                        }


                        Configuration empirePricing = configurationRepository.findByKeyAndType("empire", "currency");
                        Double priceVND = mValue * Double.valueOf(empirePricing.getValue());

                        EmpirePage empirePage = new EmpirePage();
                        empirePage.setName(mName);
                        empirePage.setOriginalPrice(mValue);
                        empirePage.setPriceByVnd(Math.round(priceVND));
                        empirePage.setItemId(itemId);
                        empirePage.setCreateAt(new Date());
                        empirePage.setOriginalPriceNotPercentage((Double.valueOf(mInt) / 100));

                        empirePageList.add(empirePage);
                    }

                    page++;

                    // update status page
                    PageCrawlLog pageCrawlLogDoing = pageCrawlLogRepository.findByWebPage("empire").get(0);
                    pageCrawlLogDoing.setPage(page);

                    pageCrawlLogRepository.save(pageCrawlLogDoing);

//                    Thread.sleep(2000);

                    Configuration configuration = configurationRepository.findByKeyAndType("mail_validate_empire", "mail_cookie");
                    configuration.setValue("1");

                    configurationRepository.save(configuration);
                } while (arrayData.length() > 0);

                PageCrawlLog pageCrawlLogFinish = pageCrawlLogRepository.findByWebPage("empire").get(0);
                pageCrawlLogFinish.setPage(1);

                pageCrawlLogRepository.save(pageCrawlLogFinish);

                List<CronJobLog> cronJobLogList = cronJobLogRepository.findByPageAndType("empire", "persist");
                CronJobLog cronJobLog = cronJobLogList.get(0);
                cronJobLog.setStatus("running");
                cronJobLogRepository.save(cronJobLog);

                empirePageRepository.deleteAll();
                empirePageRepository.saveAll(empirePageList);
                this.deleteDuplicateEmpirePage();

                cronJobLog.setStatus("idle");
                cronJobLogRepository.save(cronJobLog);

                System.out.println("Crawl Empire Sucess with size: " + empirePageList.size());

                this.updateStatusCrawl("empire", "idle");

                System.out.println("----- END CRAWL EMPIRE ----- AT: " + new Date());

                mailService.sendMail("Crawl Empire Csgo thành công vào lúc " + new Date(), "Crawl Empire Csgo thành công", emailTo);
                return "success_crawl_empire";
            } catch (Exception e) {
                e.printStackTrace();

                Configuration configuration = configurationRepository.findByKeyAndType("mail_validate_empire", "mail_cookie");

                if (e.getMessage().contains("not found") && configuration.getValue().equals("1")) {
                    mailService.sendMail("Cookie không hợp lệ", "Lỗi crawl Empire", emailTo);

                    configuration.setValue("0");
                    configurationRepository.save(configuration);

                }

                CronJobLog cronJobLog = cronJobLogRepository.findByPageAndType("empire", "job").get(0);
                cronJobLog.setStatus("idle");
                cronJobLogRepository.save(cronJobLog);

                empirePageCrawl();
                return "fail_crawl_empire";
            }
        } else {
            System.out.println("--- DISABLE CRAWL EMPIRE ---");
            return "disable_crawl_empire";
        }

    }

    private void deleteDuplicateEmpirePage() {
        List<DuplicateValue> duplicateValueList = empirePageRepository.getDupValue();

        List<EmpirePage> empirePageListDelete = new ArrayList<>();
        System.out.println("Adding Empire to list waiting Delete");
        for (DuplicateValue duplicateValue : duplicateValueList) {
            List<EmpirePage> empirePageList = empirePageRepository.findByNameAsc(duplicateValue.getName().trim());

            for (int i = 1; i < empirePageList.size(); i++) {
                empirePageListDelete.add(empirePageList.get(i));
            }
        }

        empirePageRepository.deleteAll(empirePageListDelete);

        // Delete special character
        List<EmpirePage> empireInvalidData = empirePageRepository.findEmpireInvalidData();
        empirePageRepository.deleteAll(empireInvalidData);


        System.out.println("Delete Duplicate Empire DONE >>>>>");
    }

    private String updateStatusCrawl(String page, String status) {

        List<CronJobLog> cronJobLogList = cronJobLogRepository.findByPageAndType(page, "job");
        if (cronJobLogList.isEmpty()) {
            CronJobLog cronJobLog = new CronJobLog();
            cronJobLog.setPage(page);
            cronJobLog.setStatus("idle");

            cronJobLogRepository.save(cronJobLog);

            cronJobLogList = cronJobLogRepository.findByPageAndType(page, "job");
        }

        CronJobLog cronJobLog = cronJobLogList.get(0);
        cronJobLog.setStatus(status);

        cronJobLogRepository.save(cronJobLog);

        return "update_status_cron_log_sucess";

    }

    public String checkStatusPersist() {
        List<CronJobLog> cronJobLogList = cronJobLogRepository.findByType("persist");
        for (CronJobLog cronJobLog : cronJobLogList) {
            if (cronJobLog.getStatus().equals("running")) {
                return "persist_true";
            }
        }

        return "persist_false";
    }

}
