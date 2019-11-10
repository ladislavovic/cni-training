package com.cross_ni.training.springintroduction.examples06_contexthierarchy;

import com.cross_ni.training.springintroduction.common.CommonBean;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ContextHierarchyExamples {

	////////// example start //////////
	@Configuration
	public static class ParentContextConfiguration1 {

		@Bean
		public String parentContextBean() {
			return "parent";
		}

	}

	@Configuration
	public static class ChildContextConfiguration1 {

		@Bean
		public CommonBean childContextBean(String stringBean) {
			return new CommonBean(stringBean);
		}

	}

	/**
	 * Child context can refer parent beans.
	 * 
	 * For example dependency of child context bean can be bean from parent context.
	 */
	@Test
	public void childCanReferParentBeans() {
		try (AnnotationConfigApplicationContext parentContext = new AnnotationConfigApplicationContext()) {
			parentContext.register(ParentContextConfiguration1.class);
			parentContext.refresh();
			try (AnnotationConfigApplicationContext childContext = new AnnotationConfigApplicationContext()) {
				childContext.register(ChildContextConfiguration1.class);
				childContext.setParent(parentContext);
				childContext.refresh();

				assertEquals("parent", childContext.getBean(CommonBean.class).getValue());
			}

		}

	}

	////////// example start //////////
	@Configuration
	public static class ParentContextConfiguration2 {

		@Bean
		public String stringBean() {
			return "parent";
		}
		
	}

	@Configuration
	public static class ChildContextConfiguration2 {

		@Bean
		public CommonBean childContextBean(@Qualifier("stringBean") String stringBean) {
			return new CommonBean(stringBean);
		}
		
		@Bean
		public String stringBean() {
			return "child";
		}

	}

	/**
	 * Child bean with the same name hides the parent bean.
	 */
	@Test
	public void childBeanWithTheSameNameWins() {
		try (AnnotationConfigApplicationContext parentContext = new AnnotationConfigApplicationContext()) {
			parentContext.register(ParentContextConfiguration2.class);
			parentContext.refresh();
			try (AnnotationConfigApplicationContext childContext = new AnnotationConfigApplicationContext()) {
				childContext.register(ChildContextConfiguration2.class);
				childContext.setParent(parentContext);
				childContext.refresh();

				assertEquals("child", childContext.getBean(CommonBean.class).getValue());

			}
		}
	}

	////////// example start //////////
	@Configuration
	public static class ParentContextConfiguration3 {

		@Bean
		public String stringBeanParent() {
			return "parent";
		}

	}

	@Configuration
	public static class ChildContextConfiguration3 {

		@Bean
		public String stringBeanChild() {
			return "child";
		}

		@Bean
		public Set<String> allStringBeans(Set<String> stringBeans) {
			return stringBeans;
		}

	}

	/**
	 * ApplicationContext API is inconsistent when you have context hierarchy. Some methods consider only
	 * beans directly from the context, others consider also parent beans.
	 */
	@Test
	public void inconsistentBehaviour() {
		try (AnnotationConfigApplicationContext parentContext = new AnnotationConfigApplicationContext()) {
			parentContext.register(ParentContextConfiguration3.class);
			parentContext.refresh();
			try (AnnotationConfigApplicationContext childContext = new AnnotationConfigApplicationContext()) {
				childContext.register(ChildContextConfiguration3.class);
				childContext.setParent(parentContext);
				childContext.refresh();

				// here both child and parent beans are present
				assertEquals(Sets.newHashSet("child", "parent"), childContext.getBean(Set.class));

				// but here it returns only one bean
				assertEquals(1, childContext.getBeansOfType(String.class).size());

				// here again it behaves like there is only one bean, otherwise it would throw NoUniqueBeanDefinitionException
				assertEquals("child", childContext.getBean(String.class));
				
				// but here it returs also parent bean
				assertEquals("parent", childContext.getBean("stringBeanParent", String.class));
			}
		}
	}



}
