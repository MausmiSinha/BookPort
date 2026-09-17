package com.example.BookPort.auth.dto;

import com.example.BookPort.common.dto.ProcessingStatusEnum;
import com.example.BookPort.common.dto.StatusEnum;
public class RegistrationActionResponseDto {
	
	private String message;
    private String registrationId;
    private Integer versionNo;
    private String username;
    private String email;
    private StatusEnum status;
    private ProcessingStatusEnum processingStatus;

	    
	    public RegistrationActionResponseDto(String string, String string2, String userId, String string3, String string4, StatusEnum approved, ProcessingStatusEnum completed) {
	    }
	    

		public RegistrationActionResponseDto(String message, String registrationId, Integer versionNo, String username,
				String email, StatusEnum status, ProcessingStatusEnum processingStatus) {
			super();
			this.message = message;
			this.registrationId = registrationId;
			this.versionNo = versionNo;
			this.username = username;
			this.email = email;
			this.status = status;
			this.processingStatus = processingStatus;
		}


		public String getMessage() {
			return message;
		}


		public void setMessage(String message) {
			this.message = message;
		}


		public String getRegistrationId() {
			return registrationId;
		}


		public void setRegistrationId(String registrationId) {
			this.registrationId = registrationId;
		}


		public Integer getVersionNo() {
			return versionNo;
		}


		public void setVersionNo(Integer versionNo) {
			this.versionNo = versionNo;
		}


		public String getUsername() {
			return username;
		}


		public void setUsername(String username) {
			this.username = username;
		}


		public String getEmail() {
			return email;
		}


		public void setEmail(String email) {
			this.email = email;
		}


		public StatusEnum getStatus() {
			return status;
		}


		public void setStatus(StatusEnum status) {
			this.status = status;
		}


		public ProcessingStatusEnum getProcessingStatus() {
			return processingStatus;
		}


		public void setProcessingStatus(ProcessingStatusEnum processingStatus) {
			this.processingStatus = processingStatus;
		}

		


}
