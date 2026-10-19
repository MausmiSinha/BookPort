package com.example.BookPort.common.dto;

import java.io.Serializable;


public class AuthRoleRequestDto implements Serializable{
	
	private String userId;
	private String requestId;
	private String action;
	private OprationEnum operation;
	private RoleEnum role;
	private String remark;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public OprationEnum getOperation() {
		return operation;
	}
	public void setOperation(OprationEnum operation) {
		this.operation = operation;
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
	
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	@Override
	public String toString() {
		return "AuthRoleRequestDto [userId=" + userId + ", requestId=" + requestId + ", action=" + action
				+ ", operation=" + operation + ", role=" + role + ", remark=" + remark + "]";
	}

	

}
