package com.example.BookPort.auth.service;

import java.util.Optional;

import javax.management.relation.RoleNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.example.BookPort.common.dto.IntraRequest;
import com.example.BookPort.common.dto.OprationEnum;
import com.example.BookPort.common.dto.RoleEnum;
import com.example.BookPort.common.dto.StatusEnum;
import com.example.BookPort.common.exceptions.ValidationException;
import com.example.BookPort.common.kafka.producer.KafkaProducerService;
import com.example.BookPort.common.logging.Debugger;

import tools.jackson.databind.ObjectMapper;

@Service
public class AuthRolesService {

	private Debugger d = new Debugger(this.getClass());

	@Autowired
	AuthUserDetailsRepo authUserDetailsRepo;

	@Autowired
	RoleRepo roleRepo;

	@Autowired
	UserRolesRepo userRoleRepo;

	@Autowired
	KafkaProducerService kafkaProducerService;

	@Autowired
	private ObjectMapper objectMapper;


	public void processAuthority(IntraRequest request) {

		d.dbg("Incoming authority request from User Module.");

		AuthRoleRequestDto dto = objectMapper.convertValue(request.getPayload(), AuthRoleRequestDto.class);

		String requestId = dto.getRequestId();
		String userId = dto.getUserId();
		String action = dto.getAction();
		OprationEnum operation = dto.getOperation();
		RoleEnum role = dto.getRole();

		AuthRoleResponseDto response = new AuthRoleResponseDto();

		response.setRequestId(requestId);
		response.setUserId(userId);
		response.setAction(action);
		response.setOperation(operation);
		response.setRole(role);
		response.setRemark(dto.getRemark());

		try {
			if (!ActionConstants.APPROVE.equals(action) && !ActionConstants.REJECT.equals(action)) {
				throw new ValidationException("Invalid action: " + action);
			}

			if (operation != OprationEnum.GRANT && operation != OprationEnum.REVOKE) {
				throw new ValidationException("Invalid operation: " + operation);
			}

			
			if (ActionConstants.REJECT.equals(action)) {

				response.setStatus(StatusEnum.REJECTED);

				response.setRemark(dto.getRemark() != null ? dto.getRemark(): "Role request rejected by Admin.");

			} else {

				Role roleEntity = roleRepo.findByRoleName(role.name());

				if (roleEntity == null) {
					d.dbg("Role not found: " + role);
					throw new RoleNotFoundException("Role not found: " + role);
				}

				String roleId = roleEntity.getRoleId();

				Optional<UserRoles> userRoleOptional = userRoleRepo.findByUserIdAndRoleId(userId, roleId);

				if (OprationEnum.GRANT.equals(operation)) {

					if (userRoleOptional.isPresent()) {
						response.setRemark("User already has " + role + " role.");
					} else {
						UserRoles userRole = new UserRoles();
						userRole.setUserId(userId);
						userRole.setRoleId(roleId);

						userRoleRepo.save(userRole);

						response.setRemark("Role granted successfully.");
					}
					response.setStatus(StatusEnum.APPROVED);

				} else {

					if (userRoleOptional.isEmpty()) {
						response.setStatus(StatusEnum.FAILURE);
						response.setRemark("User does not have " + role + " role.");
					} else {
						userRoleRepo.delete(userRoleOptional.get());

						response.setStatus(StatusEnum.APPROVED);
						response.setRemark("Role revoked successfully.");
					}
				}
			}

		} catch (Exception e) {
			d.dbg("Authority processing failed for requestId=" + requestId + ": " + e.getMessage());

			//operational failure, not an admin rejection.
			response.setStatus(StatusEnum.FAILURE);
			response.setRemark("Unable to process the role request.");
		}

		kafkaProducerService.postEvent(KafkaConstants.AUTH_ROLE_RESPONSE,
				new IntraRequest(request.getRequestProfile(), response, AppConstants.AUTH_MODULE_NAME));
	}

	private RoleEnum resolveRoleEnum(String roleName) {
		switch (roleName) {
		case "USER":
			return RoleEnum.USER;
		case "ADMIN":
			return RoleEnum.ADMIN;
		default:
			return RoleEnum.USER;
		}
	}

}
