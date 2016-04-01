package com.orange.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserSelector {
	@Value("${enable:false}")
	private boolean useApplicationProperties;
	@Autowired
	@Qualifier("parserApplicationProperties")
	private ParserApplicationProperties parserApplicationProperties;

	@Bean
	@Qualifier("parserProperties")
	public ParserProperties parserProperties(){
		return useApplicationProperties ? parserApplicationProperties : new ParserSystemEnvironment(new Environment());
	}

}
