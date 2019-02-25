package space.schellenberger.etl.shopware2lexware.batch.step.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import space.schellenberger.etl.shopware2lexware.dto.ArticleDTO;
import java.util.List;

/**
 * @author Hendrik Schellenberger
 */


public class Article2ShopwareAPIWriter implements ItemWriter<ArticleDTO> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public Article2ShopwareAPIWriter() {
    }

    @Override
    public void write(List<? extends ArticleDTO> items) throws Exception {
        log.debug(String.format("Writing %d items", items.size()));
        for (ArticleDTO article : items) {
            System.out.println(article);
        }
    }
}
