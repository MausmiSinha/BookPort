package com.example.BookPort.auth.dto;

import jakarta.validation.constraints.NotBlank;

/*
 * Sample Request:
 * Endpoint: /auth/v1/login
 * Body:
 {
	"username": "Johny Sins",
	"password": "Fuckme@123"
 }
 * 
 * 
 */

public class LoginRequestDto {
	
	@NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
