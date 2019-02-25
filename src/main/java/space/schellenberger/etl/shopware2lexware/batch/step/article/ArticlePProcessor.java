package space.schellenberger.etl.shopware2lexware.batch.step.article;

import org.springframework.batch.item.ItemProcessor;
import space.schellenberger.etl.shopware2lexware.dto.ArticleDTO;

public class ArticlePProcessor implements ItemProcessor<ArticleDTO, ArticleDTO> {

    public ArticlePProcessor() { }

    @Override
    public ArticleDTO process(ArticleDTO item) throws Exception {
        return item;
    }
}
