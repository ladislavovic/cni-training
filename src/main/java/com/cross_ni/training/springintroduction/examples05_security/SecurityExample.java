package com.cross_ni.training.springintroduction.examples05_security;

import com.cross_ni.training.helper.Examples;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.cross_ni.training.helper.SpringHelper.runInSpring;
import java.util.ArrayList;
import java.util.List;

public class SecurityExample extends Examples {
	
	public static final String AUTH_READ = "READ";
	public static final String AUTH_WRITE = "WRITE";
	
	@Configuration
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	public static class ContextConfiguration1 {
		
		@Bean
		public CustomAuthenticationProvider provider1() {
			CustomAuthenticationProvider provider = new CustomAuthenticationProvider();
			provider.addAccount("user", "user", AUTH_READ);
			return provider;
		}
		
		@Bean
		public CustomAuthenticationProvider provider2() {
			CustomAuthenticationProvider provider = new CustomAuthenticationProvider();
			provider.addAccount("admin", "admin", AUTH_READ, AUTH_WRITE);
			return provider;
		}
		
		@Bean
		public AuthenticationManager authenticationManager(List<CustomAuthenticationProvider> providers) {
			return new ProviderManager(new ArrayList<>(providers));
		}
		
		@Bean
		public MyService myService() {
			return new MyService();
		}
		
	}
	
	private void authenticate(ApplicationContext ctx, String user, String pswd) {
		UsernamePasswordAuthenticationToken request = new UsernamePasswordAuthenticationToken(
				user,
				pswd);
		AuthenticationManager am = ctx.getBean(AuthenticationManager.class);
		Authentication response = am.authenticate(request);
		SecurityContextHolder.getContext().setAuthentication(response);
	}
	
	@Test
	public void example() {
		runInSpring(ContextConfiguration1.class, ctx -> {
			MyService myService = ctx.getBean(MyService.class);
			
			authenticate(ctx, "user", "user");
			myService.read();
			assertThrows(AccessDeniedException.class, () -> myService.write());
			
			authenticate(ctx, "admin", "admin"); // it is OK, it overrides previous call
			myService.read();
			myService.write();
			
			assertThrows(BadCredentialsException.class, () -> authenticate(ctx, "user", "wrongPassword"));
			
			assertThrows(ProviderNotFoundException.class, () -> authenticate(ctx, "notExistingUser", ""));
		});
	}
	
}
