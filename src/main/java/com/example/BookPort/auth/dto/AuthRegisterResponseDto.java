package com.example.BookPort.auth.dto;

public class AuthRegisterResponseDto {

    private String message;

    public AuthRegisterResponseDto() {
    }

    public AuthRegisterResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
