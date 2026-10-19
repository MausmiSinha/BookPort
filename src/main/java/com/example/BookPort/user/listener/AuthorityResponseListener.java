package com.example.BookPort.user.listener;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.BookPort.common.constants.KafkaConstants;
import com.example.BookPort.common.dto.IntraRequest;
import com.example.BookPort.common.exceptions.ValidationException;
import com.example.BookPort.common.kafka.producer.KafkaProducerService;
import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.user.repository.CoreUserDetailRepo;
import com.example.BookPort.user.repository.RoleRequestRepo;
import com.example.BookPort.user.service.CoreUserRolesService;

@Service
public class AuthorityResponseListener {
	
private Debugger d = new Debugger(this.getClass());
	
	@Autowired
	ApplicationContext context;
	
	@Autowired
	CoreUserDetailRepo coreUserRepo;
	
	@Autowired
	KafkaProducerService kafkaProducerService;
	
	@Autowired
	RoleRequestRepo roleRequestRepo;
	
	@Autowired
	CoreUserRolesService coreUserRolesService;

	
	
	@KafkaListener(topics = KafkaConstants.AUTH_ROLE_RESPONSE)
	public void processAuthorityResponse(IntraRequest request) throws ValidationException{
		d.dbg("Received AUTH_ROLE_RESPONSE");
		coreUserRolesService.processAuthorityResponse(request);
	
	}

}
