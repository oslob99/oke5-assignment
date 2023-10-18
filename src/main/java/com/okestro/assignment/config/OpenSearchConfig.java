package com.okestro.assignment.config;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
@RequiredArgsConstructor
public class OpenSearchConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("admin", "admin"));

        final SSLContextBuilder sslBuilder = SSLContexts.custom()
                .loadTrustMaterial(null, (x509Certificates, s) -> true);
        final SSLContext sslContext = sslBuilder.build();


        return new RestHighLevelClient(
                RestClient.builder(new HttpHost("10.0.11.61", Integer.parseInt("9200"), "https"))
                        .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                                .setSSLContext(sslContext)
                                .setDefaultCredentialsProvider(credentialsProvider)
                                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE))
                        .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder.setConnectionRequestTimeout(5000)
                                .setSocketTimeout(120000)));
    }

}
