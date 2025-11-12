package com.auramind.api.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Value("${app.ai.base-url}")
    private String aiBaseUrl;

    @Bean
    public WebClient aiWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
                .responseTimeout(Duration.ofSeconds(30))
                .doOnConnected(conn -> {
                    conn.addHandlerLast(new ReadTimeoutHandler(30, TimeUnit.SECONDS));
                    conn.addHandlerLast(new WriteTimeoutHandler(30, TimeUnit.SECONDS));
                });

        return WebClient.builder()
                .baseUrl(aiBaseUrl) // ex.: https://SEU_FASTAPI.onrender.com/
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
