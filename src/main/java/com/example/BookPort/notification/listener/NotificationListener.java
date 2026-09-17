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
public class NotificationListener {

	private Debugger d = new Debugger(this.getClass());

	@Autowired
	NotificationService notificationService;
	
	@KafkaListener(topics = KafkaConstants.REGISTER_NOTIFICATION, groupId = "bookport-group")
	public void listenRegister(IntraRequest request) {
		d.dbg("Inside listenRegister method.");
		d.dbg("Incoming Request: "+ request);
		try {
			HashMap<String, Object> registerNotificationRequest = (HashMap<String, Object>) request.getPayload();
			d.dbg("registerNotificationRequest");
			d.dbg(registerNotificationRequest.toString());
			notificationService.sendRegistrationEmail(registerNotificationRequest);
			d.dbg("Register notification sent successfully!!");
		} catch(Exception e) {
			d.dbg("Something went wrong", Level.ERROR, e);
		}

	}

	@KafkaListener(topics = KafkaConstants.APPROVE_NOTIFICATION, groupId = "bookport-group")
	public void listen(IntraRequest request) {
		d.dbg("Inside listen method.");
		d.dbg("Incoming Request: "+ request);
		try {
			HashMap<String, Object> approveNotifRequest = (HashMap<String, Object>) request.getPayload();
			notificationService.sendRegistrationApprovedEmail(approveNotifRequest);
			d.dbg("Sucessfull!!");
		} catch(Exception e) {
			d.dbg("Something went wrong", Level.ERROR, e);
		}

	}
	
	@KafkaListener(topics = KafkaConstants.UPDATE_NOTIFICATION, groupId = "bookport-group")
	public void listenUpdate(IntraRequest request) {
		d.dbg("Incoming Request: "+ request);
		try {
			HashMap<String, Object> updateNotificationRequest = (HashMap<String, Object>) request.getPayload();
			d.dbg("updateNotificationRequest");
			d.dbg(updateNotificationRequest.toString());
			notificationService.sendUpdateEmail(updateNotificationRequest);
			d.dbg("Updation notification sent successfully!!");
		} catch(Exception e) {
			d.dbg("Something went wrong", Level.ERROR, e);
		}

	}

}
