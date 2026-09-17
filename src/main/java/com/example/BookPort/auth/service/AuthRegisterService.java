package com.example.BookPort.auth.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.BookPort.auth.assembler.AuthAssembler;
import com.example.BookPort.auth.dto.ApproveRejectResponseDto;
import com.example.BookPort.auth.dto.AuthRegisterRequestDto;
import com.example.BookPort.auth.dto.AuthUpdateRequestDto;
import com.example.BookPort.auth.dto.PendingUserResponseDto;
import com.example.BookPort.auth.dto.ProcessAuthRequstDto;
import com.example.BookPort.auth.dto.RegistrationActionResponseDto;
import com.example.BookPort.auth.entity.AuthUserDetails;
import com.example.BookPort.auth.entity.RegistrationRequestAudit;
import com.example.BookPort.auth.entity.RegistrationRequestStageView;
import com.example.BookPort.auth.entity.Role;
import com.example.BookPort.auth.exceptions.RegistrationRequestNotFoundException;
import com.example.BookPort.auth.exceptions.RoleNotFoundException;
import com.example.BookPort.auth.exceptions.UsernameAlreadyExistsException;
import com.example.BookPort.auth.exceptions.ValidationException;
import com.example.BookPort.auth.repository.AuthUserDetailsRepo;
import com.example.BookPort.auth.repository.RegistrationRequestAuditRepo;
import com.example.BookPort.auth.repository.RegistrationRequestStageViewRepo;
import com.example.BookPort.auth.repository.RoleRepo;
import com.example.BookPort.common.constants.AppConstants;
import com.example.BookPort.common.constants.KafkaConstants;
import com.example.BookPort.common.dto.IntraRequest;
import com.example.BookPort.common.dto.ProcessingStatusEnum;
import com.example.BookPort.common.dto.RequestProfile;
import com.example.BookPort.common.dto.RestRequestDto;
import com.example.BookPort.common.dto.StatusEnum;
import com.example.BookPort.common.kafka.producer.KafkaProducerService;
import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.common.util.IdGenerator;
import com.example.BookPort.coreUser.entity.CoreUserDetail;
import com.example.BookPort.coreUser.repository.CoreUserDetailRepo;
import com.example.BookPort.notification.service.NotificationService;

import jakarta.transaction.Transactional;

@Service
public class AuthRegisterService {

	private Debugger d = new Debugger(this.getClass());

	@Autowired
	RegistrationRequestAuditRepo repo;

	@Autowired
	CoreUserDetailRepo coreUserRepo;

	@Autowired
	AuthUserDetailsRepo authUserDetailsRepo;

	@Autowired
	RoleRepo roleRepo;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	NotificationService notificationService;

	@Autowired
	KafkaProducerService kafkaProducerService;

	@Autowired
	RegistrationRequestStageViewRepo viewRepo;

	private AuthAssembler assembler = new AuthAssembler();

	@Transactional
	public Object register(AuthRegisterRequestDto request) {

		d.dbg("Checking " + request.getUsername() + " and " + request.getEmail() + " already exist in table or not.");
		RegistrationRequestStageView user = viewRepo.findByUsername(request.getUsername());
		if (user != null) {
			d.dbg(request.getUsername() + " exist in registration_request_stage_vw table.");
			throw new UsernameAlreadyExistsException(request.getUsername() + " is already taken.");
		}

		RegistrationRequestStageView userByEmail = viewRepo.findByEmail(request.getEmail());
		if (userByEmail != null) {
			d.dbg(request.getEmail() + " exist in registration_request_stage_vw table.");
			throw new UsernameAlreadyExistsException(request.getEmail() + " is already taken.");
		}

		d.dbg("No existingUser user with username " + request.getUsername() + "and email with " + request.getEmail()
				+ ". Going to create user.");

		RegistrationRequestAudit newUser = assembler.assembleRegistrationRequestAudit(request);

		// Set version
//		newUser.getId().setVersionNo(1);

		repo.save(newUser);

		d.dbg(request.getUsername() + " created successfully.");

		d.dbg("Going to post event in NOTIFICATION topic.");
		HashMap<String, Object> registerNotificationRequest = new HashMap<>();
		registerNotificationRequest.put("name", newUser.getName());
		registerNotificationRequest.put("username", newUser.getUsername());
		registerNotificationRequest.put("email", newUser.getEmail());
		registerNotificationRequest.put("registrationId", newUser.getRegistrationId());
		kafkaProducerService.postEvent(KafkaConstants.REGISTER_NOTIFICATION,
				new IntraRequest(new RequestProfile(MDC.get("username"), newUser.getUsername()),
						registerNotificationRequest, AppConstants.AUTH_MODULE_NAME));

		return new RegistrationActionResponseDto(
				"Request has been registered. Notification will be sent once request gets approved.",
				newUser.getRegistrationId(), newUser.getVersionNo(), newUser.getUsername(), newUser.getEmail(),
				newUser.getStatus(), newUser.getProcessingStatus());
	}

	public Object getPendingRequests() {
		Iterable<RegistrationRequestStageView> allUser = viewRepo.findByStatus();
		List<PendingUserResponseDto> allPendingUsers = new ArrayList<PendingUserResponseDto>();
		for (RegistrationRequestStageView u : allUser) {
			PendingUserResponseDto r = assembler.assemblerPendingUserResponseDto(u);
			allPendingUsers.add(r);
		}
		d.dbg("Users with pending status fetched.");
		return allPendingUsers;
	}

	public byte[] fetchFrontGovImg(String id) {
		return viewRepo.findGovIdFront(id);

	}

	public byte[] fetchBackGovImg(String id) {
		return viewRepo.findGovIdBack(id);
	}

	public byte[] fetchPassportImg(String id) {
		;
		return viewRepo.findPassportPhoto(id);
	}

	@Transactional
	public Object approveRegistration(RestRequestDto request) throws ValidationException {

		ProcessAuthRequstDto authRequest = (ProcessAuthRequstDto) request.getData();
		d.dbg("Approve request for registrationId: " + authRequest.getRegistrationId());

		RegistrationRequestAudit user = repo.findFirstByIdRegistrationIdOrderByIdVersionNoDesc(authRequest.getRegistrationId());

		if (user == null) {
			d.dbg("Registration request not found.");
			throw new RegistrationRequestNotFoundException("Registration request not found.");
		}
		if (user.getStatus() == StatusEnum.APPROVED || user.getStatus() == StatusEnum.REJECTED) {
			d.dbg("Registration request is already Approved or Rejected.");
			throw new UsernameAlreadyExistsException("Registration request is already Approved or Rejected.");
		}
		
		Role userRole = roleRepo.findByRoleName("USER");
		if (userRole == null) {
			throw new RoleNotFoundException("Default USER role not found.");
		}

		Set<Role> roles = new HashSet<>();
		roles.add(userRole);

		IdGenerator idGenerator = new IdGenerator(AppConstants.Module.USER_MODULE_ID);
		String userId = idGenerator.generateId();

		CoreUserDetail coreUser = new CoreUserDetail();
		coreUser.setUserId(userId);
		coreUser.setUsername(user.getUsername());
		coreUser.setName(user.getName());
		coreUser.setEmail(user.getEmail());
		coreUser.setMobileNumber(user.getMobNumber());

		if (user.getGender() != null) {
			coreUser.setGender(user.getGender().toString());
		}

		if (user.getDob() != null) {
			coreUser.setDob(user.getDob());
		}

		coreUser.setStatus(AppConstants.ACTIVE);
		coreUser.setCreatedAt(LocalDateTime.now());
		coreUser.setUpdatedAt(LocalDateTime.now());

		AuthUserDetails authUser = new AuthUserDetails();
		authUser.setUserId(userId);
		authUser.setUsername(user.getUsername());
		authUser.setPasswordHash(passwordEncoder.encode(user.getPassword()));
		authUser.setAccountNonExpired(true);
		authUser.setAccountNonLocked(true);
		authUser.setCredentialsNonExpired(true);
		authUser.setEnabled(true);
		authUser.setFailedAttempts(0);
		authUser.setCreatedAt(LocalDateTime.now());
		authUser.setUpdatedAt(LocalDateTime.now());
		authUser.setRoles(roles);

		coreUserRepo.save(coreUser);
		authUserDetailsRepo.save(authUser);

		RegistrationRequestAudit newAudit = new RegistrationRequestAudit();
		newAudit.setRegistrationId( user.getRegistrationId());
		newAudit.setUsername( user.getUsername());
		newAudit.setEmail( user.getEmail());
		newAudit.setVersionNo( user.getVersionNo() + 1);
		newAudit.setName( user.getName());
		newAudit.setMobNumber( user.getMobNumber());
		newAudit.setGender( user.getGender());
		newAudit.setDob( user.getDob());

		newAudit.setPassword( user.getPassword());
		newAudit.setStatus(authRequest.getStatus());

		newAudit.setGovIdFront(user.getGovIdFront());
		newAudit.setGovIdBack(user.getGovIdBack());
		newAudit.setPhoto(user.getPhoto());
		newAudit.setCreatedAt(user.getCreatedAt());
		newAudit.setUpdatedAt(LocalDateTime.now());
		newAudit.setUpdatedBy(AppConstants.ADMIN);
		newAudit.setProcessingStatus(ProcessingStatusEnum.COMPLETED);
		newAudit.setRemark(authRequest.getRemark());

		repo.save(newAudit);
		
		d.dbg("Going to post event in APPROVE NOTIF topic.");
		HashMap<String, Object> approveNotificationRequest = new HashMap<>();
		approveNotificationRequest.put("name", user.getName());
		approveNotificationRequest.put("username", user.getUsername());
		approveNotificationRequest.put("email", user.getEmail());
		approveNotificationRequest.put("userId", user.getRegistrationId());
		kafkaProducerService.postEvent(KafkaConstants.APPROVE_NOTIFICATION,
				new IntraRequest(new RequestProfile(MDC.get("username"), userId), approveNotificationRequest,
						AppConstants.AUTH_MODULE_NAME));

		d.dbg("Registration approved successfully for username: " + user.getUsername());

		return new ApproveRejectResponseDto("Registration approved successfully.", userId, StatusEnum.APPROVED);
	}

	@Transactional
	public Object rejectRegistration(RestRequestDto request) throws ValidationException {
		
		ProcessAuthRequstDto authRequest = (ProcessAuthRequstDto) request.getData();

		d.dbg("Request for registrationId: " + authRequest.getRegistrationId());
		
		String registrationId = authRequest.getRegistrationId();

		RegistrationRequestAudit existing = repo.findFirstByIdRegistrationIdOrderByIdVersionNoDesc(registrationId);

		if (existing == null) {
			d.dbg("Registration request not found.");
			throw new RuntimeException("Registration request not found.");
		}

		RegistrationRequestAudit newAudit = new RegistrationRequestAudit();

		newAudit.setRegistrationId(existing.getRegistrationId());
		newAudit.setUsername(existing.getUsername());
		newAudit.setEmail(existing.getEmail());
		newAudit.setVersionNo(existing.getVersionNo() + 1);
		newAudit.setName(existing.getName());
		newAudit.setMobNumber(existing.getMobNumber());
		newAudit.setGender(existing.getGender());
		newAudit.setDob(existing.getDob());
		newAudit.setCreatedAt(existing.getCreatedAt());
		newAudit.setUpdatedAt(LocalDateTime.now());
		newAudit.setPassword(existing.getPassword());
		newAudit.setStatus(authRequest.getStatus());

		newAudit.setGovIdFront(existing.getGovIdFront());
		newAudit.setGovIdBack(existing.getGovIdBack());
		newAudit.setPhoto(existing.getPhoto());
		newAudit.setUpdatedBy(AppConstants.ADMIN);
		newAudit.setProcessingStatus(authRequest.getStatus() == StatusEnum.REJECTED ? ProcessingStatusEnum.COMPLETED
				: ProcessingStatusEnum.HOLD);

		newAudit.setRemark(authRequest.getRemark());

		repo.save(newAudit);

		d.dbg("New audit version created: " + newAudit.getVersionNo());

		HashMap<String, Object> notificationRequest = new HashMap<>();

		notificationRequest.put("name", newAudit.getName());
		notificationRequest.put("username", newAudit.getUsername());
		notificationRequest.put("email", newAudit.getEmail());
		notificationRequest.put("userId", newAudit.getRegistrationId());
		notificationRequest.put("remark", newAudit.getRemark());

		kafkaProducerService.postEvent(KafkaConstants.REJECT_NOTIFICATION,
				new IntraRequest(new RequestProfile(MDC.get("username"), newAudit.getUsername()), notificationRequest,
						AppConstants.AUTH_MODULE_NAME));

		return new ApproveRejectResponseDto(
				authRequest.getStatus() == StatusEnum.REJECTED ? "Registration rejected successfully."
						: "Additional information is required.",
				newAudit.getRegistrationId(),authRequest.getStatus(),authRequest.getRemark());
	}

	public Object track(String id) {
		RegistrationRequestStageView user = null;

		if (id.matches("^\\d{19}$")) {

			d.dbg("Input identified as Registration Id: " + id);

			d.dbg("Searching by Registration Id : " + id);

			user = viewRepo.findByRegistrationId(id);

		} else if (id.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
			d.dbg("Input identified as Email: " + id);
			d.dbg("Searching by Email : " + id);

			user = viewRepo.findByEmail(id);

		} else {
			d.dbg("Input identified as Username: " + id);
			d.dbg("Searching by Username : " + id);

			user = viewRepo.findByUsername(id);
		}

		if (user == null) {
			d.dbg("No registration request found for : " + id);
			throw new RegistrationRequestNotFoundException("No registration request found for : " + id);
		}
		d.dbg("Registration request found successfully.");
		d.dbg("Preparing TrackResponseDto.");
		return assembler.assembleTrackResponseDto(user);
	}

	@Transactional
	public Object update(AuthUpdateRequestDto request) {
		d.dbg("Fetching latest registration request using Registration Id : " + request.getRegistrationId());
		RegistrationRequestAudit existingUser = repo
				.findFirstByIdRegistrationIdOrderByIdVersionNoDesc(request.getRegistrationId());
		if (existingUser == null) {
			d.dbg("Registration request not found.");

			throw new RegistrationRequestNotFoundException(
					"No registration request found for : " + request.getRegistrationId());
		}
		
		d.dbg("Registration request found.");
		d.dbg("Checking " + request.getEmail() + " already exist in table or not.");
		RegistrationRequestStageView userByEmail = viewRepo.findByEmail(request.getEmail());
		if (userByEmail != null) {
			d.dbg(request.getEmail() + " already taken.");
			throw new UsernameAlreadyExistsException(request.getEmail() + " is already taken.");
		}
		d.dbg("Current Status : " + existingUser.getStatus());
		if (existingUser.getStatus() != StatusEnum.INFO_REQUIRED && existingUser.getStatus() != StatusEnum.PENDING) {
			d.dbg("Registration request can only be updated when status is INFO_REQUIRED or PENDING.");
			throw new IllegalStateException(
					"Registration request can only be updated when status is INFO_REQUIRED or PENDING.");
		}
		d.dbg("Creating new audit version.");
		RegistrationRequestAudit audit = assembler.assembleUpdateRequestAudit(request);

//		audit.setRegistrationId(existingUser.getRegistrationId());
//		audit.setUsername(request.getUsername());
//	    audit.setEmail(request.getEmail());
		audit.setVersionNo(existingUser.getVersionNo() + 1);
		audit.setStatus(StatusEnum.PENDING);
		audit.setProcessingStatus(ProcessingStatusEnum.HOLD);
		audit.setUpdatedBy(request.getUsername());
	    audit.setPassword(existingUser.getPassword());
	    audit.setCreatedAt(existingUser.getCreatedAt());
//	    if (request.getGovIdFront() == null) {
//	        audit.setGovIdFront(existingUser.getGovIdFront());
//	    }
//
//	    if (request.getGovIdBack() == null) {
//	        audit.setGovIdBack(existingUser.getGovIdBack());
//	    }
//
//	    if (request.getPhoto() == null) {
//	        audit.setPhoto(existingUser.getPhoto());
//	    }

		d.dbg("Saving Version : " + audit.getVersionNo());
		repo.save(audit);
		d.dbg("New version saved successfully.");
		d.dbg("Publishing update notification.");
		HashMap<String, Object> updateNotificationRequest = new HashMap<>();

		updateNotificationRequest.put("registrationId", audit.getRegistrationId());

		updateNotificationRequest.put("username", audit.getUsername());

		updateNotificationRequest.put("email", audit.getEmail());

		updateNotificationRequest.put("name", audit.getName());

		updateNotificationRequest.put("versionNo", audit.getVersionNo());

		d.dbg("Going to post event in UPDATE_NOTIFICATION topic.");
		kafkaProducerService.postEvent(KafkaConstants.UPDATE_NOTIFICATION,
				new IntraRequest(new RequestProfile(MDC.get("username"), audit.getUsername()),
						updateNotificationRequest, AppConstants.AUTH_MODULE_NAME));

		d.dbg("Returning response.");
		return new RegistrationActionResponseDto("Registration request updated successfully.",
				audit.getRegistrationId(), audit.getVersionNo(), audit.getUsername(), audit.getEmail(),
				audit.getStatus(), audit.getProcessingStatus());
	}
	

	

}
