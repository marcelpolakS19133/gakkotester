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
    private LocalDateTime dateTime;

    private String status;


    public Status(LocalDateTime dateTime, boolean status){
        this.dateTime=dateTime;
        this.status=status?"OK":"DEAD";
    }

    protected Status(){}
}
