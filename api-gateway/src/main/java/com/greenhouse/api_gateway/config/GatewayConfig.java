package com.greenhouse.api_gateway.config;

import com.greenhouse.api_gateway.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Value("${services.greenhouse-url:http://localhost:8081}")
    private String greenhouseUrl;

    @Value("${services.sensor-url:http://localhost:8082}")
    private String sensorUrl;

    @Value("${services.ingestion-url:http://localhost:8083}")
    private String ingestionUrl;

    @Bean
    public RouterFunction<ServerResponse> routes() {
        return route("greenhouse")
                .GET("/api/greenhouses/**", http(greenhouseUrl))
                .POST("/api/greenhouses/**", http(greenhouseUrl))
                .PUT("/api/greenhouses/**", http(greenhouseUrl))
                .DELETE("/api/greenhouses/**", http(greenhouseUrl))
                .GET("/api/sensors/**", http(sensorUrl))
                .POST("/api/sensors/**", http(sensorUrl))
                .DELETE("/api/sensors/**", http(sensorUrl))
                .POST("/api/ingest/**", http(ingestionUrl))
                .filter(jwtAuthFilter)
                .build();
    }
}