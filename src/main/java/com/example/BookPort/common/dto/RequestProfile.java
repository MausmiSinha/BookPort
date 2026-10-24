package com.example.BookPort.common.dto;

import java.util.ArrayList;
import java.util.List;

public class RequestProfile {
	
	private String traceId;
	
	private String userName;
	
	private String userId;
	
	private ArrayList<String> userRole;

	public RequestProfile() {
		super();
	}
	
	public RequestProfile(String userName, String userId) {
		super();
		this.userName = userName;
		this.userId = userId;
	}

	public RequestProfile(String traceId, String userName, String userId, ArrayList<String> userRole) {
		super();
		this.traceId = traceId;
		this.userName = userName;
		this.userId = userId;
		this.userRole = userRole;
	}	

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public ArrayList<String> getUserRole() {
		return userRole;
	}

	public void setUserRole(ArrayList<String> userRole) {
		this.userRole = userRole;
	}

	@Override
	public String toString() {
		return "RequestProfile [traceId=" + traceId + ", userName=" + userName + ", userId=" + userId + ", userRole="
				+ userRole + "]";
	}

}
