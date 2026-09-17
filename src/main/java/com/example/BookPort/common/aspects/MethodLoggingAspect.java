package com.example.BookPort.common.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.example.BookPort.common.logging.Debugger;


@Aspect
@Component
public class MethodLoggingAspect {
	
	private Debugger d;

	@Around(
			"execution(* com.example.BookPort..*(..)) " +
				    "&& !within(com.example.BookPort.common.aspects.MethodLoggingAspect) " +
				    "&& !within(com.example.BookPort.common.security.JwtFilter)")
	//exclude the aspect class itself to avoid accidental recursion/noisy logging.
    public Object logMethodStartAndEnd(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        d = new Debugger(signature.getDeclaringType());

        d.dbg("Inside " + methodName + ".");

        try {
            return joinPoint.proceed();
        } finally {
        	d.dbg("Returning from " + methodName + ".");
        }
    }

}
