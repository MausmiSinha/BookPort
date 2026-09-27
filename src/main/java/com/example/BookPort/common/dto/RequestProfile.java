package com.example.BookPort.common.dto;

import java.util.ArrayList;
import java.util.List;

public class RequestProfile {
	
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

	public RequestProfile(String userName, String userId, ArrayList<String> userRole) {
		super();
		this.userName = userName;
		this.userId = userId;
		this.userRole = userRole;
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
		return "RequestProfile [userName=" + userName + ", userId=" + userId + ", userRole=" + userRole + "]";
	}

}
