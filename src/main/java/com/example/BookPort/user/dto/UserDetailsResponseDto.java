package com.example.BookPort.user.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UserDetailsResponseDto {
	
	 private String userId;
	 private String username;
	 private String name;
	 private String email;
	 private String mobileNumber;
	 private String gender;
	 private LocalDateTime dob;
	 private String status;
	 private LocalDateTime createdAt;
	 private LocalDateTime updatedAt;
	 private Boolean accountNonExpired;
	 private Boolean accountNonLocked;
	 private Boolean credentialsNonExpired;
	 private Boolean enabled;
	 private Integer failedAttempts;
	 private List<String> roles;
	 public String getUserId() {
		 return userId;
	 }
	 public void setUserId(String userId) {
		 this.userId = userId;
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
	 public String getMobileNumber() {
		 return mobileNumber;
	 }
	 public void setMobileNumber(String mobileNumber) {
		 this.mobileNumber = mobileNumber;
	 }
	 public String getGender() {
		 return gender;
	 }
	 public void setGender(String gender) {
		 this.gender = gender;
	 }
	 public LocalDateTime getDob() {
		 return dob;
	 }
	 public void setDob(LocalDateTime dob) {
		 this.dob = dob;
	 }
	 public String getStatus() {
		 return status;
	 }
	 public void setStatus(String status) {
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
	 public Boolean getAccountNonExpired() {
		 return accountNonExpired;
	 }
	 public void setAccountNonExpired(Boolean accountNonExpired) {
		 this.accountNonExpired = accountNonExpired;
	 }
	 public Boolean getAccountNonLocked() {
		 return accountNonLocked;
	 }
	 public void setAccountNonLocked(Boolean accountNonLocked) {
		 this.accountNonLocked = accountNonLocked;
	 }
	 public Boolean getCredentialsNonExpired() {
		 return credentialsNonExpired;
	 }
	 public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
		 this.credentialsNonExpired = credentialsNonExpired;
	 }
	 public Boolean getEnabled() {
		 return enabled;
	 }
	 public void setEnabled(Boolean enabled) {
		 this.enabled = enabled;
	 }
	 public Integer getFailedAttempts() {
		 return failedAttempts;
	 }
	 public void setFailedAttempts(Integer failedAttempts) {
		 this.failedAttempts = failedAttempts;
	 }
	 public List<String> getRoles() {
		 return roles;
	 }
	 public void setRoles(List<String> roles) {
		 this.roles = roles;
	 }
	 @Override
	 public String toString() {
		return "UserDetailsResponseDto [userId=" + userId + ", username=" + username + ", name=" + name + ", email="
				+ email + ", mobileNumber=" + mobileNumber + ", gender=" + gender + ", dob=" + dob + ", status="
				+ status + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", accountNonExpired="
				+ accountNonExpired + ", accountNonLocked=" + accountNonLocked + ", credentialsNonExpired="
				+ credentialsNonExpired + ", enabled=" + enabled + ", failedAttempts=" + failedAttempts + ", roleName="
				+ roles + "]";
	 }
	 
}
