package com.example.BookPort.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.example.BookPort.auth.dto.AuthResponseDto;
import com.example.BookPort.auth.dto.BookPortUserPrincipal;
import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.common.security.JwtValidatorService;

@Service
public class AuthLoginService {
	private Debugger d = new Debugger(this.getClass());
	
	@Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    JwtValidatorService jwtValidatorService;

    public AuthResponseDto login(String username, String password) {
        d.dbg("Going to authenticate username: " + username);
        Authentication authentication = null;
        try {
	        authentication = authManager.authenticate(
	                new UsernamePasswordAuthenticationToken(username, password)
	        );
        } catch(BadCredentialsException ex) {
        	d.dbg("Failed to authenticate");
        	throw new BadCredentialsException("Username or password is incorrect");
        } catch(DisabledException ex) {
        	d.dbg("User account disabled");
        	throw new DisabledException("User is Disabled");
        }
        
        
        d.dbg("AuthenticationManager returned authenticated object.");
        
        d.dbg("Authentication successful for username: " + username);
        d.dbg("Going to extract principal from authentication object.");

        BookPortUserPrincipal principal = (BookPortUserPrincipal) authentication.getPrincipal();

        d.dbg("Principal extracted. userId: " + principal.getUserId()
        + ", username: " + principal.getUsername());
        
        d.dbg("Going to generate JWT token.");
        String token = jwtService.generateToken(principal);
        d.dbg("JWT token generated successfully.");

        AuthResponseDto response = new AuthResponseDto();
        response.setUserId(principal.getUserId());
        response.setUsername(principal.getUsername());
        response.setAccessToken(token);
        response.setExpiresAt(jwtValidatorService.extractExpirationAsLocalDateTime(token));

        d.dbg("Returning from login method.");
        return response;
    }

}
