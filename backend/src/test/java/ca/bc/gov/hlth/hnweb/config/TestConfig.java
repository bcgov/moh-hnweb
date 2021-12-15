package ca.bc.gov.hlth.hnweb.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import ca.bc.gov.hlth.hnweb.exception.HNWebException;
import reactor.netty.http.client.HttpClient;

//@TestConfiguration
public class TestConfig {

	@Primary
	@Bean("rapidWebClient")
    public WebClient rapidWebClient() throws HNWebException {
		System.out.println("Loadind testConfig");
	    HttpClient httpClient= HttpClient.create();
		ClientHttpConnector connector= new ReactorClientHttpConnector(httpClient);
	    
		return WebClient.builder()
	    		.clientConnector(connector)
                .baseUrl("")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE) 
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.ALL_VALUE) 
                .defaultHeaders(header -> header.setBasicAuth("", ""))
                .build();
    }
}
