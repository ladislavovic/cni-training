package cz.kul.snippets.spring.common;

import java.util.function.Consumer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringTestUtils {

	public static void runInSpring(Class<?> config, Consumer<ApplicationContext> consumer) {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(config);
			ctx.refresh();
			consumer.accept(ctx);
		}
	}

	

   

	

	

}
