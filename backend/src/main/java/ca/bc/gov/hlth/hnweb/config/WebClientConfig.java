package ca.bc.gov.hlth.hnweb.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

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

import ca.bc.gov.hlth.hnweb.exception.ExceptionType;
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

	private static final String CERTIFICATE_TYPE_X509 = "X.509";

	private static final String HCIM_CA_CERT = "hcimCaCert"; //Alias for HCIM trusted certificate

	@Value("${hcim.url}")
	private String hcimUrl;
	
	@Value("classpath:${hcim.cert.file}")
	private Resource hcimCertFile;

	@Value("${hcim.cert.password}")
	private String hcimCertPassword;
	
	@Value("classpath:${hcim.trust.file}")
	private Resource hcimTrustFile;

	@Value("${rapid.url}")
	private String rapidUrl;
	 
	@Value("${rapid.user.name}")
	private String rapidUserName;

	@Value("${rapid.user.password}")
	private String rapidUserPassword;

	@Value("classpath:${rapid.cert.file}")
	private Resource rapidCertFile;

	@Value("${rapid.cert.password}")
	private String rapidCertPassword;
	
	@Value("${hibc.url}")
	private String hibcUrl;

	@Value("classpath:${hibc.cert.file}")
	private Resource hibcCertFile;

	@Value("${hibc.cert.password}")
	private String hibcCertPassword;

	@Bean("hcimWebClient")
    public WebClient hcimWebClient() throws HNWebException {

		SslContext sslContext = getSSLContext(hcimUrl, hcimCertFile, hcimCertPassword, hcimTrustFile);
		
	    HttpClient httpClient= HttpClient.create().secure(t -> t.sslContext(sslContext));
		ClientHttpConnector connector= new ReactorClientHttpConnector(httpClient);
	    
		return WebClient.builder()
	    		.clientConnector(connector)
                .baseUrl(hcimUrl)
                .filter(logRequest())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE) 
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.ALL_VALUE) 
                .build();
    }

	@Bean("hibcWebClient")
    public WebClient hibcWebClient() throws HNWebException {

		SslContext sslContext = getSSLContext(hibcUrl, hibcCertFile, hibcCertPassword);
		
	    HttpClient httpClient= HttpClient.create().secure(t -> t.sslContext(sslContext));
		ClientHttpConnector connector= new ReactorClientHttpConnector(httpClient);
	    
		return WebClient.builder()
	    		.clientConnector(connector)
                .baseUrl(hibcUrl)
                .filter(logRequest())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE) 
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.ALL_VALUE)
                .build();
    }

	@Bean("rapidWebClient")
    public WebClient rapidWebClient() throws HNWebException {

		SslContext sslContext = getSSLContext(rapidUrl, rapidCertFile, rapidCertPassword);
		
	    HttpClient httpClient= HttpClient.create().secure(t -> t.sslContext(sslContext));
		ClientHttpConnector connector= new ReactorClientHttpConnector(httpClient);
	    
		return WebClient.builder()
	    		.clientConnector(connector)
                .baseUrl(rapidUrl)
                .filter(logRequest())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE) 
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.ALL_VALUE) 
                .defaultHeaders(header -> header.setBasicAuth(rapidUserName, rapidUserPassword))
                .build();
    }

	private SslContext getSSLContext(String url, Resource certFile, String certPassword) throws HNWebException {

		return getSSLContext(url, certFile, certPassword, null);
	}
	
	private SslContext getSSLContext(String url, Resource certFile, String certPassword, Resource trustFile) throws HNWebException {

		try {
			KeyManagerFactory keyManagerFactory = createKeyManagerFactory(certFile, certPassword);			
			
			TrustManagerFactory trustManagerFactory = null;
			if (trustFile != null) {
				trustManagerFactory = createTrustManagerFactory(trustFile);				
			}
            
			return SslContextBuilder.forClient()
					.trustManager(trustManagerFactory) //if trustManagerFactory is null it's the same as not setting as the system defaults are used
			        .keyManager(keyManagerFactory)
			        .build();
		} catch (IOException | CertificateException | UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
	        logger.error("Error creating SSL Context for {} due to {}.", url, e.getMessage());
	        throw new HNWebException(ExceptionType.SSL_FAILURE, e);
		}
	}

	private KeyManagerFactory createKeyManagerFactory(Resource certFile, String certPassword)
			throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
		
		KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_PKCS12);
		keyStore.load(new FileInputStream(certFile.getFile()), certPassword.toCharArray());

		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KEY_MANAGER_FACTORY_TYPE_SUN_X509);
		keyManagerFactory.init(keyStore, certPassword.toCharArray());
		
		return keyManagerFactory;
	}

	private TrustManagerFactory createTrustManagerFactory(Resource trustedCertFile) throws CertificateException,
			FileNotFoundException, IOException, KeyStoreException, NoSuchAlgorithmException {
		
		CertificateFactory certificateFactory = CertificateFactory.getInstance(CERTIFICATE_TYPE_X509);
		X509Certificate hcimCert = (X509Certificate)certificateFactory.generateCertificate(new FileInputStream(trustedCertFile.getFile()));
		
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(null); // The KeyStore instance does not need to come from a file.
		keyStore.setCertificateEntry(HCIM_CA_CERT, hcimCert);
		
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(keyStore);
		
		return trustManagerFactory;
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
