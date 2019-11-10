package com.cross_ni.training.springintroduction.examples02_configuration;

import com.cross_ni.training.springintroduction.common.CommonBean;
import com.cross_ni.training.helper.Examples;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static com.cross_ni.training.helper.SpringHelper.runInSpring;
import static org.junit.Assert.*;

public class ConfigurationExamples extends Examples {
	
	////////// example start //////////
	@Configuration
	public static class ContextConfiguration1 {
		
		@Bean
		public CommonBean bean1() {
			return new CommonBean();
		}
		
	}

	@Test
	public void beanNameIsTheSameAsMethodName() {
		runInSpring(ContextConfiguration1.class, ctx -> {
			CommonBean bean1 = (CommonBean) ctx.getBean("bean1");
			assertNotNull(bean1);
		});
	}

	////////// example start //////////
	@Configuration
	public static class ContextConfiguration2 {

		@Bean(name = "explictitNameBean")
		public CommonBean bean1() {
			return new CommonBean();
		}

	}

	@Test
	public void explicitBeanName() {
		runInSpring(ContextConfiguration2.class, ctx -> {
			assertNotNull(ctx.getBean("explictitNameBean"));
			assertThrows(NoSuchBeanDefinitionException.class, () -> ctx.getBean("bean1"));
		});
	}

	////////// example start //////////
	@Configuration
	public static class ContextConfiguration3 {

		@Bean
		public String str() {
			return "str";
		}
		
		@Bean
		public CommonBean bean1(String string) {
			return new CommonBean(string);
		}
		
	}

	@Test
	public void getDependecyFromFactoryMethodParameter() {
		runInSpring(ContextConfiguration3.class, ctx -> {
			assertEquals("str", ctx.getBean("bean1", CommonBean.class).getValue());
		});
	}

	////////// example start //////////
	@Configuration
	public static class ContextConfiguration4 {

		@Bean
		public String str1() {
			return "str1";
		}
		
		@Bean
		public String str2() {
			return "str2";
		}

		@Bean
		public CommonBean bean1(@Qualifier("str2") String string) {
			return new CommonBean(string);
		}

	}

	@Test
	public void qualifiedFactoryMehodParameter() {
		runInSpring(ContextConfiguration4.class, ctx -> {
			assertEquals("str2", ctx.getBean("bean1", CommonBean.class).getValue());
		});
	}

	////////// example start //////////
	private static int agent;
	
	@Configuration
	public static class ContextConfiguration5 {

		@Bean
		public String str() {
			agent++;
			return "str";
		}

		@Bean
		public CommonBean bean1() {
			return new CommonBean(str());
		}

		@Bean
		public CommonBean bean2() {
			return new CommonBean(str());
		}

	}

	/**
	 * Event though you call factory method multiple times, it is executed only once and
	 * you get the same instance. It is done by proxy.
	 */
	@Test
	public void getDependencyByFactoryMehodCalling() { // TODO factory method - is it the correct name?
		runInSpring(ContextConfiguration5.class, ctx -> {
			assertEquals(1, agent);
			CommonBean bean1 = ctx.getBean("bean1", CommonBean.class);
			CommonBean bean2 = ctx.getBean("bean2", CommonBean.class);
			assertSame(bean1.getValue(), bean2.getValue());
		});
	}

	////////// example start //////////
	@Configuration
	public static class ContextConfiguration6 {

		@Bean
		public CommonBean bean1() {
			return new CommonBean();
		}

	}
	
	@Configuration
	@Import(ContextConfiguration6.class)
	public static class ContextConfiguration7 {
	}
	
	@Test
	public void configurationImporting() {
		runInSpring(ContextConfiguration7.class, ctx -> {
			assertNotNull(ctx.getBean("bean1"));
		});
	}

	////////// example start //////////
	@Configuration
	public static class ContextConfiguration8 {

		@Bean
		public CommonBean bean1() {
			return new CommonBean("bean1_parent");
		}

	}

	@Configuration
	public static class ContextConfiguration9 extends ContextConfiguration8 {
		
		@Override
		@Bean
		public CommonBean bean1() {
			return new CommonBean("bean1_child");
		}

	}

	@Test
	public void configurationInheriting() {
		runInSpring(ContextConfiguration9.class, ctx -> {
			assertEquals("bean1_child", ctx.getBean("bean1", CommonBean.class).getValue());
		});
	}
	
}
