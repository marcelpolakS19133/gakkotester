package com.example.gakkotester.dao;

import com.example.gakkotester.models.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

public interface StatusRepository extends CrudRepository<Status, Long> {
    public Long countStatusesByDateTimeBetweenAndStatus(Date dateTime, Date dateTime2, String status);
    public Long countStatusesByStatus(String status);
}