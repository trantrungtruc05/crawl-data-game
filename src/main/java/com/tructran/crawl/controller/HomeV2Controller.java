package com.tructran.crawl.controller;

import com.tructran.crawl.dto.ItemDTO;
import com.tructran.crawl.model.ExcelFileEntity;
import com.tructran.crawl.model.QueueEtopItemWithdraw;
import com.tructran.crawl.service.DotaWriteExcelService;
import com.tructran.crawl.service.EtopExcelService;
import com.tructran.crawl.service.WithdrawEmpireExcelService;
import com.tructran.crawl.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "pages")
public class HomeV2Controller {

    @Autowired
    private EtopExcelService etopExcelService;

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private DotaWriteExcelService dotaWriteExcelService;

    @Autowired
    private WithdrawEmpireExcelService withdrawEmpireExcelService;

    @RequestMapping("/csgo")
    public String initPageCsgo(ModelMap map) {

        List<ExcelFileEntity> excelFileEntityList = new ArrayList<>();
        map.addAttribute("excelFileEntityList", excelFileEntityList);

        return "pages/csgo_item";
    }

    @RequestMapping(value = {"/csgo/{min}/{max}", "/csgo/{min}/{max}/{filter}", "/csgo/{filter}"})
    public ModelAndView listCsgo(@PathVariable(required = false) Integer min, @PathVariable(required = false) Integer max, @PathVariable(required = false) Double filter) {
        ModelAndView mav = new ModelAndView("pages/csgo_item");
        min = min == null ? -10 : min;
        max = max == null ? -3 : max;

        List<ExcelFileEntity> excelFileEntityList = etopExcelService.generateExcelListFilter(min, max);

        // remove startrak
        excelFileEntityList = excelFileEntityList.stream().filter(excelFileEntity -> !excelFileEntity.getName().contains("StatTrak™")).collect(Collectors.toList());

        if(filter != null){
            excelFileEntityList = excelFileEntityList.stream().filter(excelFileEntity -> excelFileEntity.getEtopOriginPrice() >= filter).collect(Collectors.toList());
        }

        mav.addObject("excelFileEntityList", excelFileEntityList);
        return mav;
    }

    @PostMapping(value = "/buyItem")
    public String testCheckboxes(@ModelAttribute ItemDTO itemDTO) throws IOException {

        withdrawService.addQueueItemWithDraw(itemDTO.getItems());
        return "redirect:/pages/items/etop-waiting";
    }

    @RequestMapping(value = {"/dota/{min}/{max}", "/dota/{min}/{max}/{filter}", "/dota/{filter}"})
    public ModelAndView listDota2(@PathVariable(required = false) Integer min, @PathVariable(required = false) Integer max, @PathVariable(required = false) Double filter) {
        ModelAndView mav = new ModelAndView("pages/dota_item");

        min = min == null ? -20 : min;
        max = max == null ? -3 : max;

        List<ExcelFileEntity> excelFileEntityList = dotaWriteExcelService.generateExcelListFilter(min, max);
        if(filter != null){
            excelFileEntityList = excelFileEntityList.stream().filter(excelFileEntity -> excelFileEntity.getEtopOriginPrice() >= filter).collect(Collectors.toList());
        }

        mav.addObject("excelFileEntityList", excelFileEntityList);

        return mav;
    }

    @RequestMapping("/dota")
    public ModelAndView initPageDota2() {
        ModelAndView mav = new ModelAndView("pages/dota_item");

        List<ExcelFileEntity> excelFileEntityList = new ArrayList<>();
        mav.addObject("excelFileEntityList", excelFileEntityList);

        return mav;
    }

    @RequestMapping("/items/etop-waiting")
    public ModelAndView listAllItemWithdraw() {

        ModelAndView mav = new ModelAndView("pages/withdraw_etop_result");

        List<QueueEtopItemWithdraw> queueEtopItemWithdrawList = withdrawService.listItemWaitingWithdraw();
        mav.addObject("queueEtopItemWithdrawList", queueEtopItemWithdrawList);

        return mav;
    }

    @RequestMapping("/etop/delete/{name}")
    public String deleteProduct(@PathVariable(name = "name") String name) {

        withdrawService.deleteQueueItem(name);
        return "redirect:/pages/items/etop-waiting";
    }

    @RequestMapping("/etop/delete/all")
    public String deleteAllProduct() {

        withdrawService.deleteAllQueueItem();
        return "redirect:/pages/items/etop-waiting";
    }

    @RequestMapping("/items/etop-success")
    public ModelAndView listItemWithdraw() {

        ModelAndView mav = new ModelAndView("pages/withdraw_etop_report");

        List<QueueEtopItemWithdraw> queueEtopItemWithdrawList = withdrawService.listItemWithdraw();
        mav.addObject("queueEtopItemWithdrawList", queueEtopItemWithdrawList);

        return mav;
    }
}
