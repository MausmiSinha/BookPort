package com.example.BookPort.auth.entity;

import java.io.Serializable;
import java.util.Objects;

public class UserRolesPk implements Serializable {
	
	private String userId;
	
	private String roleId;

	public UserRolesPk() {
		
	}

	public UserRolesPk(String userId, String role_id) {
		super();
		this.userId = userId;
		this.roleId = role_id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRole_id() {
		return roleId;
	}

	public void setRole_id(String role_id) {
		this.roleId = role_id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(roleId, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserRolesPk other = (UserRolesPk) obj;
		return Objects.equals(roleId, other.roleId) && Objects.equals(userId, other.userId);
	}
	
	
	
	

}
