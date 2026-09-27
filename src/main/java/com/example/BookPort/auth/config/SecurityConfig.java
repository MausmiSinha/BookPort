package com.example.BookPort.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.BookPort.auth.security.BookPortUserDetailsService;
import com.example.BookPort.common.logging.Debugger;

@Configuration
public class SecurityConfig {
	
	private Debugger d = new Debugger(this.getClass());
	
	@Autowired
    BookPortUserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
    	d.dbg("Inside authenticationProvider()");
        d.dbg("Creating DaoAuthenticationProvider");
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        d.dbg("Setting BCryptPasswordEncoder with strength 12");
        provider.setPasswordEncoder(passwordEncoder());
        d.dbg("AuthenticationProvider bean created successfully");
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    	d.dbg("Inside authenticationManager()");
    	AuthenticationManager authenticationManager =
                config.getAuthenticationManager();
    	d.dbg("Got hold on authenticationManager");
        return authenticationManager;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}
