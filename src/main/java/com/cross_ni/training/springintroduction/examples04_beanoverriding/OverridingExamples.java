package com.cross_ni.training.springintroduction.examples04_beanoverriding;

import com.cross_ni.training.helper.Examples;
import com.cross_ni.training.springintroduction.common.CommonBean;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static com.cross_ni.training.helper.SpringHelper.runInSpring;
import static org.junit.Assert.*;

public class OverridingExamples extends Examples {

	////////// example start //////////
	@Configuration
	public static class ContextConfiguration1 {
		
		@Bean
		public CommonBean bean1() {
			return new CommonBean();
		}
		
	}

	/**
	 * Spring has {@link BeanDefinition} object for each bean it can create.
	 */
	@Test
	public void beanDefinition() {
		runInSpring(ContextConfiguration1.class, ctx -> {
			BeanDefinition def = ctx.getBeanDefinition("bean1");
			Assert.assertEquals("overridingExamples.ContextConfiguration1", def.getFactoryBeanName());
			Assert.assertEquals("bean1", def.getFactoryMethodName());
			Assert.assertEquals(false, def.isPrimary());
			Assert.assertEquals(true, def.isSingleton());
		});
	}

	////////// example start //////////
	private static boolean agent;
	
	@Configuration
	public static class ContextConfiguration2 {

		@Bean
		public CommonBean bean1() {
			agent = true;
			return new CommonBean("val1");
		}

	}

	@Configuration
	@Import(ContextConfiguration2.class)
	public static class ContextConfiguration3 {

		@Bean
		public CommonBean bean1() {
			return new CommonBean("val2");
		}

	}

	@Test
	public void overridignOfBeanFromImportedConfiguration() {
		runInSpring(ContextConfiguration3.class, ctx -> {
			assertFalse(agent);
			assertEquals(1, ctx.getBeansOfType(CommonBean.class).size());
			assertEquals("val2", ctx.getBean(CommonBean.class).getValue());
		});
	}
	
}
