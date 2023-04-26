package com.boot.jx.rbac;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.boot.jx.rbac.docs.RbacPrivilege;
import com.boot.jx.rbac.docs.RbacRole;
import com.boot.jx.rbac.docs.RbacUser;

@Component
public class RbacUserDetailsService implements UserDetailsService {

    @Autowired(required = false)
    private RbacUserRepository userRepository;
    @Autowired(required = false)
    private RbacRoleRepository roleRepository;

    public RbacUser findByUsername(String username) {
	return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

	RbacUser userAccount = userRepository.findByUsername(username);
	if (userAccount == null) {
	    throw new UsernameNotFoundException("User with username [" + username + "] not found in the system");
	}
	Set<GrantedAuthority> authorities = getAuthorities(userAccount.getRoles());
	return new RbacUserDetails(userAccount.getUsername(), userAccount.getPassword(), userAccount.isEnabled(),
		authorities);
    }

    public Set<GrantedAuthority> getAuthorities(Set<RbacRole> roles) {
	Set<GrantedAuthority> authorities = new HashSet<>();
	for (RbacRole userRole : roles) {
	    authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRole()));
	    for (RbacPrivilege userRolePrivilege : userRole.getPrivileges()) {
		authorities.add(new SimpleGrantedAuthority(userRolePrivilege.getPrivilegeName()));
	    }
	}
	return authorities;
    }

    public void createUser(RbacUser user, String role) {
	user.setEnabled(user.isEnabled());
	RbacRole userRole = roleRepository.findByRole(role);
	user.setRoles(new HashSet<>(Arrays.asList(userRole)));
	userRepository.save(user);
    }

}
