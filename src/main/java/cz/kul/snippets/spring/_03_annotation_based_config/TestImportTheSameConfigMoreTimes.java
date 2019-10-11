package cz.kul.snippets.spring._03_annotation_based_config;

import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.Assert.assertEquals;

public class TestImportTheSameConfigMoreTimes {

    @Configuration
    public static class Config1 {

        @Bean
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

    @Configuration
    @Import({
            Config2.class,
            Config1.class
    })
    public static class Config3 {
    }

    @Test
    public void multipleImportIsIgnored() {
        SpringTestUtils.runInSpring(Config3.class, ctx -> {
            String beanA = (String) ctx.getBean("a");
            assertEquals(Config2.class.getSimpleName(), beanA);
        });
    }

}
