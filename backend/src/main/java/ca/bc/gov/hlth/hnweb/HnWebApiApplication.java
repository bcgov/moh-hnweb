package ca.bc.gov.hlth.hnweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "ca.bc.gov.hlth.hnweb.config", "ca.bc.gov.hlth.hnweb.controller", "ca.bc.gov.hlth.hnweb.service" })
public class HnWebApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HnWebApiApplication.class, args);
	}

}
