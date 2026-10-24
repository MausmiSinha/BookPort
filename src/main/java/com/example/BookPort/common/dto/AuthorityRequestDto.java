package com.example.BookPort.common.dto;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/*
 * Usage: used for posting request to /authorities endpoint
 * Payload:
 * 		{
 * 			"userId" : [ "",""],
 * 			"requestId" : [ "",""],
 * 			"role": "ADMIN",
 * 			"remark": "Admin Rights approved!"
 * 		}
 * 
 */

public class AuthorityRequestDto {
	
	private List<String> requestId;

	private List<String> userId;

	@NotNull(message = "Role is mandatory")
    private RoleEnum role;

    private String remark;
    
    private String operation;

	public List<String> getRequestId() {
		return requestId;
	}

	public void setRequestId(List<String> requestId) {
		this.requestId = requestId;
	}

	public RoleEnum getRole() {
		return role;
	}

	public void setRole(RoleEnum role) {
		this.role = role;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<String> getUserId() {
		return userId;
	}

	public void setUserId(List<String> userId) {
		this.userId = userId;
	}
	

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public String toString() {
		return "AuthorityRequestDto [requestId=" + requestId + ", userId=" + userId + ", role=" + role + ", remark="
				+ remark + ", operation=" + operation + "]";
	}


	
}
