package com.example.gakkotester.services;

import com.example.gakkotester.dao.StatusRepository;
import com.example.gakkotester.models.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public String getUptime(){
        Long up = statusRepository.countStatusesByStatus("OK");
        Long down = statusRepository.countStatusesByStatus("DEAD");

        return df.format(((up+down)==0)?100:100.0*up/(up+down))+"%";
    }

    public String getUptime(Date from, Date to) {
        Long up = statusRepository.countStatusesByDateTimeBetweenAndStatus(from, to, "OK");
        Long down = statusRepository.countStatusesByDateTimeBetweenAndStatus(from, to, "DEAD");

        return df.format(((up+down)==0)?100:100.0*up/(up+down))+"%";
    }
    @Scheduled(fixedRate=30000)
    public void refreshStatus() {

        try{
            rt.getForEntity("https://gakko.pjwstk.edu.pl/", String.class);
            this.status.set(true);
        }catch(ResourceAccessException |HttpStatusCodeException e){
            this.status.set(false);
        }

        System.out.println(status);
        statusRepository.save(new Status(new Date(System.currentTimeMillis()), this.status.get()));
    }

    public boolean isUp(){
        return this.status.get();
    }
}
