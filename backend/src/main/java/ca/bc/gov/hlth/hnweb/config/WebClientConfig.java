package ca.bc.gov.hlth.hnweb.config;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

	private final Logger logger = LoggerFactory.getLogger("WebClientConfig");

	private static final String KEY_STORE_TYPE_PKCS12 = "pkcs12";

	private static final String KEY_MANAGER_FACTORY_TYPE_SUN_X509 = "SunX509";

	@Value("${R50.url}")
	private String r50Url;
	 
	@Value("${R50.user.name}")
	private String userName;

	@Value("${R50.user.password}")
	private String userPassword;

	@Value("classpath:${R50.cert.file}")
	private Resource certFile;

	@Value("${R50.cert.password}")
	private String certPassword;

	@Bean("enrollmentWebClient")
    public WebClient enrollmentWebClient() {

	    HttpClient httpClient= HttpClient.create().secure(t -> t.sslContext(getSSLContext()));
		ClientHttpConnector connector= new ReactorClientHttpConnector(httpClient);
	    
		return WebClient.builder()
	    		.clientConnector(connector)
                .baseUrl(r50Url)
                .filter(logRequest())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE) 
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.ALL_VALUE) 
                .defaultHeaders(header -> header.setBasicAuth(userName, userPassword))
                .build();
    }

	private SslContext getSSLContext() {

	    try {
	        KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_PKCS12);
	        keyStore.load(new FileInputStream(certFile.getFile()), certPassword.toCharArray());

	        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KEY_MANAGER_FACTORY_TYPE_SUN_X509);
	        keyManagerFactory.init(keyStore, certPassword.toCharArray());

	        return SslContextBuilder.forClient()
	                .keyManager(keyManagerFactory)
	                .build();

	    } catch (Exception e) {
	        logger.error("Error creating Enroll Subscriber WebClient SSL Context.");	        
	    }

	    return null;
	}
	
    /*
     * Log request details for the downstream web service calls
     */
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(c -> {
        	logger.debug("Request: {} {}", c.method(), c.url());
            c.headers().forEach((n, v) -> logger.debug("request header: {}={}", n, v));
            return Mono.just(c);
        });
    }
}
