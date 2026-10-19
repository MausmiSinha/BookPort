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
public class AuthorityResponseListener {

	private Debugger d = new Debugger(this.getClass());

	@Autowired
	NotificationService notificationService;

	@KafkaListener(topics = KafkaConstants.AUTHORITY_RESPONSE, groupId = "bookport-group")
	public void listenAuthorityResponse(IntraRequest request) {
		try {
			HashMap<String, Object> processAuthorityResponse = (HashMap<String, Object>) request.getPayload();
			d.dbg("processAuthorityResponse");
			d.dbg(processAuthorityResponse.toString());
			notificationService.sendAuthorityEmail(processAuthorityResponse);
			d.dbg("Updation notification sent successfully!!");
		} catch(Exception e) {
			d.dbg("Something went wrong", Level.ERROR, e);
		}
		
	}

}
