package space.schellenberger.etl.shopware2lexware.batch.step.article;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import space.schellenberger.etl.shopware2lexware.batch.repository.Article2CategoryMappingRepository;
import space.schellenberger.etl.shopware2lexware.dto.Article2CatalogGroupDTO;
import space.schellenberger.etl.shopware2lexware.services.ArticleAPIService;

public class Article2CatalogGroupProcessor implements ItemProcessor<Article2CatalogGroupDTO, Article2CatalogGroupDTO> {

    private final ArticleAPIService articleAPIService;

    @Autowired
    Article2CategoryMappingRepository article2CategoryMappingRepository;

    public Article2CatalogGroupProcessor(ArticleAPIService articleAPIService) {
        this.articleAPIService = articleAPIService;
    }

    @Override
    public Article2CatalogGroupDTO process(Article2CatalogGroupDTO item) throws Exception {

        return item;
    }
}
