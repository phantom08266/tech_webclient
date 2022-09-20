package com.example.tech_webclient.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientBeanConfig {
    private static final String BASE_URL = "http://localhost:9999";
    private static final int TIMEOUT = 10000;

    @Bean
    public WebClient customWebClient(HttpClient httpClient) {
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public HttpClient creatHttpClient(ConnectionProvider provider) {
        return HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .runOn(LoopResources.create("webClient", 1, 4, true))
                .responseTimeout(Duration.ofMillis(TIMEOUT))
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                });
    }

    @Bean
    public ConnectionProvider createProvider() {
        return ConnectionProvider.builder("customProvider")
                .maxConnections(100) // 최대로 연결을 유지할 수 있는 커넥션 수(Connection Pool max수)
                .maxIdleTime(Duration.ofSeconds(60)) // 사용하지 않는 상태에서의 Connection 유지 시간(요청하는 서버의 idle Time보다 작게 설정해야 Connection Closed에러가 발생하지 않는다.)
                .maxLifeTime(Duration.ofSeconds(60)) // Connection Pool에서 최대 수명 시간
                .pendingAcquireTimeout(Duration.ofMillis(TIMEOUT)) // Conntection Pool에서 사용할 수 있는 Connection이 없을때 새로운 Connection을 얻기위해 대기하는 시간
                .pendingAcquireMaxCount(-1) // Connection을 얻기위해 대기하는 최대 수 (-1은 무한대)
                .evictInBackground(Duration.ofSeconds(30)) //백그라운드에서 만료된 Connection을 제거하는 주기
                .lifo() // 마지막에 사용된 커넥션을 재사용, fifo 먼저 사용된 커넥션을 재사용
                .build();
    }
}
