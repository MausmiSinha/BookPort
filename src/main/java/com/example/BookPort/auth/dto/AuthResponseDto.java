package com.example.BookPort.auth.dto;

import java.time.LocalDateTime;

public class AuthResponseDto {
	
	private String userId;
    private String username;
    private String accessToken;
    private LocalDateTime expiresAt;

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

}
