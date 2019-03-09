package space.schellenberger.etl.shopware2lexware.batch.step.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import space.schellenberger.etl.shopware2lexware.batch.repository.Article2CategoryMappingRepository;
import space.schellenberger.etl.shopware2lexware.dto.Article2CatalogGroupDTO;
import space.schellenberger.etl.shopware2lexware.dto.ArticleDTO;

import java.util.List;

/**
 * @author Hendrik Schellenberger
 */

public class ArticleForGroups2ShopwareAPIWriter implements ItemWriter<Article2CatalogGroupDTO> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Article2CategoryMappingRepository article2CategoryMappingRepository;

    @Override
    public void write(List<? extends Article2CatalogGroupDTO> items) throws Exception {
        for (Article2CatalogGroupDTO article2CatalogGroupDTO : items) {
            if (!article2CategoryMappingRepository.addMapping(article2CatalogGroupDTO)) {
                log.warn(String.format("Kann Kategorie Mapping für SupplierId %d in Kategorie Id nicht setzen, bereits vorhanden!",
                        article2CatalogGroupDTO.getArtId(),
                        article2CatalogGroupDTO.getCatalogGroupId()));
            }
        }
    }
}
