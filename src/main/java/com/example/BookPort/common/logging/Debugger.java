package com.example.BookPort.common.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class Debugger {
	
	private Logger log;
	
	private Class<?> clazz;
	
	public Debugger(Class<?> clazz) {
		super();
		this.clazz = clazz;
		log = LoggerFactory.getLogger(clazz);
	}

	public void dbg(String msg) {
		dbg(msg, Level.INFO, null);
		
	}
	
	public void dbg(String msg, Level level) {
		dbg(msg, level, null);
	}
	
	public void dbg(String msg, Level level, Exception e) {
		
		if(Level.INFO.equals(level)) {
			log.info(clazz.getSimpleName() +"--> " + msg);
		} else if(Level.WARN.equals(level)) {
			log.warn(clazz.getSimpleName() +"--> "+ msg, e);
		} else if(Level.ERROR.equals(level)) {
			log.warn(clazz.getSimpleName() +"--> "+ msg, e);
		} else if(Level.DEBUG.equals(level)) {
			log.debug(clazz.getSimpleName() +"--> "+msg);
		}
		
	}



}
