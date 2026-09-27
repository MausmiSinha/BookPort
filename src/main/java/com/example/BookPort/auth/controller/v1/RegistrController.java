package com.example.BookPort.auth.controller.v1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.BookPort.auth.dto.AuthRegisterRequestDto;
import com.example.BookPort.auth.dto.AuthUpdateRequestDto;
import com.example.BookPort.auth.service.AuthRegisterService;
import com.example.BookPort.common.constants.AppConstants;
import com.example.BookPort.common.dto.GenderEnum;
import com.example.BookPort.common.dto.ProcessingStatusEnum;
import com.example.BookPort.common.dto.StatusEnum;
import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.common.util.BookPortResponseBuilder;
import com.example.BookPort.common.util.IdGenerator;
import com.example.BookPort.common.util.RequestBodyValidator;
import com.example.BookPort.common.util.SecurityUtil;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;



@RestController
@RequestMapping("auth/v1/register")
public class RegistrController {
	
	private Debugger d = new Debugger(this.getClass());
	
	@Autowired
	private AuthRegisterService authRegisterService;
	
	@Autowired
	private RequestBodyValidator validator;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@PostMapping("/test")
	public String testRef() {
		IdGenerator idGen = new IdGenerator(AppConstants.Module.AUTH_MODULE_ID);
		return idGen.generateId();
	}
	
//	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public ResponseEntity<Object> register(@Valid @RequestBody AuthRegisterRequestDto request) {
//		d.dbg("Inside register controller.");
//		
////		Steps: 
////			1. Send call to AuthRegisterService.
//		Object user = authRegisterService.register(request);
////			2. Do Validations , if username already present or not.
////			3. If successful , then post record in registration_request_stagegate.
//		
//		
//		d.dbg("Return from register controller.");
//		
//		return new ResponseEntity<>(BookPortResponseBuilder.buildSuccessReponse(user), HttpStatus.CREATED);
//	}
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> register( 
			@RequestParam(value="username") String username,
	        @RequestParam(value="name")  String name,
	        @RequestParam(value="email")  String email,
	        @RequestParam(value="mobNumber")  String mobNumber,
	        @RequestParam(value="gender") GenderEnum gender,
	        @RequestParam(value="dob")  String dob,
	        @RequestParam(value="password")  String password,
	        @RequestParam(value="govIdFront") MultipartFile govIdFront, 
	        @RequestParam(value="govIdBack") MultipartFile govIdBack,
	        @RequestParam(value="photo") MultipartFile photo,
	        @RequestParam(value="remark") String remark) throws IOException, ParseException{
		
	    //Generate unique ID for incoming request. 
		IdGenerator idGen = new IdGenerator(AppConstants.Module.AUTH_MODULE_ID);
		String registrationId = idGen.generateId();
		d.dbg("Registration ID generated: "+ registrationId);
		
		AuthRegisterRequestDto request = new AuthRegisterRequestDto();
		request.setVersionNo(1);
	    request.setRegistrationId(registrationId);
		request.setUsername(username);
		request.setName(name);
		request.setEmail(email);
		request.setMobNumber(mobNumber);
		request.setGender(gender);
		LocalDate date = LocalDate.parse(dob);
		LocalDateTime dateTime = date.atStartOfDay();
		request.setDob(dateTime);
		request.setStatus(StatusEnum.PENDING);
		request.setPassword(passwordEncoder.encode(password));
		request.setGovIdFront(govIdFront.getBytes());
		request.setGovIdBack(govIdBack.getBytes());
		request.setPhoto(photo.getBytes());
		request.setRemark(remark);
		request.setProcessingStatus(ProcessingStatusEnum.HOLD);
	    request.setRemark(remark);
	    request.setUpdatedBy(username);
		
	    validator.validateDto(request);
	    
		d.dbg("Incoming request: "+ request.toString());
		
		d.dbg("Sending call to AuthRegisterService.");
		
		Object user = authRegisterService.register(request);
		
		d.dbg("Returning from register method.");
		
		return new ResponseEntity<>(BookPortResponseBuilder.buildSuccessReponse(user), HttpStatus.CREATED);

	}
	

}
