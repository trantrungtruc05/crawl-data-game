package com.tructran.crawl.controller;

import com.tructran.crawl.dto.ItemDTO;
import com.tructran.crawl.model.ExcelFileEntity;
import com.tructran.crawl.model.QueueEmpireItemWithdraw;
import com.tructran.crawl.service.CheckEtopBuffWithdrawService;
import com.tructran.crawl.service.WithdrawEmpireExcelService;
import com.tructran.crawl.service.WithdrawEmpireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "pages")
public class EmpireController {


    @Autowired
    private WithdrawEmpireService withdrawEmpireService;

    @Autowired
    private WithdrawEmpireExcelService withdrawEmpireExcelService;

    @Autowired
    private CheckEtopBuffWithdrawService checkEtopBuffWithdrawService;

    @RequestMapping("/withdraw/empire_both")
    public ModelAndView withdrawEmpire() {
        ModelAndView mav = new ModelAndView("pages/withdraw_empire");

        List<ExcelFileEntity> excelFileEntityList = withdrawEmpireExcelService.generateExcelList();
        mav.addObject("excelFileEntityList", excelFileEntityList);

        return mav;
    }

    @RequestMapping("/withdraw/empire_etop")
    public ModelAndView withdrawEmpireEtop() {
        ModelAndView mav = new ModelAndView("pages/withdraw_empire");

        List<ExcelFileEntity> excelFileEntityList = checkEtopBuffWithdrawService.generateExcelList("etop");
        mav.addObject("excelFileEntityList", excelFileEntityList);

        return mav;
    }

    @RequestMapping("/withdraw/empire_buff")
    public ModelAndView withdrawEmpireBuff() {
        ModelAndView mav = new ModelAndView("pages/withdraw_empire");

        List<ExcelFileEntity> excelFileEntityList = checkEtopBuffWithdrawService.generateExcelList("buff");
        mav.addObject("excelFileEntityList", excelFileEntityList);

        return mav;
    }

    @PostMapping(value = "/addQueueEmpireWithdraw")
    public String addQueueEmpireWithdraw(@ModelAttribute ItemDTO itemDTO) throws IOException {

        withdrawEmpireService.addQueueItemWithDrawEmpire(itemDTO.getItems());
        return "redirect:/pages/items/empire-waiting";
    }

    @RequestMapping("/items/empire-waiting")
    public ModelAndView listAllItemWithdrawEmpire() {

        ModelAndView mav = new ModelAndView("pages/withdraw_result");

        List<QueueEmpireItemWithdraw> queueEmpireItemWithdrawList = withdrawEmpireService.listItemWaitingWithdraw();
        mav.addObject("queueEmpireItemWithdrawList", queueEmpireItemWithdrawList);

        return mav;
    }

    @RequestMapping("/empire/delete/{name}")
    public String deleteProduct(@PathVariable(name = "name") String name) {

        withdrawEmpireService.deleteQueueItem(name);
        return "redirect:/pages/items/empire-waiting";
    }

    @RequestMapping("/items/empire-success")
    public ModelAndView listItemWithdraw() {

        ModelAndView mav = new ModelAndView("pages/withdraw_empire_report");

        List<QueueEmpireItemWithdraw> queueEmpireItemWithdrawList = withdrawEmpireService.listItemWithdraw();
        mav.addObject("queueEmpireItemWithdrawList", queueEmpireItemWithdrawList);

        return mav;
    }

    @RequestMapping("/empire/delete/all")
    public String deleteProduct() {

        withdrawEmpireService.deleteAllQueueItem();
        return "redirect:/pages/items/empire-waiting";
    }

    @RequestMapping("/run/manual/withdraw_empire")
    public String manualWithdrawEmpire() {
        withdrawEmpireService.autoWithDraw();
        return "redirect:/pages/items/empire-waiting";
    }
}
