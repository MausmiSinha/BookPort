package com.example.BookPort.user.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;

import com.example.BookPort.common.dto.OprationEnum;
import com.example.BookPort.common.dto.RoleEnum;
import com.example.BookPort.common.dto.StatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;

@Entity
@Table(name = "role_request")
@DynamicInsert
public class RoleRequest {
	
	@Id
    @Column(name = "request_id")
    private String requestId;
	
	@Column(name = "user_id")
    private String userId;
	
	@Column(name = "username")
    private String userName;
	
	@Column(name = "name")
    private String name;

    @Column(name = "user_email")
    private String userEmail;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleEnum role;
    
    @Enumerated(EnumType.STRING)
    @Column(name="operation")
    private OprationEnum operation;
    
    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private StatusEnum status;
    
    @Column(name="remark")
    private String remark;
    
    @Column(name="requested_by")
    private String requestedBy;
    
    @Column(name="created_at")
    private LocalDateTime createdAt;
    
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name="updated_by")
    private String updatedBy;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public RoleEnum getRole() {
		return role;
	}

	public void setRole(RoleEnum role) {
		this.role = role;
	}

	public OprationEnum getOperation() {
		return operation;
	}

	public void setOperation(OprationEnum operation) {
		this.operation = operation;
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

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
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


	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public String toString() {
		return "RoleRequest [requestId=" + requestId + ", userId=" + userId + ", userName=" + userName + ", name="
				+ name + ", userEmail=" + userEmail + ", role=" + role + ", operation=" + operation + ", status="
				+ status + ", remark=" + remark + ", requestedBy=" + requestedBy + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + ", updatedBy=" + updatedBy + "]";
	}



    
    

}
