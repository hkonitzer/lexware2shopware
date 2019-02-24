package space.schellenberger.etl.shopware2lexware.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

/**
 * JobCompletionListener
 *
 * @author Hendrik Schellenberger
 */
public class JobCompletionListener extends JobExecutionListenerSupport {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void afterJob(JobExecution jobExecution) {
        super.afterJob(jobExecution);
        if (log.isTraceEnabled())
            log.trace(String.format("Beende Job %s mit Id %s", jobExecution.getJobConfigurationName(), jobExecution.getJobId()));
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        super.beforeJob(jobExecution);
        if (log.isTraceEnabled())
            log.trace(String.format("Starte Job %s mit Id %s", jobExecution.getJobConfigurationName(), jobExecution.getJobId()));
    }
}
