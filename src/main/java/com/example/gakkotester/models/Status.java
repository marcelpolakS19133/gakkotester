package com.example.gakkotester.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Status {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;

    private String status;

    public Status(Date dateTime, boolean status){
        this.dateTime=dateTime;
        this.status=status?"OK":"DEAD";
    }

    protected Status(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
