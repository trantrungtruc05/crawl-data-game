package com.tructran.crawl.controller;

import com.tructran.crawl.dto.ConfigurationDTO;
import com.tructran.crawl.service.ConfigurationService;
import com.tructran.crawl.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "pages")
public class ConfigurationV2Controller {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private WithdrawService withdrawService;

    @GetMapping(value = "configuration")
    public String mmConfiguration(ModelMap map) {
        ConfigurationDTO configurationDTO = configurationService.getConfig();
        map.addAttribute("configurationDTO", configurationDTO);

        return "pages/configuration";
    }

    @RequestMapping(value = "/update/config", method = RequestMethod.POST)
    public String updateConfig(@ModelAttribute("configurationDTO") ConfigurationDTO configurationDTO) {

        configurationService.updateConfig(configurationDTO);
        return "redirect:/pages/configuration";
    }

    @RequestMapping("/run/manual/withdraw_etop")
    public String manualWithdrawEtop() {
        withdrawService.autoWithDraw();
        return "redirect:/pages/configuration";
    }
}
