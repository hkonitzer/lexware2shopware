package space.schellenberger.etl.shopware2lexware.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.schellenberger.etl.shopware2lexware.dto.ArticleDTO;
import space.schellenberger.etl.shopware2lexware.dto.ArticleSupplierDTO;
import space.schellenberger.etl.shopware2lexware.dto.SuppliersDTO;
import space.schellenberger.etl.shopware2lexware.utils.LoggingRequestInterceptor;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class SupplierAPIService {

    private static final Logger log = LoggerFactory.getLogger(SupplierAPIService.class);

    private final static String API_ENDPOINT = "/api/manufacturers/";

    private final RestTemplate restTemplate;

    public SupplierAPIService(RestTemplate restTemplate_) {
        this.restTemplate = restTemplate_;
        if (log.isDebugEnabled()) {
            LoggingRequestInterceptor loggingInterceptor = new LoggingRequestInterceptor();
            restTemplate.getInterceptors().add(loggingInterceptor);
        }
        restTemplate.setErrorHandler(new ShopwareResponseErrorHandler());
    }

    public SuppliersDTO getSuppliers() {
        return restTemplate.getForObject("http://192.168.74.99" + API_ENDPOINT, SuppliersDTO.class);
    }

    public ArticleSupplierDTO getSupplier(int id) {
        ResponseEntity<ArticleSupplierDTO> entity = restTemplate.exchange
                ("http://192.168.74.99"+ API_ENDPOINT + id, HttpMethod.GET, null, ArticleSupplierDTO.class);
        if (entity.getStatusCode() == HttpStatus.OK) {
            return entity.getBody();
        } else {
            return null;
        }
    }

    public boolean createSupplier(ArticleSupplierDTO supplierDTO) throws URISyntaxException {
        RequestEntity<ArticleSupplierDTO> requestEntity = RequestEntity
                .post(new URI("http://192.168.74.99" + API_ENDPOINT))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(supplierDTO, ArticleSupplierDTO.class);
        ResponseEntity<ArticleSupplierDTO> entity = restTemplate.exchange(requestEntity, ArticleSupplierDTO.class);
        return (entity.getStatusCode() == HttpStatus.CREATED);
    }

}
