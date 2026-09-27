package com.example.BookPort.user.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.example.BookPort.common.constants.AppConstants;
import com.example.BookPort.common.dto.CoreUserDetailsDto;
import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.user.assembler.CoreUserAssembler;
import com.example.BookPort.user.entity.CoreUserDetail;
import com.example.BookPort.user.repository.CoreUserDetailRepo;

import jakarta.transaction.Transactional;
@Service
public class CoreRegisterService {
	
	@Autowired
	CoreUserDetailRepo coreUserRepo;
	
	CoreUserAssembler assembler = new CoreUserAssembler();
	
	@Autowired
	ApplicationContext context;

	private Debugger d = new Debugger(this.getClass());
	
	
	/*
	 * This is a helper function to auth module, when a new user is approved by the auth module, 
	 * a entry needs to be create in core table as well. so this method will be invoked by auth to do that process.
	 */
	@Transactional
	public String register(String userId, CoreUserDetailsDto user) {
		CoreUserDetail coreUser = new CoreUserDetail();
		
		try {
			coreUser.setUserId(userId);
			coreUser.setUsername(user.getUsername());
			coreUser.setName(user.getName());
			coreUser.setEmail(user.getEmail());
			coreUser.setMobileNumber(user.getMobileNumber());

			if (user.getGender() != null) {
				coreUser.setGender(user.getGender().toString());
			}

			if (user.getDob() != null) {
				coreUser.setDob(user.getDob());
			}

			coreUser.setStatus(AppConstants.ACTIVE);
			coreUser.setCreatedAt(LocalDateTime.now());
			coreUser.setUpdatedAt(LocalDateTime.now());
			
			coreUserRepo.save(coreUser);
			d.dbg("User registered successfully in core_user_details table.");
			return "SUCCESS";
			
		} catch(Exception e) {
			d.dbg("Something went wrong.");
			throw new RuntimeException();
			
		}
		
		

	}



	
}
