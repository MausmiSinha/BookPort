package com.example.BookPort.auth.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.management.relation.RoleNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.BookPort.auth.assembler.AuthAssembler;
import com.example.BookPort.auth.entity.AuthUserDetails;
import com.example.BookPort.auth.entity.Role;
import com.example.BookPort.auth.entity.UserRoles;
import com.example.BookPort.auth.repository.AuthUserDetailsRepo;
import com.example.BookPort.auth.repository.RoleRepo;
import com.example.BookPort.auth.repository.UserRolesRepo;
import com.example.BookPort.common.constants.ActionConstants;
import com.example.BookPort.common.constants.AppConstants;
import com.example.BookPort.common.constants.KafkaConstants;
import com.example.BookPort.common.dto.AuthRoleRequestDto;
import com.example.BookPort.common.dto.AuthRoleResponseDto;
import com.example.BookPort.common.dto.AuthUserDetailsDto;
import com.example.BookPort.common.dto.IntraRequest;
import com.example.BookPort.common.dto.OprationEnum;
import com.example.BookPort.common.dto.RequestProfile;
import com.example.BookPort.common.dto.RoleEnum;
import com.example.BookPort.common.dto.StatusEnum;
import com.example.BookPort.common.exceptions.UserNotFoundException;
import com.example.BookPort.common.exceptions.ValidationException;
import com.example.BookPort.common.kafka.producer.KafkaProducerService;
import com.example.BookPort.common.logging.Debugger;

@Service
public class AuthService {

	private Debugger d = new Debugger(this.getClass());

	@Autowired
	AuthUserDetailsRepo authUserDetailsRepo;

	@Autowired
	RoleRepo roleRepo;

	@Autowired
	UserRolesRepo userRoleRepo;
	
	@Autowired
	KafkaProducerService kafkaProducerService;

	private AuthAssembler assembler = new AuthAssembler();

	public AuthUserDetailsDto fetchAuthUserDetails(String userId) {
		d.dbg("Fetching AuthUserDetails.");
		AuthUserDetails authUser = authUserDetailsRepo.findAuthUserDetailsByUserId(userId);
		if (authUser == null) {
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

			AuthUserDetailsDto authUserDto = assembler.assembleAuthUserDetails(authUser);

			response.add(authUserDto);
		}

		d.dbg("Auth users fetched: " + response.size());

		return response;
	}



}
