package com.example.BookPort.auth.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.BookPort.auth.entity.AuthUserDetails;
import com.example.BookPort.auth.entity.Role;
//Wraps AuthUserDetails into Spring Security’s UserDetails
public class BookPortUserPrincipal implements UserDetails{
	
	private AuthUserDetails user;
	
    public BookPortUserPrincipal(AuthUserDetails user) {
        this.user = user;
    }

    public String getUserId() {
        return user.getUserId();
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : user.getRoles()) {
            String roleName = role.getRoleName();


            authorities.add(new SimpleGrantedAuthority(roleName));
        }

        return authorities;
	}

	@Override
	public @Nullable String getPassword() {
		// TODO Auto-generated method stub
		 return user.getPasswordHash();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		 return user.getUsername();
	}
	
	@Override
    public boolean isAccountNonExpired() {
        return Boolean.TRUE.equals(user.getAccountNonExpired());
    }

    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE.equals(user.getAccountNonLocked());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return Boolean.TRUE.equals(user.getCredentialsNonExpired());
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getEnabled());
    }

}
