package com.example.BookPort.notification.listener;

import java.util.HashMap;

import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.BookPort.common.constants.KafkaConstants;
import com.example.BookPort.common.dto.IntraRequest;
import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.notification.service.NotificationService;

@Service
public class RequestRoleNotificationListener {
	private Debugger d = new Debugger(this.getClass());

	@Autowired
	NotificationService notificationService;
	
	@KafkaListener(topics = KafkaConstants.REQUEST_ROLE_NOTIFICATION)
	public void listenRequestRole(IntraRequest request) {
		d.dbg("ROLE_REQUEST_SUBMITTED listener started");
		try {
	        HashMap<String, Object> roleRequestNotification = (HashMap<String, Object>) request.getPayload();

	        d.dbg("Role request notification: " + roleRequestNotification);

	        notificationService.sendRoleRequestSubmittedEmail(
	                roleRequestNotification
	        );

	        d.dbg("REQUEST_ROLE_NTIFICATION listener completed successfully");

	    } catch (Exception e) {
	        d.dbg("Exception in ROLE_REQUEST_SUBMITTED listener: "
	                + e.getMessage());

	        e.printStackTrace();

	        throw e;
	    }
	}

}
