package space.schellenberger.etl.shopware2lexware.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class LexwareErrorTasklet implements Tasklet {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    final String text;

    public LexwareErrorTasklet( String text ) { this.text = text; }

    @Override
    public RepeatStatus execute(StepContribution sc, ChunkContext cc ) throws Exception {
        log.error(String.format("Lexware Import Fehler: %s", text));
        return RepeatStatus.FINISHED;
    }
}
