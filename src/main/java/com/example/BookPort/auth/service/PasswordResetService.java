package com.example.BookPort.auth.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.BookPort.auth.dto.PasswordResetRequestDto;
import com.example.BookPort.auth.dto.PasswordResetResponseDto;
import com.example.BookPort.auth.entity.AuthUserDetails;
import com.example.BookPort.auth.entity.PasswordHistory;
import com.example.BookPort.auth.exceptions.PasswordMismatchExceptio;
import com.example.BookPort.auth.exceptions.PasswordReuseException;
import com.example.BookPort.auth.exceptions.UserNotFoundException;
import com.example.BookPort.auth.repository.AuthUserDetailsRepo;
import com.example.BookPort.auth.repository.PasswordHistoryRepo;
import com.example.BookPort.common.logging.Debugger;

import jakarta.transaction.Transactional;


@Service
public class PasswordResetService {
	
	private Debugger d = new Debugger(this.getClass());
	
    @Autowired
    AuthUserDetailsRepo authUserDetailsRepo;

    @Autowired
    PasswordHistoryRepo passwordHistoryRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    public PasswordResetResponseDto resetPassword(PasswordResetRequestDto request){
        d.dbg("Inside resetPassword for username: " + request.getUsername());
        d.dbg("Checking if new password and confirm password match.");
        
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            d.dbg("New password and confirm password do not match.");
            throw new PasswordMismatchExceptio("New password and confirm password do not match.");
        }
        
        d.dbg("Password confirmation matched.");
        d.dbg("Fetching user by username: " + request.getUsername());
        AuthUserDetails user = authUserDetailsRepo.findAuthUserDetailsByUsername(request.getUsername());
        
        if (user == null) {
            d.dbg("User not found for username: " + request.getUsername());
            throw new UserNotFoundException("User not found.");
        }
        
        d.dbg("User found. userId: " + user.getUserId());
        d.dbg("Fetching current password hash.");
        String currentPasswordHash = user.getPasswordHash();
        
        d.dbg("Checking if new password is same as current password.");
        if (passwordEncoder.matches(request.getNewPassword(), currentPasswordHash)) {
            d.dbg("New password is same as current password.");
            throw new PasswordReuseException("New password cannot be same as current password.");
        }
        
        d.dbg("New password is not same as current password.");
        d.dbg("Fetching last three password history records for userId: " + user.getUserId());
        List<PasswordHistory> lastThreePasswords =
                passwordHistoryRepo.findTop3ByUserIdOrderByUpdatedAtDesc(user.getUserId());
        
        
        for (PasswordHistory passwordHistory : lastThreePasswords) {
            d.dbg("Checking password history id: " + passwordHistory.getPasswordHistoryId());

            d.dbg("Checking if new password matches latest password in history.");
            if (passwordEncoder.matches(request.getNewPassword(), passwordHistory.getLatestPassword())) {
                d.dbg("New password matched latest password in history.");
                throw new PasswordReuseException("New password cannot be same as last three passwords.");
            }

            d.dbg("Checking if old password exists in history.");
            if (passwordHistory.getOldPassword() != null
                    && passwordEncoder.matches(request.getNewPassword(), passwordHistory.getOldPassword())) {
                d.dbg("New password matched old password in history.");
                throw new PasswordReuseException("New password cannot be same as last three passwords.");
            }
            
            d.dbg("Password history id checked successfully: " + passwordHistory.getPasswordHistoryId());
        }
        
        d.dbg("New password did not match current password or last three passwords.");
        d.dbg("Encoding new password.");
        String newPasswordHash = passwordEncoder.encode(request.getNewPassword());
        
        d.dbg("Creating password history record.");
        PasswordHistory history = new PasswordHistory();
        history.setUserId(user.getUserId());
        history.setOldPassword(currentPasswordHash);
        history.setLatestPassword(newPasswordHash);
        history.setUpdatedAt(LocalDateTime.now());
        history.setIsReverted(false);

        d.dbg("Saving password history record.");
        passwordHistoryRepo.save(history);
        d.dbg("Password history record saved.");

        d.dbg("Updating user password hash.");
        user.setPasswordHash(newPasswordHash);
        user.setUpdatedAt(LocalDateTime.now());

        d.dbg("Saving updated user details.");
        authUserDetailsRepo.save(user);
        d.dbg("Updated user details saved.");

        d.dbg("Password reset successfully for username: " + user.getUsername());

        return new PasswordResetResponseDto(
                "Password reset successfully.",
                user.getUsername(),
                LocalDateTime.now()
        );
    }

}
