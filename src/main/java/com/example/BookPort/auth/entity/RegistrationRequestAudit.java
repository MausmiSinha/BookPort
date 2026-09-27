package com.example.BookPort.auth.entity;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.hibernate.annotations.DynamicInsert;

import com.example.BookPort.common.dto.GenderEnum;
import com.example.BookPort.common.dto.ProcessingStatusEnum;
import com.example.BookPort.common.dto.StatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name="bptb_registration_request_audit")
@IdClass(RegistrationRequestAuditPk.class)
//@DynamicInsert - Allow sql to give default values.
@DynamicInsert

public class RegistrationRequestAudit {
	
	@Id
	@Column(name = "version_no")
    private Integer versionNo;

	@Id
    @Column(name = "registration_id")
    private String registrationId;

	@Id
    @Column(name = "username")
    private String username;

	@Id
    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "mobile_number")
    private String mobNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderEnum gender;

    @Column(name = "dob")
    private LocalDateTime dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "password_hash")
    private String password;

    @Column(name = "gov_id_front")
    private byte[] govIdFront;

    @Column(name = "gov_id_back")
    private byte[] govIdBack;

    @Column(name = "photo")
    private byte[] photo;

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status")
    private ProcessingStatusEnum processingStatus;

    @Column(name = "remark")
    private String remark;

    @Column(name = "updated_by")
    private String updatedBy;


	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		this.updatedBy = updatedBy;
	}

	@Override
	public String toString() {
		return "RegistrationRequestAudit [versionNo=" + versionNo + ", registrationId=" + registrationId + ", username="
				+ username + ", email=" + email + ", name=" + name + ", mobNumber=" + mobNumber + ", gender=" + gender
				+ ", dob=" + dob + ", status=" + status + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", password=" + password + ", govIdFront=" + Arrays.toString(govIdFront) + ", govIdBack="
				+ Arrays.toString(govIdBack) + ", photo=" + Arrays.toString(photo) + ", processingStatus="
				+ processingStatus + ", remark=" + remark + ", updatedBy=" + updatedBy + "]";
	}


    
	

}
