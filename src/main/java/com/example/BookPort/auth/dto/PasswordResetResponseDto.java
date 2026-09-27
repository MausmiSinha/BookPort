package com.example.BookPort.auth.dto;

import java.time.LocalDateTime;

public class PasswordResetResponseDto {
	
	private String message;
    private String username;
    private LocalDateTime updatedAt;

    public PasswordResetResponseDto() {
    }

    public PasswordResetResponseDto(String message, String username, LocalDateTime updatedAt) {
        this.message = message;
        this.username = username;
        this.updatedAt = updatedAt;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
