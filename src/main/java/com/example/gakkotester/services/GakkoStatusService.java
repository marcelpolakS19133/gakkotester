package com.example.gakkotester.services;

import com.example.gakkotester.dao.StatusRepository;
import com.example.gakkotester.models.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class GakkoStatusService {
    private final StatusRepository statusRepository;
    private final DecimalFormat df = new DecimalFormat("#.##");

    private final RestTemplate rt = new RestTemplate();

    private AtomicBoolean status;

    public GakkoStatusService(@Autowired StatusRepository statusRepository){
        this.statusRepository=statusRepository;
        this.status=new AtomicBoolean(true);
    }


    public String getUptime() {
        AtomicReference<Long> up = new AtomicReference<>();
        AtomicReference<Long> down = new AtomicReference<>();

        CompletableFuture.allOf(
            CompletableFuture.runAsync(()-> {
                try {
                    up.set(statusRepository.countStatusesByStatus("OK").get());
                } catch (InterruptedException | ExecutionException e) {
                    up.set(0L);
                }
            })
            ,CompletableFuture.runAsync(()-> {
                try {
                    down.set(statusRepository.countStatusesByStatus("DEAD").get());
                } catch (InterruptedException | ExecutionException e) {
                    down.set(0L);
                }
            })).join();

        return df.format(((up.get() +down.get())==0)?100:100.0* up.get() /(up.get() +down.get()))+"%";
    }

    public String getUptime(Date from, Date to) {
        AtomicReference<Long> up = new AtomicReference<>();
        AtomicReference<Long> down = new AtomicReference<>();
        CompletableFuture.allOf(
                CompletableFuture.runAsync(()-> {
                    try {
                        up.set(statusRepository.countStatusesByDateTimeBetweenAndStatus(from, to, "OK").get());
                    } catch (InterruptedException | ExecutionException e) {
                        up.set(0L);
                    }
                })
                ,CompletableFuture.runAsync(()-> {
                    try {
                        down.set(statusRepository.countStatusesByDateTimeBetweenAndStatus(from, to, "DEAD").get());
                    } catch (InterruptedException | ExecutionException e) {
                        down.set(0L);
                    }
                })).join();

        return df.format(((up.get() +down.get())==0)?100:100.0* up.get() /(up.get() +down.get()))+"%";
    }
    @Scheduled(fixedRate=30000)
    @Async
    public void refreshStatus() {
        try{
            rt.getForEntity("https://gakko.pjwstk.edu.pl/", String.class);
            this.status.set(true);
        }catch(ResourceAccessException |HttpStatusCodeException e){
            this.status.set(false);
        }
        statusRepository.save(new Status(new Date(System.currentTimeMillis()), this.status.get()));
    }

    public boolean isUp(){
        return this.status.get();
    }
}
