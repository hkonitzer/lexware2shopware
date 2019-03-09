package space.schellenberger.etl.shopware2lexware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * Lexware 2 Shopware Import
 *
 * @author Hendrik Schellenberger
 */
@SpringBootApplication
public class Lexware2ShopwareApplication {

    private final static Logger LOG = LoggerFactory.getLogger(Lexware2ShopwareApplication.class);

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(Lexware2ShopwareApplication.class);
        ConfigurableApplicationContext ctx = app.run(args);
        JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);

        String lexwareImportFilename = ctx.getEnvironment().getProperty("config.lexwareXMLDatei");
        if (lexwareImportFilename == null) {
            LOG.error("Ein Lexware Export (als XML Datei) muss angegeben werden (Parameter 'lexwareXMLDatei')");
            System.exit(2);
        }
        LOG.info("START mit Datei: " + lexwareImportFilename);

        // Lese und Schreibe Kategorien
        JobParameters jobParametersForCategories = new JobParametersBuilder()
                .addString("lexwareXMLDatei", lexwareImportFilename)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        Job processCategoriesJob = ctx.getBean("readAndStoreCategoriesJob", Job.class);
        JobExecution jobExecution1 = jobLauncher.run(processCategoriesJob, jobParametersForCategories);
        LOG.info(String.format("jobExecution1 Id: %d, Status: %s", jobExecution1.getId(), jobExecution1.getExitStatus().getExitCode()));
        if (!jobExecution1.getExitStatus().equals(ExitStatus.COMPLETED))
            System.exit(1);

        // Update der Artikel für Kategorien (Verlinkung)
        JobParameters jobParametersForArticles2Groups = new JobParametersBuilder()
                .addString("lexwareXMLDatei", lexwareImportFilename)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        Job processArticles2GroupsJob = ctx.getBean("readAndUpdateArticles2GroupsJob", Job.class);
        JobExecution jobExecution2 = jobLauncher.run(processArticles2GroupsJob, jobParametersForArticles2Groups);
        LOG.info(String.format("jobExecution2 Id: %d, Status: %s", jobExecution2.getId(), jobExecution2.getExitStatus().getExitCode()));
        if (!jobExecution2.getExitStatus().equals(ExitStatus.COMPLETED))
            System.exit(1);

        // Lese und schreibe Artikel
        JobParameters jobParametersForArticles= new JobParametersBuilder()
                .addString("lexwareXMLDatei", lexwareImportFilename)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        Job processArticlesJob = ctx.getBean("readAndStoreArticlesJob", Job.class);
        JobExecution jobExecution3 = jobLauncher.run(processArticlesJob, jobParametersForArticles);
        LOG.info(String.format("jobExecution3 Id: %d, Status: %s", jobExecution3.getId(), jobExecution3.getExitStatus().getExitCode()));
        if (!jobExecution3.getExitStatus().equals(ExitStatus.COMPLETED))
            System.exit(1);

        ctx.close();
        System.exit(0); //@TODO: set exit code according to job errors
    }

}
