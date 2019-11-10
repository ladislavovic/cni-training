package com.cross_ni.training.springintroduction.examples03_autowiring.example2;

import com.cross_ni.training.helper.Examples;
import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.cross_ni.training.helper.SpringHelper.runInSpring;

import static org.junit.Assert.*;

public class Example2AutowiringTypes extends Examples {

	@Configuration
	@ComponentScan("com.cross_ni.training.springintroduction.examples03_autowiring.example2")
	public static class ContextConfiguration1 {
	}

	@Test
	public void autowiringTypes() {
		runInSpring(ContextConfiguration1.class, ctx -> {
			MyService myService = ctx.getBean(MyService.class);
			assertNotNull(myService.getDao1());
			assertNotNull(myService.getDao2());
		});
	}
	
}
