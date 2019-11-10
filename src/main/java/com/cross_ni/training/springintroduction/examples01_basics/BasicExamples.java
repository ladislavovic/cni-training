package com.cross_ni.training.springintroduction.examples01_basics;

import com.cross_ni.training.helper.AgentManager;
import com.cross_ni.training.springintroduction.common.CommonBean;
import com.cross_ni.training.helper.Examples;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.util.Map;

import static com.cross_ni.training.helper.SpringHelper.runInSpring;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BasicExamples extends Examples {
	
	@Configuration
	public static class ContextConfiguration {
		
		@Bean
		public CommonBean bean1() {
			return new CommonBean("bean1Val");
		}
		
		@Bean
		public String stringBean1() {
			return "str1";
		}
		
		@Bean
		public String stringBean2() {
			return "str2";
		}
		
	}

	////////// example start //////////
	/**
	 * Creates Spring Context and get a bean from it.
	 */
	@Test
	public void springHelloWorld() {
		
		// Create the context
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			
			// Register configuration. Here is only one but you can register more configurations.
			ctx.register(ContextConfiguration.class);
			
			// Load registered configurations
			ctx.refresh();
			
			Assert.assertNotNull(ctx.getBean("bean1"));
		}		
	}

	////////// example start //////////
	/**
	 * Get bean from Spring Context by the bean name.
	 */
	@Test
	public void getBeanByName() {
		runInSpring(ContextConfiguration.class, ctx -> {
			CommonBean bean1 = (CommonBean) ctx.getBean("bean1");
			Assert.assertNotNull(bean1);
			assertEquals("bean1Val", bean1.getValue());
		});
	}

	////////// example start //////////
	/**
	 * Get bean from Spring Context by the bean name.
	 */
	@Test
	public void getBeanByType() {
		runInSpring(ContextConfiguration.class, ctx -> {
			CommonBean bean1 = ctx.getBean(CommonBean.class);
			Assert.assertNotNull(bean1);
			assertEquals("bean1Val", bean1.getValue());
		});
	}

	////////// example start //////////
	/**
	 * Get collection of beans from the context, not only one.
	 */
	@Test
	public void getBeanCollection() {
		runInSpring(ContextConfiguration.class, ctx -> {
			Map<String, String> beans = ctx.getBeansOfType(String.class);
			assertEquals(2, beans.size());
		});
	}

	////////// example start //////////
	@Configuration
	public static class ContextConfiguration2 {
		
		@Bean
		@Scope("singleton")
		public CommonBean singletonBean() {
			return new CommonBean();
		}
		
		@Bean
		@Scope("prototype")
		public CommonBean prototypeBean() {
			return new CommonBean();
		}
		
	}

	/**
	 * Singleton is created once, prototype per each calling
	 */
	@Test
	public void beanScopes() {
		runInSpring(ContextConfiguration2.class, ctx -> {
			Assert.assertSame(ctx.getBean("singletonBean"), ctx.getBean("singletonBean"));
			Assert.assertNotSame(ctx.getBean("prototypeBean"), ctx.getBean("prototypeBean"));
		});
	}

	////////// example start //////////
	@Configuration
	public static class ContextConfiguration3 {

		@Bean
		public CommonBean bean1() {
			return new CommonBean("val1");
		}

		@Bean
		@Primary
		public CommonBean bean2() {
			return new CommonBean("val2");
		}
		
		@Bean
		public String stbBean1() {
			return "str1";
		}
		
		@Bean
		public String stbBean2() {
			return "str2";
		}

	}

	/**
	 * Primary is used when you inject according to type.
	 * When there is more candidates, the primary one is selected.
	 * When you inject according to name the primary has effect.
	 * It has nothing to do with bean overriding.
	 */
	@Test
	public void primary() {
		runInSpring(ContextConfiguration3.class, ctx -> {
			assertEquals(ctx.getBean("bean2"), ctx.getBean(CommonBean.class));
			assertThrows(NoUniqueBeanDefinitionException.class, () -> ctx.getBean(String.class));
		});
	}

	////////// example start //////////
	public static boolean agent;
	
	@Configuration
	public static class ContextConfiguration4 {

		@Bean
		public CommonBean bean1() {
			agent = true;
			return new CommonBean("val1");
		}

	}

	/**
	 * By default, @Bean methods are eagerly instantiated at container bootstrap time.
	 */
	@Test
	public void eagerInstantiation() {
		assertFalse(agent);
		runInSpring(ContextConfiguration4.class, ctx -> {
			assertTrue(agent);
		});
	}
	
}
