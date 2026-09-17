package com.example.BookPort.common.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.common.security.JwtFilter;

@Configuration
@EnableWebSecurity

/**@EnableMethodSecurity is used in Spring Security to enable method-level authorization.

It allows you to secure individual methods using annotations such as:

@PreAuthorize
@PostAuthorize
@Secured
@RolesAllowed

Without @EnableMethodSecurity, these annotations are ignored. **/
@EnableMethodSecurity
public class CommonSecurityConfig {
	
	private Debugger d = new Debugger(this.getClass());
	
	@Autowired
    JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
    	d.dbg("========================================");
        d.dbg("Inside securityFilterChain()");
        d.dbg("Configuring Spring Security");
        
        d.dbg("Disabling CSRF");
        http.csrf(csrf -> csrf.disable());

        d.dbg("Configuring authorization rules");
        d.dbg("Permitting: /auth/v1/register and /auth/v1/login. All other endpoints require authentication");
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/v1/register","/auth/v1/register/update" , "/auth/v1/login", "/auth/v1/track", "/dummy", "/auth/v1/register/test").permitAll()
                .anyRequest().authenticated()
        );

        d.dbg("Making HTTP Stateless.");
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        d.dbg("Add jwtFilter before UsernamePasswordAuthenticationFilter.");
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        d.dbg("Building SecurityFilterChain");
        return http.build();
    }

}
