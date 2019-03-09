package space.schellenberger.etl.shopware2lexware.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.schellenberger.etl.shopware2lexware.dto.CategoriesDTO;
import space.schellenberger.etl.shopware2lexware.dto.CategoryDTO;
import space.schellenberger.etl.shopware2lexware.utils.LoggingRequestInterceptor;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class CategoryAPIService {

    private static final Logger log = LoggerFactory.getLogger(CategoryAPIService.class);

    private final RestTemplate restTemplate;

    public CategoryAPIService(RestTemplate restTemplate_) {
        this.restTemplate = restTemplate_;
        if (log.isTraceEnabled()) {
            LoggingRequestInterceptor loggingInterceptor = new LoggingRequestInterceptor();
            restTemplate.getInterceptors().add(loggingInterceptor);
        }
        restTemplate.setErrorHandler(new ShopwareResponseErrorHandler());
    }

    public CategoriesDTO getCategories() {
        return restTemplate.getForObject("http://192.168.74.99/api/categories/", CategoriesDTO.class);
    }

    public CategoryDTO getCategory(Integer id) {
        ResponseEntity<CategoryDTO> entity = null;
        entity = restTemplate.exchange("http://192.168.74.99/api/categories/" + id, HttpMethod.GET, null, CategoryDTO.class);
        if (entity.getStatusCode() == HttpStatus.OK) {
            return entity.getBody();
        } else {
            return null;
        }
    }

    public boolean updateCategory(CategoryDTO categoryDTO) {
        HttpEntity<CategoryDTO> requestEntity = new HttpEntity<CategoryDTO>(categoryDTO, new HttpHeaders());
        ResponseEntity<CategoryDTO> entity = null;
        entity = restTemplate.exchange("http://192.168.74.99/api/categories/" + categoryDTO.getId(), HttpMethod.PUT, requestEntity, CategoryDTO.class);
        return (entity.getStatusCode() == HttpStatus.OK);
    }

    public boolean createCategory(CategoryDTO categoryDTO) throws URISyntaxException {
        RequestEntity<CategoryDTO> requestEntity = RequestEntity
                .post(new URI("http://192.168.74.99/api/categories/"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(categoryDTO, CategoryDTO.class);
        ResponseEntity<CategoryDTO> entity = restTemplate.exchange(requestEntity, CategoryDTO.class);
        return (entity.getStatusCode() == HttpStatus.CREATED);
    }
}
