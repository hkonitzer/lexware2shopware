package space.schellenberger.etl.shopware2lexware.batch.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
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
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import space.schellenberger.etl.shopware2lexware.batch.listener.JobCompletionListener;
import space.schellenberger.etl.shopware2lexware.batch.step.category.LexwareCategoryXMLReader;
import space.schellenberger.etl.shopware2lexware.batch.step.category.Category2ShopwareAPIWriter;
import space.schellenberger.etl.shopware2lexware.batch.step.category.CategoryPProcessor;
import space.schellenberger.etl.shopware2lexware.batch.step.category.LexwareCategoryXMLReaderListener;
import space.schellenberger.etl.shopware2lexware.dto.CategoryDTO;
import space.schellenberger.etl.shopware2lexware.services.CategoryAPIService;


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

}
