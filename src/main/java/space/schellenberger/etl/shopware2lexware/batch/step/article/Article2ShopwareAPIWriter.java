package space.schellenberger.etl.shopware2lexware.batch.step.article;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import space.schellenberger.etl.shopware2lexware.batch.repository.Article2CategoryMappingRepository;
import space.schellenberger.etl.shopware2lexware.dto.ArticleDTO;
import space.schellenberger.etl.shopware2lexware.services.ArticleAPIService;

import java.util.List;

/**
 * @author Hendrik Schellenberger
 */

public class Article2ShopwareAPIWriter implements ItemWriter<ArticleDTO> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ArticleAPIService articleAPIService;

    @Value("${config.shopware.priceGroupId}")
    private Integer priceGroupId;

    @Autowired Article2CategoryMappingRepository article2CategoryMappingRepository;

    private Counter updatedArticlesCounter;
    private Counter createdArticlesCounter;
    private Counter skippedArticlesCounter;

    public Article2ShopwareAPIWriter(MeterRegistry meterRegistry, ArticleAPIService articleAPIService) {
        this.updatedArticlesCounter = Counter
                .builder("l2s.articles.updated")
                .description("Anzahl der erneuerten  Artikel")
                .tags("import", "articles")
                .register(meterRegistry);
        this.createdArticlesCounter = Counter
                .builder("l2s.articles.created")
                .description("Anzahl der neu erzeugten Artikel")
                .tags("import", "articles")
                .register(meterRegistry);
        this.skippedArticlesCounter = Counter
                .builder("l2s.articles.error")
                .description("Anzahl der verworfernen Artikel auf Grund von Fehlern")
                .tags("import", "articles")
                .register(meterRegistry);
        this.articleAPIService = articleAPIService;
    }

    @Override
    public void write(List<? extends ArticleDTO> items) throws Exception {
        log.debug(String.format("Writing %d items", items.size()));
        for (ArticleDTO articleDTO : items) {
            articleDTO.addCategoriesForId(article2CategoryMappingRepository.getCategoriesForSupplierAID(articleDTO.getMainDetail().getSupplierNumber()));
            if (articleDTO.isInShopwareDB()) { // Update
                if (articleAPIService.updateArticle(articleDTO)) {
                    updatedArticlesCounter.increment();
                    if (log.isDebugEnabled())
                        log.debug(String.format("Artikel mit id %d und artnr '%s' geupdated", articleDTO.getId(), articleDTO.getArtNr()));
                } else {
                    skippedArticlesCounter.increment();
                    log.warn(String.format("Fehler beim Update von Artikel mit id %d und artnr '%s'", articleDTO.getId(), articleDTO.getArtNr()));
                }
            } else { // Neuer Artikel
                if (articleDTO.getMainDetail().getInStock() > 0) // Alle Artikel mit Bestand werden aktiviert
                    articleDTO.setActive(true); //@TODO: Konfigurierbar machen
                articleDTO.getMainDetail().setActive(true); // setze MainDetail immer aktiv
                articleDTO.setPriceGroupId(priceGroupId); // Setzte Standard priceGroupId
                articleDTO.setPriceGroupActive(true);
                if (articleAPIService.createArticle(articleDTO)) {
                    createdArticlesCounter.increment();
                    if (log.isDebugEnabled())
                        log.debug(String.format("Artikel mit artnr '%s' angelegt", articleDTO.getArtNr()));
                } else {
                    skippedArticlesCounter.increment();
                    log.warn(String.format("Fehler beim Anlegen von Artikel mit artnr '%s'", articleDTO.getId(), articleDTO.getArtNr()));
                }
            }
        }
    }
}
