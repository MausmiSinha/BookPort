package com.example.BookPort.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(UserRolesPk.class)
@Table(name="user_roles")
public class UserRoles {
	
	@Id
	@Column(name="user_id")
	private String userId;
	
	@Id
	@Column(name="role_id")
	private String roleId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return "UserRoles [userId=" + userId + ", role_id=" + roleId + "]";
	}
	
	

}
