package space.schellenberger.etl.shopware2lexware.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import space.schellenberger.etl.shopware2lexware.services.CategoryAPIService;

@Configuration
@Component
public class RestTemplateProvider {

    private static final Logger log = LoggerFactory.getLogger(CategoryAPIService.class);

    @Value("${config.shopware.rootURI}")
    private String rootURI;

    @Value("${config.shopware.apiUser}")
    private String apiUserName;

    @Value("${config.shopware.apiPassword}")
    private String apiPassword;

    @Bean
    public RestTemplate restTemplate() {
        log.info(String.format("Erzeuge REST Template für den Host %s und mit Authorisierung für den Nutzer '%s'", rootURI, apiUserName));
        return new RestTemplateBuilder()
                .rootUri(rootURI)
                .basicAuthorization(apiUserName, apiPassword)
                .build();
    }
}
