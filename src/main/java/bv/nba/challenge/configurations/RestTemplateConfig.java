package bv.nba.challenge.configurations;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    /**
     * Rest template for calling Free NBA.
     */
    @Bean
    public RestTemplate nbaTemplate(RestTemplateBuilder restTemplateBuilder, Environment environment) {
        return restTemplateBuilder
            .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
            .setConnectTimeout(
                Duration.ofMillis(Long.parseLong(environment.getRequiredProperty("nba.timeouts.connection")))
            )
            .setReadTimeout(
                Duration.ofMillis(Long.parseLong(environment.getRequiredProperty("nba.timeouts.read")))
            )
            .build();
    }

}
