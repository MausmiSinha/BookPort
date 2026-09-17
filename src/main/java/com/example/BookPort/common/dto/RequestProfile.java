package com.example.BookPort.common.dto;

public class RequestProfile {
	
	private String userName;
	
	private String userId;

	public RequestProfile() {
		super();
	}

	public RequestProfile(String userName, String userId) {
		super();
		this.userName = userName;
		this.userId = userId;
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

	@Override
	public String toString() {
		return "RequestProfile [userName=" + userName + ", userId=" + userId + "]";
	}

}
