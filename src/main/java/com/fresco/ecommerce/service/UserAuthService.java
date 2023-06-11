package com.fresco.ecommerce.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fresco.ecommerce.models.User;
import com.fresco.ecommerce.repo.UserRepo;

@Service
public class UserAuthService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	AuthenticationManager authenticationManager;

	public User loadUserByUserID(Integer id) {
		Optional<User> user = userRepo.findById(id);
		if (user.isPresent()) {
			return user.get();
		} else {
			throw new UsernameNotFoundException("User ID not found");
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepo.findByUsername(username);
		if (user.isPresent()) {
//			return user.get();
			return new org.springframework.security.core.userdetails.User(username, user.get().getPassword(), user.get().getAuthorities());
		} else {
			throw new UsernameNotFoundException("User ID not found");
		}
	}
//	private Set<SimpleGrantedAuthority> getAuthorities(User user) {
//
//		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
//		user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.name())));
//		return authorities;
//		}
		
	public void authenticate(String username, String password) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (Exception e) {
		}
	}

}
