package com.example.BookPort.notification.service;

import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.BookPort.common.constants.AppConstants;
import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.notification.entity.EmailNotifLog;
import com.example.BookPort.notification.repository.EmailNotifLogRepo;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderService {
	
	private Debugger d = new Debugger(this.getClass());
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private EmailNotifLogRepo emailNotifLogRepo;

	
	public void sendEmail(String toEmail, String fromEmail, String subject, String body, EmailNotifLog notifLog) {
		d.dbg("Inside sendEmail with toEmail: "+toEmail+" fromEmail: "+fromEmail);
		d.dbg("Subject: "+subject);
		d.dbg("Body: \n"+body);
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	        helper.setTo(toEmail);
	        helper.setFrom(fromEmail);
	        helper.setSubject(subject);

	        // true = HTML content
	        helper.setText(body, true);
			
	        javaMailSender.send(message);
			notifLog.setNotifStatus(AppConstants.SUCCESS);
			d.dbg("Mail Sent successfully.");
		} catch(Exception e) {
			d.dbg("Something went wrong", Level.ERROR, e);
			notifLog.setNotifStatus(AppConstants.FAILURE);
		}
		d.dbg("Returing from sendEmail.");
	}
}
