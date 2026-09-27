package com.example.BookPort.common.security;

public class JwtUserPrincipal {
	
	private String userId;
    private String username;

    public JwtUserPrincipal(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

	@Override
	public String toString() {
		return "JwtUserPrincipal [userId=" + userId + ", username=" + username + "]";
	}

    
}
