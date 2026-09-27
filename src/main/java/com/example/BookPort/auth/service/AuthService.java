package com.example.BookPort.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BookPort.auth.assembler.AuthAssembler;
import com.example.BookPort.auth.entity.AuthUserDetails;
import com.example.BookPort.auth.repository.AuthUserDetailsRepo;
import com.example.BookPort.common.dto.AuthUserDetailsDto;
import com.example.BookPort.common.dto.IntraRequest;
import com.example.BookPort.common.exceptions.UserNotFoundException;
import com.example.BookPort.common.logging.Debugger;

@Service
public class AuthService {
	
	private Debugger d = new Debugger(this.getClass());
	
	@Autowired
	AuthUserDetailsRepo authUserDetailsRepo;
	
	private AuthAssembler assembler = new AuthAssembler();
	
	public AuthUserDetailsDto fetchAuthUserDetails(String userId) {
		d.dbg("Fetching AuthUserDetails.");
		AuthUserDetails authUser = authUserDetailsRepo.findAuthUserDetailsByUserId(userId);
		if(authUser == null) {
			d.dbg("Auth User not found.");
			throw new UserNotFoundException("User not found.");
		}
		
		AuthUserDetailsDto response = assembler.assembleAuthUserDetails(authUser);
		
		return response;
	}
	
	public List<AuthUserDetailsDto> fetchAuthUsers(IntraRequest intraRequest) {
		List<String> userIds = (List<String>) intraRequest.getQueryParam();
		d.dbg("User IDs received from Core: " + userIds);
		List<AuthUserDetails> authUsers = authUserDetailsRepo.findAllById(userIds);
		List<AuthUserDetailsDto> response = new ArrayList<>();
		for (AuthUserDetails authUser : authUsers) {

	        AuthUserDetailsDto authUserDto =assembler.assembleAuthUserDetails(authUser);

	        response.add(authUserDto);
	    }	

	    d.dbg("Auth users fetched: " + response.size());

	    return response;
	}

}
