package com.example.BookPort.auth.assembler;

import java.io.IOException;
import java.util.List;

import com.example.BookPort.auth.dto.AuthRegisterRequestDto;
import com.example.BookPort.auth.dto.AuthUpdateRequestDto;
import com.example.BookPort.auth.dto.PendingUserResponseDto;
import com.example.BookPort.auth.dto.TrackResponseDto;
import com.example.BookPort.auth.entity.RegistrationRequestAudit;
import com.example.BookPort.auth.entity.RegistrationRequestStageView;

public class AuthAssembler {
	
	
	//Dto to Entity: Assemble AuthRegisterRequestDto to RgistrationRequestStagegate(Entity)
	public RegistrationRequestAudit assembleRegistrationRequestAudit(AuthRegisterRequestDto dto) {
		RegistrationRequestAudit audit = new RegistrationRequestAudit();

	    audit.setVersionNo(dto.getVersionNo());
	    audit.setRegistrationId(dto.getRegistrationId());
	    audit.setUsername(dto.getUsername());
	    audit.setEmail(dto.getEmail());
	    audit.setName(dto.getName());
	    audit.setMobNumber(dto.getMobNumber());
	    audit.setGender(dto.getGender());
	    audit.setDob(dto.getDob());
	    audit.setStatus(dto.getStatus());
	    audit.setPassword(dto.getPassword());
	    audit.setGovIdFront(dto.getGovIdFront());
	    audit.setGovIdBack(dto.getGovIdBack());
	    audit.setPhoto(dto.getPhoto());
	    audit.setProcessingStatus(dto.getProcessingStatus());
	    audit.setRemark(dto.getRemark());
	    audit.setUpdatedBy(dto.getUpdatedBy());

	    return audit;
	}
	
	//Dto to Entity: Assemble AuthUpdateRequestDto to RgistrationRequestStagegate(Entity)
	public RegistrationRequestAudit assembleUpdateRequestAudit(AuthUpdateRequestDto dto) {
		RegistrationRequestAudit audit = new RegistrationRequestAudit();

	    audit.setVersionNo(dto.getVersionNo());
	    audit.setRegistrationId(dto.getRegistrationId());
	    audit.setUsername(dto.getUsername());
	    audit.setEmail(dto.getEmail());
	    audit.setName(dto.getName());
	    audit.setMobNumber(dto.getMobNumber());
	    audit.setGender(dto.getGender());
	    audit.setDob(dto.getDob());
	    audit.setStatus(dto.getStatus());
	    audit.setGovIdFront(dto.getGovIdFront());
	    audit.setGovIdBack(dto.getGovIdBack());
	    audit.setPhoto(dto.getPhoto());
	    audit.setProcessingStatus(dto.getProcessingStatus());
	    audit.setRemark(dto.getRemark());
	    audit.setUpdatedBy(dto.getUpdatedBy());

	    return audit;
	}
	
	//Entity to Dto: Assemble RgistrationRequestStagegate(Entity) to AuthRegisterRequestDto
//	public AuthRegisterRequestDto assembleAuthRegisterRequestDto(RegistrationRequestAudit request) {
//		AuthRegisterRequestDto response = new AuthRegisterRequestDto();
//		response.setRegistrationId(request.getRegistrationId());
//		response.setUsername(request.getUsername());
//		response.setName(request.getName());
//		response.setEmail(request.getEmail());
//		response.setMobNumber(request.getMobNumber());
//		response.setDob(request.getDob());
//		response.setStatus(request.getStatus());
//		response.setCreatedAt(request.getCreatedAt());
//		response.setUpdatedAt(request.getUpdatedAt());
//		response.setPassword(request.getPassword());
//		response.setGovIdFront(request.getGovIdFront());
//		response.setGovIdBack(request.getGovIdBack());
//		return response;
//		
//	}
	
//	Entity to Dto: Assemble RegistrationRequestStagegate to PendingUserResponseDto
	public PendingUserResponseDto assemblerPendingUserResponseDto(RegistrationRequestStageView request) {
		PendingUserResponseDto response  = new PendingUserResponseDto();
		response.setRegistrationId(request.getRegistrationId());
		response.setUsername(request.getUsername());
		response.setName(request.getName());
		response.setEmail(request.getEmail());
		response.setMobNumber(request.getMobNumber());
		response.setGender(request.getGender());
		response.setDob(request.getDob());
		response.setStatus(request.getStatus());
		response.setCreatedAt(request.getCreatedAt());
		response.setUpdatedAt(request.getUpdatedAt());
		response.setPassword(request.getPassword());
		response.setGovIdFront("/auth/v1/"+request.getRegistrationId()+"/front/image");
		response.setGovIdBack("/auth/v1/"+request.getRegistrationId()+"/back/image");
		response.setPhoto("/auth/v1/"+request.getRegistrationId()+"/passport/image");
		response.setProcessingStatus(request.getProcessingStatus());
		response.setRemark(request.getRemark());
		return response;
		
	}

// Entity to Dto 
	public TrackResponseDto assembleTrackResponseDto(RegistrationRequestStageView entity) {
		TrackResponseDto dto = new TrackResponseDto();

	    dto.setRegistrationId(entity.getRegistrationId());
	    dto.setUsername(entity.getUsername());
	    dto.setName(entity.getName());
	    dto.setEmail(entity.getEmail());
	    dto.setMobNumber(entity.getMobNumber());
	    dto.setGender(entity.getGender());
	    dto.setDob(entity.getDob());
	    dto.setStatus(entity.getStatus());
	    dto.setCreatedAt(entity.getCreatedAt());
	    dto.setUpdatedAt(entity.getUpdatedAt());
	    dto.setGovIdFront("/v1/"+entity.getRegistrationId()+"/front/image");
	    dto.setGovIdBack("/v1/"+entity.getRegistrationId()+"/back/image");
	    dto.setPhoto("/v1/"+entity.getRegistrationId()+"/passport/image");
	    dto.setProcessingStatus(entity.getProcessingStatus());
	    dto.setRemark(entity.getRemark());
		return dto;
	}
	
}
