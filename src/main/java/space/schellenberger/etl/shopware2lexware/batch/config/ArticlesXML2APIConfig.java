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
    @Autowired
    RestTemplate restTemplate;

    @Bean
    @StepScope
    StaxEventItemReader xmlArticlesReader(@Value("#{jobParameters['lexwareXMLDatei']}") String fileName) {
        log.debug(String.format("Calling xmlArticlesReader for file: '%s'", fileName));
        return new LexwareArticleXMLReader(fileName).itemReader();
    }

    @Bean
    ItemProcessor<ArticleDTO, ArticleDTO> articleItemProcessor() {
        return new ArticlePProcessor();
    }

    @Bean
    ItemWriter<ArticleDTO> articleItemWriter() {
        return new Article2ShopwareAPIWriter();
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
        log.info("Launching Job 'readAndStoreArticlesJob'");
        return jobBuilderFactory.get("readAndStoreArticlesJob")
                .incrementer(new RunIdIncrementer())
                .flow(processArticlesStep(xmlArticlesReader(null), articleItemProcessor(), articleItemWriter(), stepBuilderFactory))
                .on(ExitStatus.FAILED.getExitCode()).to(articlesErrorStep()).next(articlesFinishStep())
                .from(processArticlesStep(xmlArticlesReader(null), articleItemProcessor(), articleItemWriter(), stepBuilderFactory)).on("*").to(articlesFinishStep())
                .end()
                .build();
    }

}
