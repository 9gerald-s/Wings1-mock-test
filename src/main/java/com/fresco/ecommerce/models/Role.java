package com.fresco.ecommerce.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role {
	
	ROLE_CONSUMER, ROLE_SELLER

}

class RoleGrantedAuthority implements GrantedAuthority{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8651121408097829057L;

	String role;
	
	public RoleGrantedAuthority(String role) {
		this.role = role;
	}
	
	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return this.role;
	}
	
	
	
}
