package com.example.BookPort.auth.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.BookPort.auth.dto.BookPortUserPrincipal;
import com.example.BookPort.auth.entity.AuthUserDetails;
import com.example.BookPort.auth.repository.AuthUserDetailsRepo;
import com.example.BookPort.common.logging.Debugger;

@Service
public class BookPortUserDetailsService implements UserDetailsService{
	
	private Debugger d = new Debugger(this.getClass());

	 @Autowired
	 private AuthUserDetailsRepo authUserDetailsRepo;
	 
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		d.dbg("Inside loadUserByUsername()");
		
		AuthUserDetails user = authUserDetailsRepo.findAuthUserDetailsByUsername(username);
		
		if (user == null) {
			d.dbg("No user found for username: " + username);
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
		
		d.dbg("User found with userId: " + user.getUserId());
        d.dbg("Creating BookPortUserPrincipal object");

        return new BookPortUserPrincipal(user);
	}
	
}
