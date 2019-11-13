package com.cross_ni.training.springintroduction.examples05_security;

import org.springframework.security.access.prepost.PreAuthorize;

public class MyService {

	@PreAuthorize("hasAuthority('" + SecurityExample.AUTH_READ + "')")
	public void read() {
		
	}
	
	@PreAuthorize("hasAuthority('WRITE')")
	public void write() {
		
	}
	
}
