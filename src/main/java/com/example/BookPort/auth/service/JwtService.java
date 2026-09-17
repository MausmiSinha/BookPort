package com.example.BookPort.auth.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.example.BookPort.auth.dto.BookPortUserPrincipal;
import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.common.security.JwtKeyProvider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private Debugger d = new Debugger(this.getClass());
	
	@Autowired
    JwtKeyProvider keyProvider;

    public String generateToken(BookPortUserPrincipal user) {
    	
    	d.dbg("Inside generateToken()");
        d.dbg("Generating token for username: " + user.getUsername());
        
        Map<String, Object> claims = new HashMap<>();

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        d.dbg("Roles found: " + roles);
        
        claims.put("roles", roles);
        claims.put("userId", user.getUserId());
        
        d.dbg("JWT token generated successfully");
        
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + keyProvider.getExpiryInMs()))
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
    	d.dbg("Inside getKey()");
        byte[] keyBytes = Decoders.BASE64.decode(keyProvider.getSecretKey());
        d.dbg("SecretKey created successfully");
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
