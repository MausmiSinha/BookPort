package com.example.BookPort.user.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.example.BookPort.common.constants.ActionConstants;
import com.example.BookPort.common.constants.AppConstants;
import com.example.BookPort.common.constants.KafkaConstants;
import com.example.BookPort.common.dto.AuthRoleRequestDto;
import com.example.BookPort.common.dto.AuthRoleResponseDto;
import com.example.BookPort.common.dto.AuthorityRequestDto;
import com.example.BookPort.common.dto.IntraRequest;
import com.example.BookPort.common.dto.OprationEnum;
import com.example.BookPort.common.dto.RequestProfile;
import com.example.BookPort.common.dto.RoleEnum;
import com.example.BookPort.common.dto.StatusEnum;
import com.example.BookPort.common.exceptions.ValidationException;
import com.example.BookPort.common.kafka.producer.KafkaProducerService;
import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.common.util.IdGenerator;
import com.example.BookPort.user.assembler.CoreUserAssembler;
import com.example.BookPort.user.entity.CoreUserDetail;
import com.example.BookPort.user.entity.RoleRequest;
import com.example.BookPort.user.repository.CoreUserDetailRepo;
import com.example.BookPort.user.repository.RoleRequestRepo;

import jakarta.transaction.Transactional;
import tools.jackson.databind.ObjectMapper;

@Service
public class CoreUserRolesService {

	CoreUserAssembler assembler = new CoreUserAssembler();

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
	CoreUserDetailRepo coreUserDetailRepo;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Transactional
	public String createRoleRequests(AuthorityRequestDto data,RequestProfile requestProfile) throws ValidationException{
		if (data.getOperation() == null || data.getRole() == null) {
			d.dbg("Operation and role are required.");
			throw new ValidationException("Operation and role are required.");
	    }
		
		OprationEnum operation;
		try {
			 operation = OprationEnum.valueOf( data.getOperation().toUpperCase());
		}catch(Exception e) {
			 throw new ValidationException("Invalid operation: " + data.getOperation());
		}
		
		int created = 0;
		List<String> skipped = new ArrayList<>();
		for (String userId : data.getUserId()) {
			
			Optional<CoreUserDetail> coreUserOptional =coreUserRepo.findById(userId);
			 if (!coreUserOptional.isPresent()) {
				 d.dbg("User not found: " + userId);
				 throw new ValidationException("User not found: " + userId);
			 }
			 
			 boolean pendingUserExists =roleRequestRepo.existsByUserIdAndRoleAndOperationAndStatus( userId,data.getRole(),operation,StatusEnum.PENDING);
			 if (pendingUserExists) {
		            skipped.add(userId);
		            continue;
		        }
			 CoreUserDetail coreUser = coreUserOptional.get();
			 RoleRequest roleRequest = new RoleRequest();
			 IdGenerator idGenerator =new IdGenerator(AppConstants.Module.USER_MODULE_ID);
			 
			 String requestId = idGenerator.generateId();

		        roleRequest.setRequestId(requestId);
		        roleRequest.setUserId(userId);
		        roleRequest.setUserName(coreUser.getUsername());
		        roleRequest.setName(coreUser.getName());
		        roleRequest.setUserEmail(coreUser.getEmail());
		        roleRequest.setRole(data.getRole());
		        roleRequest.setOperation(operation);
		        roleRequest.setStatus(StatusEnum.PENDING);
		        roleRequest.setRemark(data.getRemark());
		        roleRequest.setRequestedBy(requestProfile.getUserName());
		        roleRequest.setCreatedAt(LocalDateTime.now());
		        roleRequest.setUpdatedAt(LocalDateTime.now());
		        roleRequest.setUpdatedBy(null);

		        roleRequestRepo.save(roleRequest);
		        
		        Map<String, Object> notification = new HashMap<>();
		        notification.put("name", roleRequest.getName());
		        notification.put("username", roleRequest.getUserName());
		        notification.put("email", roleRequest.getUserEmail());
		        notification.put("requestId", requestId);
		        notification.put("role", roleRequest.getRole());
		        notification.put("operation", roleRequest.getOperation());
		        notification.put("status", roleRequest.getStatus());
		        notification.put("remark", roleRequest.getRemark());
		        notification.put("createdAt", roleRequest.getCreatedAt());
		        
		        kafkaProducerService.postEvent(
		                KafkaConstants.REQUEST_ROLE_NOTIFICATION,
		                new IntraRequest(
		                        requestProfile,
		                        notification,
		                        AppConstants.USER_MODULE
		                )
		        );
		        
		        created++;
		        d.dbg("Role request created. requestId=" + requestId);
		}
		
		return "Requests created: " + created + "; duplicate pending requests skipped: " + skipped.size()+ (skipped.isEmpty() ? "" : "; userIds: " + skipped);
			
		
	}
	
	@Transactional
	public String processAdminDecisions(String action, AuthorityRequestDto data,  RequestProfile requestProfile) throws ValidationException{

		 
		 int published = 0;
		 List<String> skipped = new ArrayList<>();
		 
		 for (String requestId : data.getRequestId()) {
			 Optional<RoleRequest> optional =roleRequestRepo.findById(requestId);
			 if (!optional.isPresent()) {
				 skipped.add(requestId + " (not found)");
				 continue;
			 }
			 
			 RoleRequest roleRequest = optional.get();
			 if (roleRequest.getStatus() != StatusEnum.PENDING) {
		            skipped.add(requestId + " already processed.");
		            continue;
		        }
			 
			 AuthRoleRequestDto authRequest = new AuthRoleRequestDto();
		        authRequest.setRequestId(roleRequest.getRequestId());
		        authRequest.setUserId(roleRequest.getUserId());
		        authRequest.setAction(action);
		        authRequest.setOperation(roleRequest.getOperation());
		        authRequest.setRole(roleRequest.getRole());
		        authRequest.setRemark(data.getRemark());
		        
		        kafkaProducerService.postEvent(
		                KafkaConstants.AUTH_ROLE_REQUEST,
		                new IntraRequest(
		                        requestProfile,
		                        authRequest,
		                        AppConstants.USER_MODULE
		                )
		        );
		        
		        published++;
		        
		        d.dbg("Event published for requestId: " + requestId);

	}
		 
		 return "Events published: " + published+ "; skipped: " + skipped.size()+ (skipped.isEmpty() ? "" : "; details: " + skipped);
	}

	@Transactional
	public void processAuthorityResponse(IntraRequest request) throws ValidationException{

		d.dbg("Incoming authority response from Auth Module.");
		d.dbg(request.toString());
		AuthRoleResponseDto response = objectMapper.convertValue(request.getPayload(),AuthRoleResponseDto.class);
		
		 String requestId = response.getRequestId();
		 Optional<RoleRequest> roleRequestOptional = roleRequestRepo.findById(requestId);
		 
		 if(roleRequestOptional.isEmpty()) {
			 new IllegalArgumentException("Role request not found: " + requestId);
		 }
		 RoleRequest roleRequest = roleRequestOptional.get();
		    if (roleRequest.getStatus() != StatusEnum.PENDING) {
		        d.dbg("Request already processed: " + requestId);
		        return;
		    }
		    
		    //Validation
		    if (!roleRequest.getUserId().equals(response.getUserId())
		            || !roleRequest.getRole().equals(response.getRole())
		            || !roleRequest.getOperation().equals(response.getOperation())) {

		    	d.dbg("Auth response does not match request: " + requestId);
		        throw new ValidationException("Auth response does not match request: " + requestId);
		    }
		    
		    roleRequest.setStatus(response.getStatus());
		    roleRequest.setRemark(response.getRemark());
		    roleRequest.setUpdatedAt(LocalDateTime.now());
		    roleRequest.setUpdatedBy(request.getRequestProfile().getUserName());

		    roleRequestRepo.save(roleRequest);
		    
		    Map<String, Object> notification = new HashMap<>();
		    notification.put("name", roleRequest.getName());
		    notification.put("username", roleRequest.getUserName());
		    notification.put("email", roleRequest.getUserEmail());
		    notification.put("requestId", roleRequest.getRequestId());
		    notification.put("role", roleRequest.getRole());
		    notification.put("operation", roleRequest.getOperation());
		    notification.put("status", roleRequest.getStatus());
		    notification.put("remark", roleRequest.getRemark());
		    notification.put("updatedAt", roleRequest.getUpdatedAt());
		    
		    kafkaProducerService.postEvent(
		            KafkaConstants.AUTHORITY_RESPONSE,
		            new IntraRequest(
		                    request.getRequestProfile(),
		                    notification,
		                    AppConstants.USER_MODULE
		            )
		    );
		    
		    d.dbg("Role request updated: " + requestId);


	}
}
