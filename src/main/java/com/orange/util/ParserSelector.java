package com.orange.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;

@Configuration
public class ParserSelector {
	@Autowired
	@Qualifier("parserApplicationProperties")
	private ParserApplicationProperties parserApplicationProperties;

	@Autowired
	private ConfigurableEnvironment env;
	
	@Bean
	@Qualifier("parserProperties")
	public ParserProperties parserProperties(){
		for (PropertySource<?> propertySource :  env.getPropertySources()) {
			if (propertySource instanceof CompositePropertySource && propertySource.getName() == "bootstrapProperties") {
				for (PropertySource<?> bootstrapPropertySource : ((CompositePropertySource)propertySource).getPropertySources()) {
					if (bootstrapPropertySource instanceof CompositePropertySource && bootstrapPropertySource.getName() == "configService") {
						if (((CompositePropertySource)bootstrapPropertySource).getPropertySources().size() != 0) {
							System.out.println("Using local or remote yaml configuration file to configurate the broker.");
							return parserApplicationProperties;
						}
					}
				}
			}
		}
		System.out.println("Using local environment variables to configurate the broker.");
		return new ParserSystemEnvironment(new Environment());
	}

}
