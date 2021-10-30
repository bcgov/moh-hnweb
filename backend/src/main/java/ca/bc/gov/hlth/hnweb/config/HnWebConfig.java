package ca.bc.gov.hlth.hnweb.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.parser.Parser;

/**
 * Spring configuration file for the HN Web application 
 *
 */
@Configuration
public class HnWebConfig {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(HnWebConfig.class);

	private static final String KEY_STORE_TYPE_PKCS12 = "pkcs12";

	@Value("${R50.user.name}")
	private String userName;

	@Value("${R50.user.password}")
	private String userPassword;

	@Value("classpath:${R50.cert.file}")
	private Resource certFile;

	@Value("${R50.cert.password}")
	private String certPassword;

	@Bean
	public HapiContext hapiContext() {
		return new DefaultHapiContext();
	}
	
	@Bean
	public Parser parser() {
		return hapiContext().getGenericParser();
	}

	/**
	 * Creates a {@link RestTemplate} configured to access the Enroll Subscriber endpoint
	 * @return
	 */
	@Bean
	public RestTemplate enrollmentRestTemplate() {

		SSLConnectionSocketFactory sslConnectionSocketFactory = null;
		try {
			KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_PKCS12);
			keyStore.load(new FileInputStream(certFile.getFile()), certPassword.toCharArray());
	
			SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
		    sslContextBuilder.setProtocol("TLS");
		    sslContextBuilder.loadKeyMaterial(keyStore, certPassword.toCharArray());
		    sslContextBuilder.loadTrustMaterial(new TrustSelfSignedStrategy());
	
		    sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build(), NoopHostnameVerifier.INSTANCE);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableKeyException | KeyManagementException e) {
			logger.error("Error creating SSL Rest Template {}", e.getMessage());
		}
		
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		 
	    credentialsProvider.setCredentials(AuthScope.ANY, 
	                    new UsernamePasswordCredentials(userName, userPassword));
	 	 
	    CloseableHttpClient httpClient = HttpClients.custom()
	            .setSSLSocketFactory(sslConnectionSocketFactory)
	            .setDefaultCredentialsProvider(credentialsProvider)
	            //TODO (daveb-hni) Check if values need to be set for these parameters
//	            .setMaxConnTotal(Integer.valueOf(5))
//	            .setMaxConnPerRoute(Integer.valueOf(5))
	            .build();
	    
	    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
	    requestFactory.setConnectTimeout(60000); // 10 seconds
	    requestFactory.setReadTimeout(60000); // 10 seconds
	    
	    return new RestTemplate(requestFactory);		
	}

}

