package cz.kul.snippets.spring._03_annotation_based_config;

import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertNotNull;

public class TestComponentWithMoreNames {

    @Configuration
    @ComponentScan("cz.kul.snippets.spring._03_annotation_based_config") // TODO move all for this test to separated package because it scan more components that needed
    public static class Config { }

    @Test
    public void configWins() {
        SpringTestUtils.runInSpring(TestComponentWithMoreNames.Config.class, ctx -> {
            Bean1 bean1 = (Bean1) ctx.getBean("bean1");
            assertNotNull(bean1);
        });
    }

}
