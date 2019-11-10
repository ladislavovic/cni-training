package com.cross_ni.training.helper;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.function.Consumer;

public class SpringHelper {

	public static void runInSpring(Class<?> config, Consumer<AnnotationConfigApplicationContext> consumer) {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(config);
			ctx.refresh();
			consumer.accept(ctx);
		}
	}
	
}
