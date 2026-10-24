package com.example.BookPort.user.controller.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BookPort.common.constants.ActionConstants;
import com.example.BookPort.common.constants.AppConstants;
import com.example.BookPort.common.dto.AuthorityRequestDto;
import com.example.BookPort.common.dto.MetaDataDto;
import com.example.BookPort.common.dto.RequestProfile;
import com.example.BookPort.common.dto.RestRequestDto;
import com.example.BookPort.common.dto.RestResponseDto;
import com.example.BookPort.common.exceptions.ValidationException;
import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.common.util.BookPortResponseBuilder;
import com.example.BookPort.common.util.RequestBodyValidator;
import com.example.BookPort.user.dto.UserDetailsResponseDto;
import com.example.BookPort.user.service.CoreUserRolesService;
import com.example.BookPort.user.service.CoreUserService;

import jakarta.validation.Valid;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/user/v1")
public class UserController {

	private Debugger d = new Debugger(this.getClass());

	CoreUserService coreUserService;
	
	public void UserController(CoreUserService coreUserService) {
		this.coreUserService =coreUserService;
		
	}

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	CoreUserRolesService coreUserRolesService;
	
	@Autowired
	RequestBodyValidator authorityDtoValidator;

	@PostMapping("/users")
	public ResponseEntity<Object> fetchUser(@Valid @RequestBody RestRequestDto request, Authentication authentication)
			throws ValidationException {
		d.dbg("Inside fetchUser method.");
		d.dbg("Incoming request: ");
		d.dbg(request.toString());
		RequestProfile requestProfile = request.getRequestProfile();

		ArrayList<String> roles = requestProfile.getUserRole();
		HashMap<String, Object> queryParamMap = (HashMap<String, Object>) request.getQueryParam();
		d.dbg("Incoming Action: " + request.getAction());
		Object finalResponse = null;

		if ((ActionConstants.FECTH_USER).equals(request.getAction())) {
			UserDetailsResponseDto response;
			String userId = (String) queryParamMap.get("userId");
			d.dbg("Request for userId: " + userId);
			if (userId == null || userId.isEmpty()) {
				d.dbg("UserId cannot be null.");
				throw new ValidationException("UserId cannot be null.");
			}

			if (requestProfile.getUserId().equals(userId) && !requestProfile.getUserRole().contains(AppConstants.ADMIN)) {
				d.dbg("UserId in requestProfile and userId in payload are not same. Access Denied.");
				throw new ValidationException("Access Denied.");
			}
			response = coreUserService.fetchUser(userId, roles);

			d.dbg("Returning response.");
			d.dbg(response.toString());
			finalResponse = response;

		} else if (ActionConstants.FECTH_ALL_USER.equals(request.getAction())) {
			List<UserDetailsResponseDto> response;
			if (!requestProfile.getUserRole().contains(AppConstants.ADMIN)) {
				d.dbg("User is not a Admin, only admin can fetch all users.");
				throw new AuthorizationDeniedException("User is not a Admin, only admin can fetch all users.");
			}
			d.dbg("Request initiated by ADMIN.");
			// ObjectMapper used to convert the value returned by request.getQueryParam()
			// into a MetaDataDto object.
			HashMap<String, Integer> hm = (HashMap<String, Integer>) request.getQueryParam();
			int page = hm.containsKey("page") ? (int) hm.get("page") : 1;
			int size = hm.containsKey("size") ? (int) hm.get("size") : 10;
			// What if page is coming null or size is coming null?
			MetaDataDto responseMetaData = new MetaDataDto(page, size);
			response = coreUserService.fetchAllUsers(page, size, responseMetaData);

			return new ResponseEntity<>(BookPortResponseBuilder.buildSuccessReponse(response, responseMetaData),
					HttpStatus.OK);
		}
		d.dbg("Returning from fetchUser");
		return new ResponseEntity<>(BookPortResponseBuilder.buildSuccessReponse(finalResponse), HttpStatus.OK);

	}
	
	/***
	POST /user/v1/authorities is an endpoint for granting, rejecting and requesting roles for users and requesting roles by user or admin.
	***/
	@PostMapping("/authorities")
	public ResponseEntity<RestResponseDto> authorities(@Valid @RequestBody RestRequestDto request,
			Authentication authentication) throws ValidationException {
		d.dbg("Inside authorities method.");
		d.dbg("Incoming request: ");
		d.dbg(request.toString());
		
		RequestProfile requestProfile = request.getRequestProfile();
		
		if (request.getData() == null) {
		d.dbg("Data is required");
        throw new ValidationException("Data is required.");
		}
		

		// Only look for REQUEST, APPROVE, REJECT action. 
			String action = request.getAction();
			AuthorityRequestDto data = objectMapper.convertValue(request.getData(), AuthorityRequestDto.class);
			authorityDtoValidator.validateDto(data);
			String response = null;
			d.dbg("ACTION: " + action);
		    d.dbg("DATA: " + data);
		    
		    boolean isAdmin = requestProfile.getUserRole().contains(AppConstants.ADMIN);
		    /*
		     * REQUEST_ROLE
		     * A normal user can request a role only for themselves.
		     * Admin can request a role for another user.
		     */
		    if(ActionConstants.REQUEST.equals(action)) {
		    	if (data.getUserId() == null || data.getUserId().isEmpty()) {
		    		d.dbg("Userid is null or empty. At least one userId is required.");
		            throw new ValidationException("At least one userId is required.");
		        }
		    	boolean requestingForSelf = data.getUserId().contains(requestProfile.getUserId());
		    	d.dbg("isAdmin: "+ isAdmin);
		    	d.dbg("requestingForSelf: "+requestingForSelf);
		    	if((requestingForSelf && (data.getUserId().size()>1) && !isAdmin) || (!isAdmin && !requestingForSelf)) {
		    		 d.dbg(requestProfile.getUserId()+ " cannot request role for another user.");
		    		 throw new AuthorizationDeniedException(requestProfile.getUserId()+ " cannot request role for another user.");
		    	}
		    	d.dbg("Request initiated by: "+requestProfile.getUserId());
		    	response = coreUserRolesService.createRoleRequests(data,requestProfile);
		    	
		    } 
		    // Only ADMIN can approve or reject roles.
		    else if(ActionConstants.APPROVE.equals(action)|| ActionConstants.REJECT.equals(action)) {
		    	if(!isAdmin) {
		    		d.dbg("User is not an Admin. Only Admin can perform " + action+ ".");
		    		throw new AuthorizationDeniedException("User is not an Admin. Only Admin can perform " + action+ ".");
		    	}
		    	
		    	if (data.getRequestId() == null|| data.getRequestId().isEmpty()) {
		    		d.dbg("RequestId is null. At least one requestId is required.");
		            throw new ValidationException("At least one requestId is required.");
		        }
		    	
		    	d.dbg("Authority request initiated by ADMIN: "+ requestProfile.getUserName());
		    	response = coreUserRolesService.processAdminDecisions(action,data,requestProfile);
		    } else {
		    	d.dbg("Invalid action: " + action);
		    	throw new ValidationException("Invalid action: " + action);
		    }
			d.dbg("Returning from authorities method.");
		    return new ResponseEntity<>(BookPortResponseBuilder.buildSuccessReponse(response), HttpStatus.OK);
		    
		

	}

}
