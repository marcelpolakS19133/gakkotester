package com.example.gakkotester.dao;

import com.example.gakkotester.models.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.Future;

public interface StatusRepository extends CrudRepository<Status, Long> {
    @Async
    Future<Long> countStatusesByDateTimeBetweenAndStatus(Date dateTime, Date dateTime2, String status);
    @Async
    Future<Long> countStatusesByStatus(String status);
}