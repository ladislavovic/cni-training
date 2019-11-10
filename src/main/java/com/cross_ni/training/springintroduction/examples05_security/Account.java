package com.cross_ni.training.springintroduction.examples05_security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Account {
	
	private String name;
	
	private String pswd;
	
	private List<String> authorities;

	public Account(String name, String pswd, String... authorities) {
		this.name = name;
		this.pswd = pswd;
		this.authorities = Arrays.asList(authorities);
	}

	public String getName() {
		return name;
	}

	public String getPswd() {
		return pswd;
	}

	public List<GrantedAuthority> getAuthorities() {
		return authorities.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}
}
