package com.example.BookPort.auth.dto;

import java.io.Serializable;

import com.example.BookPort.common.dto.StatusEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProcessAuthRequstDto implements Serializable {
	
	@NotBlank(message = "registrationId cannot be blank")
	@NotNull(message = "registrationId is mandatory")
	private String registrationId;

	private String remark;
	
	private StatusEnum status;

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ProcessAuthRequstDto [registrationId=" + registrationId + ", remark=" + remark + ", status=" + status
				+ "]";
	}
	
}
