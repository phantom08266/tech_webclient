package com.example.tech_webclient.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate customRestTemplate() {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(10)
                .setMaxConnPerRoute(10)
                .build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//        factory.setReadTimeout(5000);
        factory.setConnectTimeout(3000);
        factory.setHttpClient(httpClient);

        return new RestTemplate(factory);
    }
}
