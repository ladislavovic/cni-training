package com.cross_ni.training.springintroduction.examples03_autowiring.example3;

import com.cross_ni.training.springintroduction.common.CommonBean;
import com.cross_ni.training.helper.Examples;
import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.cross_ni.training.helper.SpringHelper.runInSpring;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Example2 extends Examples {

	@Configuration
	@ComponentScan("com.cross_ni.training.springintroduction.examples03_autowiring.example3")
	public static class ContextConfiguration1 {
	}

	@Test
	public void configurationIsAlsoScanned() {
		runInSpring(ContextConfiguration1.class, ctx -> {
			assertEquals("foundByScan", ctx.getBean(CommonBean.class).getValue());
		});
	}
	
}
