package space.schellenberger.etl.shopware2lexware.services;


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

    public ArticleAPIService(RestTemplate restTemplate_) {
        this.restTemplate = restTemplate_;
        if (log.isTraceEnabled()) {
            LoggingRequestInterceptor loggingInterceptor = new LoggingRequestInterceptor();
            restTemplate.getInterceptors().add(loggingInterceptor);
        }
        restTemplate.setErrorHandler(new ShopwareResponseErrorHandler());
    }

    public ArticleDTO getArticle(int id) {
        ResponseEntity<ArticleDTO> entity = restTemplate.exchange
                (API_ENDPOINT + id, HttpMethod.GET, null, ArticleDTO.class);
        if (entity.getStatusCode() == HttpStatus.OK) {
            return entity.getBody();
        } else {
            return null;
        }
    }
    public ArticleDTO getArticleForNumber(String number) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(API_ENDPOINT + "/" + number)
                .queryParam("useNumberAsId", true);
        ResponseEntity<ArticleDTO> entity = restTemplate.exchange
                (uriComponentsBuilder.toUriString(), HttpMethod.GET, null, ArticleDTO.class);
        if (entity.getStatusCode() == HttpStatus.OK) {
            return entity.getBody();
        } else {
            return null;
        }
    }


    public boolean updateArticle(ArticleDTO articleDTO) {
        HttpEntity<ArticleDTO> requestEntity = new HttpEntity<ArticleDTO>(articleDTO, new HttpHeaders());
        ResponseEntity<ArticleDTO> entity = null;
        entity = restTemplate.exchange(API_ENDPOINT + articleDTO.getId(), HttpMethod.PUT, requestEntity, ArticleDTO.class);
        return (entity.getStatusCode() == HttpStatus.OK);
    }

    public boolean createArticle(ArticleDTO articleDTO) {
        ResponseEntity<ArticleDTO> entity = restTemplate.postForEntity(API_ENDPOINT, articleDTO, ArticleDTO.class);
        return (entity.getStatusCode() == HttpStatus.CREATED);
    }

}
