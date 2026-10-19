package com.example.BookPort.auth.listener;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.BookPort.auth.service.AuthRolesService;
import com.example.BookPort.common.constants.KafkaConstants;
import com.example.BookPort.common.dto.IntraRequest;
import com.example.BookPort.common.exceptions.ValidationException;
import com.example.BookPort.common.logging.Debugger;

@Service
public class AuthorityRequestListener {
	
	private Debugger d = new Debugger(this.getClass());

	
	@Autowired
	AuthRolesService authRoleService;
	
	@KafkaListener(topics = KafkaConstants.AUTH_ROLE_REQUEST)
	public void processAuthority(IntraRequest request){

		d.dbg("Received AUTH_ROLE_REQUEST");
		authRoleService.processAuthority(request);

	}
}
