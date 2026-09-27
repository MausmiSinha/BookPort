package com.example.BookPort.user.assembler;

import java.util.ArrayList;
import java.util.List;

import com.example.BookPort.common.dto.AuthUserDetailsDto;
import com.example.BookPort.user.dto.UserDetailsResponseDto;
import com.example.BookPort.user.entity.CoreUserDetail;

public class CoreUserAssembler {
	
	public UserDetailsResponseDto assembleUserDetails(CoreUserDetail coreUser, AuthUserDetailsDto authUser) {
		UserDetailsResponseDto response = new UserDetailsResponseDto();
		
		response.setUserId(coreUser.getUserId());
		response.setUsername(coreUser.getUsername());
		response.setName(coreUser.getName());
		response.setEmail(coreUser.getEmail());
		response.setMobileNumber(coreUser.getMobileNumber());
		response.setGender(coreUser.getGender());
		response.setDob(coreUser.getDob());
		response.setStatus(coreUser.getStatus());
		response.setCreatedAt(coreUser.getCreatedAt());
		response.setUpdatedAt(coreUser.getUpdatedAt());
		response.setAccountNonExpired(authUser.getAccountNonExpired());
		response.setAccountNonLocked(authUser.getAccountNonLocked());
		response.setCredentialsNonExpired(authUser.getCredentialsNonExpired());
		response.setEnabled(authUser.getEnabled());
		response.setRoles(authUser.getRoles());
		return response;
	}

}
