package space.schellenberger.etl.shopware2lexware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * Lexware 2 Shopware Import
 *
 * @author Hendrik Schellenberger
 * @version 0.0.2
 */
@SpringBootApplication
public class Lexware2ShopwareApplication {

    private final static Logger LOG = LoggerFactory.getLogger(Lexware2ShopwareApplication.class);

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(Lexware2ShopwareApplication.class);
        ConfigurableApplicationContext ctx = app.run(args);
        JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
        LOG.info("START");
        // Read and write categories
        JobParameters jobParametersForCategories = new JobParametersBuilder()
                .addString("lexwareXMLDatei", "E:/temp/catalog.xml")
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        Job processCategoriesJob = ctx.getBean("readAndStoreCategoriesJob", Job.class);
        JobExecution jobExecution1 = jobLauncher.run(processCategoriesJob, jobParametersForCategories);
        LOG.info(String.format("jobExecution1: %s, %s", jobExecution1.getId(), jobExecution1.getExitStatus()));
        // Read and write articles
        JobParameters jobParametersForArticles= new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        //@TODO: Add articles import job
        ctx.close();
        System.exit(0); //@TODO: set exit code according to job errors
    }

}
