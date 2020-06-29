package com.example.gakkotester.controllers;

import com.example.gakkotester.dao.StatusRepository;
import com.example.gakkotester.services.GakkoStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IndexController {

    private final GakkoStatusService statusService;


    public IndexController(@Autowired GakkoStatusService statusService) {
        this.statusService = statusService;
    }


    @GetMapping("/")
    public String index(Model model){
        boolean isUp = statusService.isUp();
        model.addAttribute("status", isUp?"Gakko działa!":"Gakko nie działa");
        model.addAttribute("uptime", statusService.getUptime());
        model.addAttribute("isUp", isUp);
        return "index";
    }
}
