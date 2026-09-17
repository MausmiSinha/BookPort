package com.example.BookPort.common.dto;

public class IntraRequest {
	
	private RequestProfile requestProfile;
	
	private Object payload;
	
	private String requestingModule;
	

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

	@Override
	public String toString() {
		return "IntraRequest [payload=" + payload + ", requestingModule=" + requestingModule + "]";
	}

}
