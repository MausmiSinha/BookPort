package com.example.BookPort.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BookPort.notification.entity.EmailNotifLog;

@Repository
public interface EmailNotifLogRepo extends JpaRepository<EmailNotifLog, String>{
	
}