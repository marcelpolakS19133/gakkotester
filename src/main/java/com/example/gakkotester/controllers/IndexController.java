package com.example.gakkotester.controllers;

import com.example.gakkotester.services.GakkoStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Controller
public class IndexController {

    private final GakkoStatusService statusService;


    public IndexController(@Autowired GakkoStatusService statusService) {
        this.statusService = statusService;
    }


    @GetMapping("/")
    public DeferredResult<String> index(Model model) {
        DeferredResult<String> result = new DeferredResult<>(null, "");

        AtomicBoolean isUp = new AtomicBoolean();
        AtomicReference<String> uptime = new AtomicReference<>();

        CompletableFuture.allOf(CompletableFuture.runAsync(()->
                    isUp.set(statusService.isUp())
                ),
                CompletableFuture.runAsync(()->
                    uptime.set(statusService.getUptime())
                )).join();

        model.addAttribute("status", isUp.get() ?"Gakko działa!":"Gakko nie działa");
        model.addAttribute("uptime", uptime.get());
        model.addAttribute("isUp", isUp.get());

        result.setResult("index");
        return result;
    }
}
