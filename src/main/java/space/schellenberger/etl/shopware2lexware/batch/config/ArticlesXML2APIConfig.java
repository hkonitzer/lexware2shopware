package space.schellenberger.etl.shopware2lexware.batch.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import space.schellenberger.etl.shopware2lexware.batch.step.article.Article2ShopwareAPIWriter;
import space.schellenberger.etl.shopware2lexware.batch.step.article.ArticlePProcessor;
import space.schellenberger.etl.shopware2lexware.batch.step.article.LexwareArticleXMLReader;
import space.schellenberger.etl.shopware2lexware.batch.step.category.LexwareCategoryXMLReader;
import space.schellenberger.etl.shopware2lexware.dto.ArticleDTO;
import space.schellenberger.etl.shopware2lexware.dto.ArticleSupplierDTO;
import space.schellenberger.etl.shopware2lexware.dto.SuppliersDTO;
import space.schellenberger.etl.shopware2lexware.services.ArticleAPIService;
import space.schellenberger.etl.shopware2lexware.services.SupplierAPIService;

import java.net.URISyntaxException;

/**
 * @author Hendrik Schellenberger
 * @version 1.0.0
 */
@EnableBatchProcessing
@Configuration
@Component
public class ArticlesXML2APIConfig {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JobBuilderFactory jobBuilderFactory;
    @Autowired
    StepBuilderFactory stepBuilderFactory;
    @Autowired
    JobLauncher jobLauncher;

    private final RestTemplate restTemplate;
    private final static String GENERIC_SUPPLIER_NAME = "LEXWARE2SHOPWARE IMPORT SUPPLIER";
    private final ArticleSupplierDTO genericSupplierDTO;

    public ArticlesXML2APIConfig(@Autowired RestTemplate restTemplate) throws URISyntaxException {
        this.restTemplate = restTemplate;
        log.debug("Erzeuge generischen Hersteller (Supplier)");
        SupplierAPIService supplierAPIService = new SupplierAPIService(restTemplate);
        SuppliersDTO allKnownSuppliersDTO = supplierAPIService.getSuppliers();
        ArticleSupplierDTO genericSupplierDTO_ = allKnownSuppliersDTO.getSupplierForName(GENERIC_SUPPLIER_NAME);
        if (genericSupplierDTO_ == null) {
            genericSupplierDTO_ = new ArticleSupplierDTO();
            genericSupplierDTO_.setName(GENERIC_SUPPLIER_NAME);
            if (!supplierAPIService.createSupplier(genericSupplierDTO_)) {
                log.error(String.format("Kann Generischen Hersteller mit Namen '%s' nicht erzeugen!", GENERIC_SUPPLIER_NAME));
                System.exit(1);
            } else {
                allKnownSuppliersDTO = supplierAPIService.getSuppliers();
                genericSupplierDTO_ = allKnownSuppliersDTO.getSupplierForName(GENERIC_SUPPLIER_NAME);
                log.debug(String.format("Generischer Hersteller mit Namen '%s' und id %d angelegt!", GENERIC_SUPPLIER_NAME, genericSupplierDTO_.getId()));
            }
        }
        this.genericSupplierDTO = genericSupplierDTO_;
    }

    public ArticleSupplierDTO getGenericSupplierDTO() {
        return genericSupplierDTO;
    }

    @Bean
    @StepScope
    StaxEventItemReader xmlArticlesReader(@Value("#{jobParameters['lexwareXMLDatei']}") String fileName) {
        log.debug(String.format("Calling xmlArticlesReader for file: '%s'", fileName));
        return new LexwareArticleXMLReader(fileName).itemReader();
    }

    @Bean
    ItemProcessor<ArticleDTO, ArticleDTO> articleItemProcessor() {
        return new ArticlePProcessor(new ArticleAPIService(restTemplate), getGenericSupplierDTO());
    }

    @Bean
    ItemWriter<ArticleDTO> articleItemWriter() {
        return new Article2ShopwareAPIWriter(new ArticleAPIService(restTemplate));
    }

    @Bean
    Step processArticlesStep(ItemReader xmlArticlesReader, ItemProcessor<ArticleDTO, ArticleDTO> articleItemProcessor, ItemWriter<ArticleDTO> articleItemWriter, StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("processArticlesStep")
                .<ArticleDTO, ArticleDTO> chunk(5)
                .reader(xmlArticlesReader)
                .processor(articleItemProcessor)
                .writer(articleItemWriter).build();
    }

    @Bean
    public Step articlesErrorStep() {
        return stepBuilderFactory.get("fehlerbehandlungsSchritt").tasklet(new LexwareErrorTasklet("FehlerbehandlungsSchritt")).build();
    }

    @Bean
    public Step articlesFinishStep() {
        return stepBuilderFactory.get("abschliessenderSchritt").tasklet(new LexwareFinishTasklet("AbschliessenderSchritt")).build();
    }

    @Bean
    public Job readAndStoreArticlesJob() {
        log.info("Erzeuge Job 'readAndStoreArticlesJob'");
        return jobBuilderFactory.get("readAndStoreArticlesJob")
                .incrementer(new RunIdIncrementer())
                .flow(processArticlesStep(xmlArticlesReader(null), articleItemProcessor(), articleItemWriter(), stepBuilderFactory))
                .on(ExitStatus.FAILED.getExitCode()).to(articlesErrorStep()).next(articlesFinishStep())
                .from(processArticlesStep(xmlArticlesReader(null), articleItemProcessor(), articleItemWriter(), stepBuilderFactory)).on("*").to(articlesFinishStep())
                .end()
                .build();
    }

}
