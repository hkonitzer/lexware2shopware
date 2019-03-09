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
import space.schellenberger.etl.shopware2lexware.batch.step.article.Article2CatalogGroupProcessor;
import space.schellenberger.etl.shopware2lexware.batch.step.article.ArticleForGroups2ShopwareAPIWriter;
import space.schellenberger.etl.shopware2lexware.batch.step.article.LexwareArticle2CatalogGroupXMLReader;
import space.schellenberger.etl.shopware2lexware.dto.Article2CatalogGroupDTO;
import space.schellenberger.etl.shopware2lexware.services.ArticleAPIService;

import java.net.URISyntaxException;

/**
 * @author Hendrik Schellenberger
 * @version 1.0.0
 */
@EnableBatchProcessing
@Configuration
@Component
public class Articles2CatalogGroupsXML2APIConfig {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JobBuilderFactory jobBuilderFactory;
    @Autowired
    StepBuilderFactory stepBuilderFactory;
    @Autowired
    JobLauncher jobLauncher;

    private final RestTemplate restTemplate;

    public Articles2CatalogGroupsXML2APIConfig(@Autowired RestTemplate restTemplate) throws URISyntaxException {
        this.restTemplate = restTemplate;
    }

    @Bean
    @StepScope
    StaxEventItemReader xmlArticles2GroupsReader(@Value("#{jobParameters['lexwareXMLDatei']}") String fileName) {
        log.debug(String.format("Calling xmlArticles2GroupsReader for file: '%s'", fileName));
        return new LexwareArticle2CatalogGroupXMLReader(fileName).itemReader();
    }

    @Bean
    ItemProcessor<Article2CatalogGroupDTO, Article2CatalogGroupDTO> article2CatalogGroupItemProcessor() {
        return new Article2CatalogGroupProcessor(new ArticleAPIService(restTemplate));
    }

    @Bean
    ItemWriter<Article2CatalogGroupDTO> article2CatalogGroupItemWriter() {
        return new ArticleForGroups2ShopwareAPIWriter();
    }

    @Bean
    Step processArticles2GroupsStep(ItemReader xmlArticles2GroupsReader, ItemProcessor<Article2CatalogGroupDTO, Article2CatalogGroupDTO> articleItemProcessor,  ItemWriter<Article2CatalogGroupDTO> article2CatalogGroupItemWriter, StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("processArticles2GroupsStep")
                .<Article2CatalogGroupDTO, Article2CatalogGroupDTO> chunk(5)
                .reader(xmlArticles2GroupsReader)
                //.processor(articleItemProcessor)
                .writer(article2CatalogGroupItemWriter)
                .build();
    }

    @Bean
    public Step articles2GroupsErrorStep() {
        return stepBuilderFactory.get("fehlerbehandlungsSchritt").tasklet(new LexwareErrorTasklet("FehlerbehandlungsSchritt")).build();
    }

    @Bean
    public Step articles2GroupsFinishStep() {
        return stepBuilderFactory.get("abschliessenderSchritt").tasklet(new LexwareFinishTasklet("AbschliessenderSchritt")).build();
    }

    @Bean
    public Job readAndUpdateArticles2GroupsJob() {
        log.info("Starte Job 'readAndUpdateArticles2GroupsJob'");
        return jobBuilderFactory.get("readAndUpdateArticles2GroupsJob")
                .incrementer(new RunIdIncrementer())
                .flow(processArticles2GroupsStep(xmlArticles2GroupsReader(null), article2CatalogGroupItemProcessor(), article2CatalogGroupItemWriter(), stepBuilderFactory))
                .on(ExitStatus.FAILED.getExitCode()).to(articles2GroupsErrorStep()).next(articles2GroupsFinishStep())
                .from(processArticles2GroupsStep(xmlArticles2GroupsReader(null), article2CatalogGroupItemProcessor(), article2CatalogGroupItemWriter(), stepBuilderFactory)).on("*").to(articles2GroupsFinishStep())
                .end()
                .build();
    }

}
