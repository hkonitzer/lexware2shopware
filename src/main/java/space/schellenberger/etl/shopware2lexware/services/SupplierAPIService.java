package space.schellenberger.etl.shopware2lexware.services;


import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.schellenberger.etl.shopware2lexware.dto.ArticleSupplierDTO;
import space.schellenberger.etl.shopware2lexware.dto.SuppliersDTO;
import space.schellenberger.etl.shopware2lexware.utils.LoggingRequestInterceptor;

@Service
public class SupplierAPIService {

    private static final Logger log = LoggerFactory.getLogger(SupplierAPIService.class);

    private final static String API_ENDPOINT = "/api/manufacturers/";

    private final RestTemplate restTemplate;
    private final MeterRegistry meterRegistry;
    private Timer apiResponseTimer;

    public SupplierAPIService(MeterRegistry meterRegistry_, RestTemplate restTemplate_) {
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

    public SuppliersDTO getSuppliers() {
        Timer.Sample sample = Timer.start(meterRegistry);
        SuppliersDTO suppliersDTO = restTemplate.getForObject(API_ENDPOINT, SuppliersDTO.class);
        sample.stop(apiResponseTimer);
        return suppliersDTO;
    }

    public ArticleSupplierDTO getSupplier(int id) {
        Timer.Sample sample = Timer.start(meterRegistry);
        ResponseEntity<ArticleSupplierDTO> entity = restTemplate.exchange
                (API_ENDPOINT + id, HttpMethod.GET, null, ArticleSupplierDTO.class);
        sample.stop(apiResponseTimer);
        if (entity.getStatusCode() == HttpStatus.OK) {
            return entity.getBody();
        } else {
            return null;
        }
    }

    public boolean createSupplier(ArticleSupplierDTO supplierDTO) {
        Timer.Sample sample = Timer.start(meterRegistry);
        ResponseEntity<ArticleSupplierDTO> entity = restTemplate.postForEntity(API_ENDPOINT, supplierDTO, ArticleSupplierDTO.class);
        sample.stop(apiResponseTimer);
        return (entity.getStatusCode() == HttpStatus.CREATED);
    }

}
