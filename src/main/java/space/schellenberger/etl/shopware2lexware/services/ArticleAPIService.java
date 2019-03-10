package space.schellenberger.etl.shopware2lexware.services;


import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import space.schellenberger.etl.shopware2lexware.dto.ArticleDTO;
import space.schellenberger.etl.shopware2lexware.utils.LoggingRequestInterceptor;

@Service
public class ArticleAPIService {

    private static final Logger log = LoggerFactory.getLogger(ArticleAPIService.class);

    private final static String API_ENDPOINT = "/api/articles/";

    private final RestTemplate restTemplate;
    private final MeterRegistry meterRegistry;
    private Timer apiResponseTimer;

    public ArticleAPIService(MeterRegistry meterRegistry_, RestTemplate restTemplate_) {
        this.meterRegistry = meterRegistry_;
        this.restTemplate = restTemplate_;
        if (log.isTraceEnabled()) {
            LoggingRequestInterceptor loggingInterceptor = new LoggingRequestInterceptor();
            restTemplate.getInterceptors().add(loggingInterceptor);
        }
        restTemplate.setErrorHandler(new ShopwareResponseErrorHandler());

        this.apiResponseTimer = Timer
                .builder("l2s.api.response")
                .description("Antwortzeiten der Shopware API")
                .tags("import", "shopwareapi")
                .register(meterRegistry);
    }

    public ArticleDTO getArticle(int id) {
        Timer.Sample sample = Timer.start(meterRegistry);
        ResponseEntity<ArticleDTO> entity = restTemplate.exchange
                (API_ENDPOINT + id, HttpMethod.GET, null, ArticleDTO.class);
        sample.stop(apiResponseTimer);
        if (entity.getStatusCode() == HttpStatus.OK) {
            return entity.getBody();
        } else {
            return null;
        }
    }
    public ArticleDTO getArticleForNumber(String number) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(API_ENDPOINT + "/" + number)
                .queryParam("useNumberAsId", true);
        Timer.Sample sample = Timer.start(meterRegistry);
        ResponseEntity<ArticleDTO> entity = restTemplate.exchange
                (uriComponentsBuilder.toUriString(), HttpMethod.GET, null, ArticleDTO.class);
        sample.stop(apiResponseTimer);
        if (entity.getStatusCode() == HttpStatus.OK) {
            return entity.getBody();
        } else {
            return null;
        }
    }


    public boolean updateArticle(ArticleDTO articleDTO) {
        HttpEntity<ArticleDTO> requestEntity = new HttpEntity<ArticleDTO>(articleDTO, new HttpHeaders());
        ResponseEntity<ArticleDTO> entity = null;
        Timer.Sample sample = Timer.start(meterRegistry);
        entity = restTemplate.exchange(API_ENDPOINT + articleDTO.getId(), HttpMethod.PUT, requestEntity, ArticleDTO.class);
        sample.stop(apiResponseTimer);
        return (entity.getStatusCode() == HttpStatus.OK);
    }

    public boolean createArticle(ArticleDTO articleDTO) {
        Timer.Sample sample = Timer.start(meterRegistry);
        ResponseEntity<ArticleDTO> entity = restTemplate.postForEntity(API_ENDPOINT, articleDTO, ArticleDTO.class);
        sample.stop(apiResponseTimer);
        return (entity.getStatusCode() == HttpStatus.CREATED);
    }

}
