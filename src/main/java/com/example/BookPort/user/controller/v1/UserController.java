package com.example.BookPort.user.controller.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BookPort.common.constants.ActionConstants;
import com.example.BookPort.common.constants.AppConstants;
import com.example.BookPort.common.dto.MetaDataDto;
import com.example.BookPort.common.dto.RequestProfile;
import com.example.BookPort.common.dto.RestRequestDto;
import com.example.BookPort.common.exceptions.ValidationException;
import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.common.util.BookPortResponseBuilder;
import com.example.BookPort.user.dto.UserDetailsResponseDto;
import com.example.BookPort.user.service.CoreUserService;

import jakarta.validation.Valid;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/user/v1")
public class UserController {
	
	private Debugger d = new Debugger(this.getClass());
	
	@Autowired
	CoreUserService coreUserService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@PostMapping("/users")
	public ResponseEntity<Object> fetchUser(@Valid @RequestBody RestRequestDto request, Authentication authentication) throws ValidationException{
		d.dbg("Incoming request: ");
		d.dbg(request.toString());
		RequestProfile requestProfile = request.getRequestProfile();
		
		ArrayList<String> roles = requestProfile.getUserRole();
		HashMap<String, Object> queryParamMap = (HashMap<String,Object>) request.getQueryParam();
		d.dbg("Incoming Action: "+ request.getAction());
		Object finalResponse = null;
		
		if((ActionConstants.FECTH_USER).equals(request.getAction())) {
			UserDetailsResponseDto response;
			String userId= (String) queryParamMap.get("userId");
			d.dbg("Request for userId: "+ userId);
			if(userId == null || userId.isEmpty()) {
				d.dbg("UserId cannot be null.");
				throw new ValidationException("UserId cannot be null.");
			}
			
			if(requestProfile.getUserId() != userId && !requestProfile.getUserRole().contains(AppConstants.ADMIN)) {
				d.dbg("UserId in requestProfile and userId in payload are not same. Access Denied.");
				throw new ValidationException("Access Denied.");
			}
			response = coreUserService.fetchUser(userId,roles);
		
			d.dbg("Returning response.");
			d.dbg(response.toString());
			finalResponse = response;
			
		} else if (ActionConstants.FECTH_ALL_USER.equals(request.getAction())) {
			List<UserDetailsResponseDto> response;
			if(!requestProfile.getUserRole().contains(AppConstants.ADMIN)) {
				d.dbg("User is not a Admin, only admin can fetch all users.");
				throw new AuthorizationDeniedException("User is not a Admin, only admin can fetch all users.");
			}
			d.dbg("Request initiated by ADMIN.");
			//ObjectMapper used to convert the value returned by request.getQueryParam() into a MetaDataDto object.
			HashMap<String, Integer> hm = (HashMap<String, Integer>) request.getQueryParam();
			int page = hm.containsKey("page")? (int)hm.get("page") : 1;
			int size = hm.containsKey("size")? (int) hm.get("size") : 10;
			// What if page is coming null or size is coming null?
			MetaDataDto responseMetaData = new MetaDataDto(page,size);
			response = coreUserService.fetchAllUsers(page, size, responseMetaData);
			
			return new ResponseEntity<>(BookPortResponseBuilder.buildSuccessReponse(response, responseMetaData), HttpStatus.OK);
		}

		
		return new ResponseEntity<>(BookPortResponseBuilder.buildSuccessReponse(finalResponse), HttpStatus.OK);
		
		
	}

}
