package space.schellenberger.etl.shopware2lexware.batch.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Configuration
@Component
public class RestTemplateProvider {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                //.rootUri("http://http://192.168.74.99/api")
                .basicAuthorization("apiuser", "TKt6QxRG4NWRM1yG52LgebxF2hUQlM7QdEEzgJrv")
                .build();
    }
}
