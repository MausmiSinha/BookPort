package com.example.BookPort.auth.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.example.BookPort.common.dto.GenderEnum;
import com.example.BookPort.common.dto.ProcessingStatusEnum;
import com.example.BookPort.common.dto.StatusEnum;

public class PendingUserResponseDto {
	
	private String registrationId;
    private String username;
    private String name;
    private String email;
    private String mobNumber;
    private GenderEnum gender;
    private LocalDateTime dob;
    private StatusEnum status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String password;
    private String govIdFront;
    private String govIdBack;
    private String photo;
    private ProcessingStatusEnum processingStatus;
    private String remark;
    
    // Getter Setter
	public String getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobNumber() {
		return mobNumber;
	}
	public void setMobNumber(String mobNumber) {
		this.mobNumber = mobNumber;
	}
	public GenderEnum getGender() {
		return gender;
	}
	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}
	public LocalDateTime getDob() {
		return dob;
	}
	public void setDob(LocalDateTime dob) {
		this.dob = dob;
	}
	public StatusEnum getStatus() {
		return status;
	}
	public void setStatus(StatusEnum status) {
		this.status = status;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGovIdFront() {
		return govIdFront;
	}
	public void setGovIdFront(String govIdFront) {
		this.govIdFront = govIdFront;
	}
	public String getGovIdBack() {
		return govIdBack;
	}
	public void setGovIdBack(String govIdBack) {
		this.govIdBack = govIdBack;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public ProcessingStatusEnum getProcessingStatus() {
		return processingStatus;
	}
	public void setProcessingStatus(ProcessingStatusEnum processingStatus) {
		this.processingStatus = processingStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	// To String
	@Override
	public String toString() {
		return "PendingUserResponseDto [registrationId=" + registrationId + ", username=" + username + ", name=" + name
				+ ", email=" + email + ", mobNumber=" + mobNumber + ", gender=" + gender + ", dob=" + dob + ", status="
				+ status + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", password=" + password
				+ ", govIdFront=" + govIdFront + ", govIdBack=" + govIdBack + ", photo=" + photo + ", processingStatus="
				+ processingStatus + ", remark=" + remark + "]";
	}
    
   
	
    
}
