package com.cross_ni.training.springintroduction.examples03_autowiring.example1;

import com.cross_ni.training.helper.Examples;
import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.cross_ni.training.helper.SpringHelper.runInSpring;
import static org.junit.Assert.*;

public class Example1 extends Examples {

	@Configuration
	@ComponentScan("com.cross_ni.training.springintroduction.examples03_autowiring.example1")
	public static class ContextConfiguration1 {
	}

	@Test
	public void beansCreatedByComponentScan() {
		runInSpring(ContextConfiguration1.class, ctx -> {
			MyService myService = ctx.getBean(MyService.class);
			MyDao1 myDao1 = ctx.getBean(MyDao1.class);
			MyDao2 myDao2 = ctx.getBean(MyDao2.class);
			assertSame(myDao1, myService.getDao1());
			assertSame(myDao2, myService.getDao2());
		});
	}

}
