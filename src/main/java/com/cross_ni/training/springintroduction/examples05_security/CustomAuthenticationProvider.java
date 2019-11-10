package com.cross_ni.training.springintroduction.examples05_security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	private Map<String, Account> accounts = new HashMap<>();
	
	public void addAccount(String name, String pswd, String... authorities) {
		accounts.put(name, new Account(name, pswd, authorities));
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Account account = accounts.get(authentication.getName());
		if (account != null) {
			if (account.getPswd().equals(authentication.getCredentials())) {
				return new UsernamePasswordAuthenticationToken(account.getName(), null, account.getAuthorities());
			} else {
				throw new BadCredentialsException("incorrect password");
			}
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}
}
