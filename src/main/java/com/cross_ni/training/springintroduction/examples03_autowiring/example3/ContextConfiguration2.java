package com.cross_ni.training.springintroduction.examples03_autowiring.example3;

import com.cross_ni.training.springintroduction.common.CommonBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextConfiguration2 {
	
	@Bean
	public CommonBean bean1() {
		return new CommonBean("foundByScan");
	}
	
}
