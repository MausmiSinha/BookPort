package com.example.BookPort.auth.dto;

import java.time.LocalDateTime;
import java.util.Arrays;

import com.example.BookPort.common.dto.GenderEnum;
import com.example.BookPort.common.dto.ProcessingStatusEnum;
import com.example.BookPort.common.dto.StatusEnum;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AuthUpdateRequestDto {
	
	@NotBlank(message = "Registration Id is mandatory")
    @Size(max = 19, message = "Registration Id cannot exceed 19 characters")
    private String registrationId;
	
	@NotNull(message = "Version Number is mandatory")
    @Min(value = 1, message = "Version Number must be at least 1")
    @Max(value = 100, message = "Version Number cannot exceed 100")
    private Integer versionNo = 1;
	
	@NotBlank(message="Username is mandatory")
	@Size(min = 1, max = 100, message="Username must be between 1 to 100 characters")
	private String username;
	
	@NotBlank(message="Name is mandatory")
	@Size(min = 1, max = 500, message="Name must be between 1 to 500 characters")
	private String name;
	
	@NotBlank(message="Email is mandatory")
	@Size(min = 1, max = 100, message="Email must be between 1 to 100 characters")
	private String email;
	
	@NotBlank(message="Mobile number is mandatory")
	@Pattern(regexp = "^[0-9]{10}$", message = "Mobile Number must be a 10 digit number")
	private String mobNumber;
	
	@NotNull(message="Gender is mandatory")
	private GenderEnum gender;
	
	@NotNull(message = "Date of Birth is mandatory")
    private LocalDateTime dob;
	
	private StatusEnum status;
	
	private LocalDateTime updatedAt;
	
	@NotNull(message="Government Id front side is mandatory and should be image.")
	private byte[] govIdFront;
	
	@NotNull(message="Government Id back side is mandatory and should be image.")
	private byte[] govIdBack;
	
	
	@NotNull(message = "Photo is mandatory")
    private byte[] photo;

    private ProcessingStatusEnum processingStatus;

    @Size(max = 500, message = "Remark cannot exceed 500 characters")
    private String remark;

    @NotBlank(message = "Updated By is mandatory")
    @Size(max = 100, message = "Updated By cannot exceed 100 characters")
    private String updatedBy;

    //Getter Setter
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
		this.username = username.toUpperCase();
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

	public void setDob(LocalDateTime dateTime) {
		this.dob = dateTime;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public byte[] getGovIdFront() {
		return govIdFront;
	}

	public void setGovIdFront(byte[] govIdFront) {
		this.govIdFront = govIdFront;
	}

	public byte[] getGovIdBack() {
		return govIdBack;
	}

	public void setGovIdBack(byte[] govIdBack) {
		this.govIdBack = govIdBack;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
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

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy.toUpperCase();
	}
	

    public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}
	
	// To String
	@Override
	public String toString() {
		return "AuthRegisterRequestDto [registrationId=" + registrationId + ", versionNo=" + versionNo + ", username="
				+ username + ", name=" + name + ", email=" + email + ", mobNumber=" + mobNumber + ", gender=" + gender
				+ ", dob=" + dob + ", status=" + status + ", updatedAt=" + updatedAt
				+ ", govIdFront=" + Arrays.toString(govIdFront) + ", govIdBack="
				+ Arrays.toString(govIdBack) + ", photo=" + Arrays.toString(photo) + ", processingStatus="
				+ processingStatus + ", remark=" + remark + ", updatedBy=" + updatedBy + "]";
	}


	
	

}
