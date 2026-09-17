package com.example.BookPort.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PasswordResetRequestDto {
	
	 	@NotBlank(message = "Username is mandatory")
	    private String username;

	    @NotBlank(message = "New password is mandatory")
	    @Pattern(
	        regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_]).{8,}$",
	        message = "Password must contain uppercase, lowercase, number, special character and minimum 8 characters."
	    )
	    private String newPassword;

	    @NotBlank(message = "Confirm password is mandatory")
	    private String confirmPassword;

	    public String getUsername() {
	        return username;
	    }

	    public String getNewPassword() {
	        return newPassword;
	    }

	    public String getConfirmPassword() {
	        return confirmPassword;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    public void setNewPassword(String newPassword) {
	        this.newPassword = newPassword;
	    }

	    public void setConfirmPassword(String confirmPassword) {
	        this.confirmPassword = confirmPassword;
	    }

}
