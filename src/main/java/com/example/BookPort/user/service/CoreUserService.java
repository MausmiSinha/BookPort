package com.example.BookPort.user.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.BookPort.auth.service.AuthService;
import com.example.BookPort.common.constants.ActionConstants;
import com.example.BookPort.common.constants.AppConstants;
import com.example.BookPort.common.dto.AuthUserDetailsDto;
import com.example.BookPort.common.dto.IntraRequest;
import com.example.BookPort.common.dto.MetaDataDto;
import com.example.BookPort.common.exceptions.UserNotFoundException;
import com.example.BookPort.common.exceptions.ValidationException;
import com.example.BookPort.common.kafka.producer.KafkaProducerService;
import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.user.assembler.CoreUserAssembler;
import com.example.BookPort.user.dto.UserDetailsResponseDto;
import com.example.BookPort.user.entity.CoreUserDetail;
import com.example.BookPort.user.repository.CoreUserDetailRepo;
import com.example.BookPort.user.repository.RoleRequestRepo;

import jakarta.transaction.Transactional;

@Service
public class CoreUserService {
	
	@Autowired
	private AuthService authService;
	
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
	
	
	@Transactional
	public UserDetailsResponseDto fetchUser(String userId, ArrayList<String> roles) {
		d.dbg("Fetching user from core table.");
		CoreUserDetail coreUser =coreUserRepo.findUserByUserId(userId);
		if(coreUser == null ) {
			d.dbg("User in core table not found.");
			throw new UserNotFoundException("User not found.");
		} 
		AuthUserDetailsDto authUser = authService.fetchAuthUserDetails(userId);

		d.dbg("Assembling UserDetailsResponseDto.");
		UserDetailsResponseDto assembleUserDetailsResponse = assembler.assembleUserDetails(coreUser, authUser);
		
		d.dbg("UserDetailsResponseDto: ");
		d.dbg(assembleUserDetailsResponse.toString());
		return assembleUserDetailsResponse;
		
	}

	@Transactional
	public List<UserDetailsResponseDto> fetchAllUsers(int page, int size, MetaDataDto responseMetaData) throws ValidationException {
		d.dbg("Fetching users. Page: " + page + ", Size: " + size);
		if (page < 1) {
			d.dbg("Page number must be greater than 0.");
			throw new ValidationException("Page number must be greater than 0.");
		}
		
		if (size <= 0) {
			d.dbg("Page size must be greater than 0.");
			throw new ValidationException("Page size must be greater than 0.");
		}
		Pageable pageable = PageRequest.of(page-1, size) ;
		d.dbg("Fetching users from Core table.");
		Page<CoreUserDetail> coreUserPage = coreUserRepo.findAll(pageable);
		responseMetaData.setTotalElements(coreUserPage.getTotalElements());
		responseMetaData.setTotalPages(coreUserPage.getTotalPages());
		if (coreUserPage.isEmpty()) {
			d.dbg("No users found.");
			throw new UserNotFoundException("No users found for requested page.");
		}
		List<CoreUserDetail> coreUsers = coreUserPage.getContent();
		List<String> userIds = new ArrayList<>();
		
		for (CoreUserDetail user : coreUsers) {
		    userIds.add(user.getUserId());
		}
		

		d.dbg("User IDs fetched from Core: " + userIds);
		
		IntraRequest intraRequest = new IntraRequest();
		intraRequest.setRequestingModule(AppConstants.USER_MODULE);
		intraRequest.setAction(ActionConstants.FECTH_ALL_USER);
		intraRequest.setQueryParam(userIds);
		
		d.dbg("Sending IntraRequest to Auth: " + intraRequest);
		
		
		List<AuthUserDetailsDto> authUsers = authService.fetchAuthUsers(intraRequest);
		d.dbg("Response received from auth: "+authUsers);
		
		List<UserDetailsResponseDto> response = new ArrayList<>();
		
		for(CoreUserDetail coreDetail: coreUsers) {
			d.dbg("Going to get Auth data for userId: "+coreDetail.getUserId());
			for(AuthUserDetailsDto authDetail: authUsers) {
				if(authDetail.getUserId().equals(coreDetail.getUserId())) {
					UserDetailsResponseDto assembleUserDetailsResponse = assembler.assembleUserDetails(coreDetail, authDetail);
					response.add(assembleUserDetailsResponse);
					break;
				}
			}
		}
		return response;
	}

	

}
