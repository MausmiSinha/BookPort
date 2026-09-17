package com.example.BookPort.auth.dto;

import com.example.BookPort.common.dto.ProcessingStatusEnum;
import com.example.BookPort.common.dto.StatusEnum;

public class ApproveRejectResponseDto {
	
	private String message;
    private String userId;
    private StatusEnum status;
    private String remark;
    
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public ApproveRejectResponseDto(String message, String userId, StatusEnum status) {
		super();
		this.message = message;
		this.userId = userId;
		this.status = status;
	}
	public ApproveRejectResponseDto(String message, String userId, StatusEnum status, String remark) {
		super();
		this.message = message;
		this.userId = userId;
		this.status = status;
		this.remark = remark;
	}
	
	
    
    


    
}
