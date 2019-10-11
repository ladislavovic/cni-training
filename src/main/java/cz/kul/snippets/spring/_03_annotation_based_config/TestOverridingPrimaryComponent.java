package cz.kul.snippets.spring._03_annotation_based_config;

import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.context.annotation.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestOverridingPrimaryComponent {

    @Configuration
    public static class Config1 {

        @Bean
        @Primary
        public String a() {
            return Config1.class.getSimpleName();
        }

    }

    @Configuration
    @Import(Config1.class)
    public static class Config2 {

        @Bean
        public String a() {
            return Config2.class.getSimpleName();
        }

    }

    @Test
    public void primaryComponentCanBeOverriden() {
        SpringTestUtils.runInSpring(Config2.class, ctx -> {
            String beanA = (String) ctx.getBean("a");
            assertEquals(Config2.class.getSimpleName(), beanA);
        });
    }
    
    @Test
    public void beanOverridingCanChangePrimaryProperty() {
        SpringTestUtils.runInSpring(Config1.class, ctx -> {
            AnnotationConfigApplicationContext ctxRetyped = (AnnotationConfigApplicationContext) ctx;
            assertTrue(ctxRetyped.getBeanDefinition("a").isPrimary());
        });
        SpringTestUtils.runInSpring(Config2.class, ctx -> {
            AnnotationConfigApplicationContext ctxRetyped = (AnnotationConfigApplicationContext) ctx;
            assertFalse(ctxRetyped.getBeanDefinition("a").isPrimary());
        });
    }
    
}
