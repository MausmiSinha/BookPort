package com.example.BookPort.common.aspects;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.BookPort.common.dto.RequestProfile;
import com.example.BookPort.common.dto.RestRequestDto;
import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.common.security.JwtValidatorService;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class RestControllerAspect {
	
	@Autowired
	private JwtValidatorService jwtValidatorService;
	
	

	@Before("execution(* com.example.BookPort..controller..*(..))")
	public void defaultRequestProfile(JoinPoint joinPoint) {
		Debugger d = new Debugger(joinPoint.getTarget().getClass());
		RestRequestDto request = null;
		
		for(Object arg:joinPoint.getArgs()) {
			if(arg instanceof RestRequestDto) {
				request = (RestRequestDto) arg;
				break;
			}
		}
		
		if(request == null) {
			d.dbg("Request is not type of RestRequestDto. Skipping RequestProfile derivation.");
			return;
		}
		
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		
		if(attributes== null) {
			d.dbg("SSkipping RequestProfile derivation. Attributes are null.");
			return;
		}
		
		HttpServletRequest httpRequest = attributes.getRequest();
		d.dbg("Request URI: "+ httpRequest.getRequestURI());
		String path = httpRequest.getRequestURI();
		d.dbg("HTTP Method: "+ httpRequest.getMethod());
		
		RequestProfile profile = new RequestProfile();
		String authHeader = httpRequest.getHeader("Authorization");
		String token = null;
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			token= authHeader.substring(7);
			
			String userId= jwtValidatorService.extractCustomClaim(token, "userId", String.class);
			String userName= jwtValidatorService.extractUserName(token);
			ArrayList<String> role = jwtValidatorService.extractCustomClaim(token, "roles",ArrayList.class);
			profile.setUserId(userId);
			profile.setUserName(userName);
			profile.setUserRole(role);
			profile.setTraceId((String) MDC.get("traceId"));
			d.dbg("Request Profile populated successfully.");
			d.dbg(profile.toString());
		}
		request.setRequestProfile(profile);
	}
}
