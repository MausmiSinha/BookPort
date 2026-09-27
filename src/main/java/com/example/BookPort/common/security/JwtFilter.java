package com.example.BookPort.common.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.BookPort.common.logging.Debugger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	 private Debugger d = new Debugger(this.getClass());

	@Autowired
    JwtValidatorService jwtValidatorService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
    	
        d.dbg("Inside JwtFilter");
        d.dbg("Request URI: " + request.getRequestURI());
        d.dbg("Request Method: " + request.getMethod());

        try {
            String authHeader = request.getHeader("Authorization");
            d.dbg("Authorization Header: " + authHeader);
            if (authHeader != null
                    && authHeader.startsWith("Bearer ")
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
            	d.dbg("Bearer token found and no existing authentication present");
                String token = authHeader.substring(7);

                if (jwtValidatorService.validateToken(token)) {
                    String userId = jwtValidatorService.extractCustomClaim(token, "userId", String.class);
                    String username = jwtValidatorService.extractUserName(token);

                    List<String> roles = jwtValidatorService.extractCustomClaim(token, "roles", List.class);

                    d.dbg("User ID extracted: " + userId);
                    d.dbg("Username extracted: " + username);
                    d.dbg("Roles extracted: " + roles);
                    
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    JwtUserPrincipal principal = new JwtUserPrincipal(userId, username);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(principal, null, authorities);

                    d.dbg("UsernamePasswordAuthenticationToken created");
                    
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    d.dbg("Authentication stored in SecurityContext");
                    d.dbg("Authenticated User: " + username);
                }
            }
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
