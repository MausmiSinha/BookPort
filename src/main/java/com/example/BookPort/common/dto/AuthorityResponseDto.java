package com.example.BookPort.common.dto;

import java.util.List;

public class AuthorityResponseDto {
	
	private List<String> requestId;

    private String status;

    private String message;

	public List<String> getRequestId() {
		return requestId;
	}

	public void setRequestId(List<String> requestId) {
		this.requestId = requestId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "AuthorityResponseDto [requestId=" + requestId + ", status=" + status + ", message=" + message + "]";
	}
    
    

}
