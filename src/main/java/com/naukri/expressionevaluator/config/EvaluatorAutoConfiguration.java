package com.naukri.expressionevaluator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.naukri.expressionevaluator.service.ExpressionEvaluatorService;

@Configuration
@ComponentScan("com.naukri")
public class EvaluatorAutoConfiguration {

	@Bean
	public ExpressionEvaluatorService ExpressionEvaluatorLibrary() {
		return new ExpressionEvaluatorService();
	}
}