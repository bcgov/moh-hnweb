package ca.bc.gov.hlth.hnweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.parser.Parser;

/**
 * Spring configuration file for the HN Web application 
 *
 */
@Configuration
public class HnWebConfig {

	@Bean
	public HapiContext hapiContext() {
		return new DefaultHapiContext();
	}
	
	@Bean
	public Parser parser() {
		return hapiContext().getGenericParser();
	}
}
