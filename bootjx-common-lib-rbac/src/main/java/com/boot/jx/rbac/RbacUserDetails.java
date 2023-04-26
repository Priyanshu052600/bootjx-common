package com.boot.jx.rbac;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class RbacUserDetails implements UserDetails {

    private static final long serialVersionUID = -5239211012237179966L;
    private final String username;
    private final String password;
    private final boolean active;
    private final Collection<? extends GrantedAuthority> authorities;

    public RbacUserDetails(String username, String password, boolean active,
	    Collection<? extends GrantedAuthority> authorities) {
	this.username = username;
	this.password = password;
	this.active = active;
	this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
	return authorities;
    }

    @Override
    public String getUsername() {
	return username;
    }

    @Override
    public String getPassword() {
	return password;
    }

    @Override
    public boolean isAccountNonExpired() {
	return active;
    }

    @Override
    public boolean isAccountNonLocked() {
	return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
	return active;
    }

    @Override
    public boolean isEnabled() {
	return active;
    }
}
