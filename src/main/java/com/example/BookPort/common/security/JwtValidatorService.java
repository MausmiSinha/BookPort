package com.example.BookPort.common.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BookPort.common.logging.Debugger;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtValidatorService {

	@Autowired
    JwtKeyProvider keyProvider;
	
	private Debugger d = new Debugger(this.getClass());

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(keyProvider.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        try {
        	d.dbg("Inside validateToken");
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
        	d.dbg("Exception: ", Level.ERROR, e);
            return false;
        }
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public LocalDateTime extractExpirationAsLocalDateTime(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);

        return expiration.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public <T> T extractCustomClaim(String token, String claimKey, Class<T> type) {
        return extractClaim(token, claims -> claims.get(claimKey, type));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
