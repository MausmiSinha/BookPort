package com.example.BookPort.common.dto;

import java.io.Serializable;

public class IntraRequest implements Serializable {
	
	private RequestProfile requestProfile;
	
	private Object payload;
	
	private String requestingModule;
	
	private String action;
	
	private Object queryParam;
	

	public IntraRequest() {
		super();
	}

	public IntraRequest(RequestProfile requestProfile, Object payload, String requestingModule) {
		super();
		this.requestProfile = requestProfile;
		this.payload = payload;
		this.requestingModule = requestingModule;
	}

	public RequestProfile getRequestProfile() {
		return requestProfile;
	}

	public void setRequestProfile(RequestProfile requestProfile) {
		this.requestProfile = requestProfile;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public String getRequestingModule() {
		return requestingModule;
	}

	public void setRequestingModule(String requestingModule) {
		this.requestingModule = requestingModule;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Object getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(Object queryParam) {
		this.queryParam = queryParam;
	}

	@Override
	public String toString() {
		return "IntraRequest [requestProfile=" + requestProfile + ", payload=" + payload + ", requestingModule="
				+ requestingModule + ", action=" + action + ", queryParam=" + queryParam + "]";
	}

}
