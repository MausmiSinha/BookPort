package com.example.BookPort.auth.controller.v1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.text.ParseException;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.BookPort.auth.dto.AuthUpdateRequestDto;
import com.example.BookPort.auth.dto.LoginRequestDto;
import com.example.BookPort.auth.dto.PasswordResetRequestDto;
import com.example.BookPort.auth.dto.ProcessAuthRequstDto;
import com.example.BookPort.auth.exceptions.ValidationException;
import com.example.BookPort.auth.service.AuthLoginService;
import com.example.BookPort.auth.service.AuthRegisterService;
import com.example.BookPort.auth.service.PasswordResetService;
import com.example.BookPort.common.constants.AppConstants;
import com.example.BookPort.common.dto.GenderEnum;
import com.example.BookPort.common.dto.ProcessingStatusEnum;
import com.example.BookPort.common.dto.RequestProfile;
import com.example.BookPort.common.dto.RestRequestDto;
import com.example.BookPort.common.dto.StatusEnum;
import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.common.util.BookPortResponseBuilder;
import com.example.BookPort.common.util.RequestBodyValidator;
import com.example.BookPort.common.util.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth/v1")
public class AuthController {

	private Debugger d = new Debugger(this.getClass());

	@Autowired
	AuthLoginService authLoginService;

	@Autowired
	PasswordResetService passwordResetService;

	@Autowired
	private AuthRegisterService authRegisterService;

	@Autowired
	private RequestBodyValidator validator;
	
	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping("/login")
	public ResponseEntity<Object> login(@Valid @RequestBody LoginRequestDto request) {
		d.dbg("Sending credentials to login service.");
		Object response = authLoginService.login(request.getUsername(), request.getPassword());
		d.dbg("Returns accessToken in response.");
		return new ResponseEntity<>(BookPortResponseBuilder.buildSuccessReponse(response), HttpStatus.OK);
	}

	@PostMapping("/password/reset")
	public ResponseEntity<Object> resetPassword(@Valid @RequestBody PasswordResetRequestDto request) {

		Object response = passwordResetService.resetPassword(request);

		return new ResponseEntity<>(BookPortResponseBuilder.buildSuccessReponse(response), HttpStatus.OK);
	}

	@GetMapping("/track")
	public ResponseEntity<Object> track(@RequestParam(name = "id", required = true) String id)
			throws ValidationException {
		d.dbg("Inside track");
		if (id == null || id.isBlank()) {
			throw new ValidationException("Id is mandatory for tracking.");
		}

		Object response = authRegisterService.track(id);
		return new ResponseEntity<>(BookPortResponseBuilder.buildSuccessReponse(response), HttpStatus.OK);

	}

	@PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> update(@RequestParam("registrationId") String registrationId,
			@RequestParam("username") String username, @RequestParam("name") String name,
			@RequestParam("email") String email, @RequestParam("mobNumber") String mobNumber,
			@RequestParam("gender") GenderEnum gender, @RequestParam("dob") String dob,
			@RequestParam(value = "govIdFront", required = false) MultipartFile govIdFront,
			@RequestParam(value = "govIdBack", required = false) MultipartFile govIdBack,
			@RequestParam(value = "photo", required = false) MultipartFile photo,
			@RequestParam(value = "remark", required = false) String remark) throws IOException, ParseException {
		AuthUpdateRequestDto request = new AuthUpdateRequestDto();
		request.setRegistrationId(registrationId);
		request.setUsername(username);
		request.setName(name);
		request.setEmail(email);
		request.setMobNumber(mobNumber);
		request.setGender(gender);
		request.setDob(LocalDate.parse(dob).atStartOfDay());
		request.setStatus(StatusEnum.PENDING);
		request.setGovIdFront(govIdFront.getBytes());
		request.setGovIdBack(govIdBack.getBytes());
		request.setPhoto(photo.getBytes());
		request.setProcessingStatus(ProcessingStatusEnum.HOLD);
		request.setRemark(remark);
		request.setUpdatedBy(AppConstants.USER);

		validator.validateDto(request);

		d.dbg("Incoming request: " + request.toString());

		Object user = authRegisterService.update(request);

		return new ResponseEntity<>(BookPortResponseBuilder.buildSuccessReponse(user), HttpStatus.OK);
	}

	@GetMapping("/pending")
	public ResponseEntity<Object> fetchPendingRequests(Authentication authentication) {

		SecurityUtil securityUtil = new SecurityUtil();

		if (!securityUtil.hasRoleAdmin(authentication)) {
			throw new AuthorizationDeniedException("User is not a Admin, only admin can access auth/v1/pending");
		}

		Object pendingRequests = authRegisterService.getPendingRequests();
		return new ResponseEntity<>(BookPortResponseBuilder.buildSuccessReponse(pendingRequests), HttpStatus.OK);
	}

	@GetMapping("/{id}/{side}/image")
	public ResponseEntity<byte[]> getImage(@PathVariable("id") String id, @PathVariable("side") String side)
			throws NoResourceFoundException, IOException {
		byte[] image = null;
		if ("front".equals(side)) {
			d.dbg("Fetching gov_id_front image.");
			image = authRegisterService.fetchFrontGovImg(id);
		} else if ("back".equals(side)) {
			d.dbg("Fetching gov_id_back image.");
			image = authRegisterService.fetchBackGovImg(id);
		} else if ("passport".equals(side)) {
			d.dbg("Fetching passport photo image.");
			image = authRegisterService.fetchPassportImg(id);
		} else {
			throw new NoResourceFoundException(HttpMethod.GET, id, "/auth/v1/register/" + id + "/" + side + "/image");
		}
		if (image == null) {
			throw new NoResourceFoundException(HttpMethod.GET, id, "/auth/v1/register/" + id + "/" + side + "/image");
		}

		String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(image));
		if (contentType == null) {
			d.dbg("contentType is null going to set here");
			contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
		}
		d.dbg("Returning Image");
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
	}

	@PostMapping("/processAuth")
	public ResponseEntity<Object> processAuth(@Valid @RequestBody RestRequestDto request, Authentication authentication) throws ValidationException {
		
		SecurityUtil securityUtil = new SecurityUtil();

		if (!securityUtil.hasRoleAdmin(authentication)) {
			throw new AuthorizationDeniedException("User is not a Admin, only admin can access auth/v1/pending");
		}

		d.dbg(request.toString());
		ProcessAuthRequstDto processAuthRequest = null;
		try {
			processAuthRequest = objectMapper.convertValue(request.getData(), ProcessAuthRequstDto.class);
		} catch(Exception e) {
			throw new ValidationException("Data must be of type ProcessAuthRequstDto");
		}

		validator.validateDto(processAuthRequest);
		Object response = null;
		
		request.setData(processAuthRequest);

		if ("APPROVE".equalsIgnoreCase(request.getAction())) {
			response = authRegisterService.approveRegistration(request);
		} else if ("REJECT".equalsIgnoreCase(request.getAction())
				|| "INFO_REQUIRED".equalsIgnoreCase(request.getAction())) {
			response = authRegisterService.rejectRegistration(request);
		} else {
			throw new ValidationException("Action is invalid.");
		}

		return new ResponseEntity<>(BookPortResponseBuilder.buildSuccessReponse(response), HttpStatus.OK);
	}


}
