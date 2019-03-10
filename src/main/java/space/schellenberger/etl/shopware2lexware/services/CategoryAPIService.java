package space.schellenberger.etl.shopware2lexware.services;


import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.schellenberger.etl.shopware2lexware.dto.CategoriesDTO;
import space.schellenberger.etl.shopware2lexware.dto.CategoryDTO;
import space.schellenberger.etl.shopware2lexware.utils.LoggingRequestInterceptor;

@Service
public class CategoryAPIService {

    private static final Logger log = LoggerFactory.getLogger(CategoryAPIService.class);

    private final static String API_ENDPOINT = "/api/categories/";

    private final RestTemplate restTemplate;
    private final MeterRegistry meterRegistry;
    private Timer apiResponseTimer;

    public CategoryAPIService(MeterRegistry meterRegistry_, RestTemplate restTemplate_) {
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

    public CategoriesDTO getCategories() {
        return restTemplate.getForObject(API_ENDPOINT, CategoriesDTO.class);
    }

    public CategoryDTO getCategory(Integer id) {
        Timer.Sample sample = Timer.start(meterRegistry);
        ResponseEntity<CategoryDTO> entity = restTemplate.exchange(API_ENDPOINT + id, HttpMethod.GET, null, CategoryDTO.class);
        sample.stop(apiResponseTimer);
        if (entity.getStatusCode() == HttpStatus.OK) {
            return entity.getBody();
        } else {
            return null;
        }
    }

    public boolean updateCategory(CategoryDTO categoryDTO) {
        HttpEntity<CategoryDTO> requestEntity = new HttpEntity<CategoryDTO>(categoryDTO, new HttpHeaders());
        Timer.Sample sample = Timer.start(meterRegistry);
        ResponseEntity<CategoryDTO> entity = restTemplate.exchange(API_ENDPOINT + categoryDTO.getId(), HttpMethod.PUT, requestEntity, CategoryDTO.class);
        sample.stop(apiResponseTimer);
        return (entity.getStatusCode() == HttpStatus.OK);
    }

    public boolean createCategory(CategoryDTO categoryDTO) {
        Timer.Sample sample = Timer.start(meterRegistry);
        ResponseEntity<CategoryDTO> entity = restTemplate.postForEntity(API_ENDPOINT, categoryDTO, CategoryDTO.class);
        sample.stop(apiResponseTimer);
        return (entity.getStatusCode() == HttpStatus.CREATED);
    }
}
