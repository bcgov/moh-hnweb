package ca.bc.gov.hlth.hnweb.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

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

import ca.bc.gov.hlth.hnweb.exception.HNWebException;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

	private final Logger logger = LoggerFactory.getLogger(WebClientConfig.class);

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
    public WebClient enrollmentWebClient() throws HNWebException {

		SslContext sslContext = getSSLContext();
		
	    HttpClient httpClient= HttpClient.create().secure(t -> t.sslContext(sslContext));
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

	private SslContext getSSLContext() throws HNWebException {

		try {
			KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_PKCS12);
			keyStore.load(new FileInputStream(certFile.getFile()), certPassword.toCharArray());

			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KEY_MANAGER_FACTORY_TYPE_SUN_X509);
			keyManagerFactory.init(keyStore, certPassword.toCharArray());
			
			return SslContextBuilder.forClient()
			        .keyManager(keyManagerFactory)
			        .build();
		} catch (IOException | CertificateException | UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
	        logger.error("Error creating Enroll Subscriber SSL Context due to {}.", e.toString());
	        throw new HNWebException("SSL Context for Enroll Subscriber could not be built, application will not start", e.getCause());
		}
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
