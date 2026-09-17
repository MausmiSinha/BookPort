package com.example.BookPort.common.security;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.BookPort.common.logging.Debugger;

import jakarta.annotation.PostConstruct;

@Component
public class JwtKeyProvider {
	
	private Debugger d = new Debugger(this.getClass());
	
	@Value("${bookport.jwt.secret:}")
    private String secretKey;

    @Value("${security.jwt.expiry:7200000}")
    private int expiryInMs;

    @PostConstruct
    public void init() {
    	d.dbg("inside init");
        if (secretKey == null || secretKey.isBlank()) {
            try {
            	d.dbg("inside if");
                KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
                SecretKey sk = keyGen.generateKey();
                secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
                d.dbg("New Secret key generated is: " + secretKey);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Error generating JWT secret key", e);
            }
        }
    }

    public String getSecretKey() {
        return secretKey;
    }

    public int getExpiryInMs() {
        return expiryInMs;
    }

}
