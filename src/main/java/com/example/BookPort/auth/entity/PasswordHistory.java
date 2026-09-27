package com.example.BookPort.auth.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="password_history")
public class PasswordHistory {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "password_history_id")
	private String passwordHistoryId;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "old_password")
	private String oldPassword;
	
	@Column(name = "latest_password")
	private String latestPassword;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@Column(name = "is_reverted")
	private Boolean isReverted;
	
	@Column(name= "remark")
	private String remark;

	public String getPasswordHistoryId() {
		return passwordHistoryId;
	}

	public void setPasswordHistoryId(String passwordHistoryId) {
		this.passwordHistoryId = passwordHistoryId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getLatestPassword() {
		return latestPassword;
	}

	public void setLatestPassword(String latestPassword) {
		this.latestPassword = latestPassword;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Boolean getIsReverted() {
		return isReverted;
	}

	public void setIsReverted(Boolean isReverted) {
		this.isReverted = isReverted;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "PasswordHistory [passwordHistoryId=" + passwordHistoryId + ", userId=" + userId + ", oldPassword="
				+ oldPassword + ", latestPassword=" + latestPassword + ", updatedAt=" + updatedAt + ", isReverted="
				+ isReverted + ", remark=" + remark + "]";
	}
	
	

}
