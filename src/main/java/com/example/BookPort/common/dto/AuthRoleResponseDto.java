package com.example.BookPort.common.dto;

import java.io.Serializable;

public class AuthRoleResponseDto implements Serializable{
	
	private OprationEnum operation;
	private String userId;
	private String action;
	private RoleEnum role;
	private String requestId;
	private StatusEnum status;
	private String remark;
	public OprationEnum getOperation() {
		return operation;
	}
	public void setOperation(OprationEnum authority) {
		this.operation = authority;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public RoleEnum getRole() {
		return role;
	}
	public void setRole(RoleEnum role) {
		this.role = role;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public StatusEnum getStatus() {
		return status;
	}
	public void setStatus(StatusEnum status) {
		this.status = status;
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
		return "AuthRoleResponseDto [operation=" + operation + ", userId=" + userId + ", action=" + action + ", role="
				+ role + ", requestId=" + requestId + ", status=" + status + ", remark=" + remark + "]";
	}


	
	
	

}
