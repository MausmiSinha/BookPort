package com.example.BookPort.common.util;

import java.util.List;

import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.BookPort.common.constants.AppConstants;
import com.example.BookPort.common.logging.Debugger;

public class SecurityUtil {
	
	private Debugger d = new Debugger(this.getClass());
	
	public boolean hasRoleAdmin(Authentication authentication) {
		// Logic to check if current user is admin or not:
		d.dbg("authentication Object:");
		d.dbg(authentication.toString());
		List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) authentication.getAuthorities();
		boolean isAdmin = false;
		for(SimpleGrantedAuthority au : authorities) {
			d.dbg("Current au"+ au.toString());
			d.dbg("au.getAuthority: "+au.getAuthority());
			if(au.getAuthority().equals(AppConstants.ADMIN)) {
				isAdmin = true;
			}
			d.dbg("-------------------------");
		}
		d.dbg("Is current User a Admin? "+isAdmin);
		return isAdmin;
	}

}
