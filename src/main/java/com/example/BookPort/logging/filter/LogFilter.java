package com.example.BookPort.logging.filter;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.BookPort.common.logging.Debugger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LogFilter extends OncePerRequestFilter{
	
	private Debugger d = new Debugger(this.getClass());

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
			String traceId = UUID.randomUUID().toString();
			if(MDC.get("traceId") == null || "".equals(MDC.get("traceId")))
				MDC.put("traceId", traceId);
			
		} catch(Exception e) {
			d.dbg("Exception: ", Level.ERROR, e);
		} finally {
			filterChain.doFilter(request, response);
			MDC.clear();
		}
		
	}
	

}
