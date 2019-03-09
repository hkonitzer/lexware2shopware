package space.schellenberger.etl.shopware2lexware.batch.step.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired Article2CategoryMappingRepository article2CategoryMappingRepository;

    public Article2ShopwareAPIWriter(ArticleAPIService articleAPIService) {
        this.articleAPIService = articleAPIService;
    }

    @Override
    public void write(List<? extends ArticleDTO> items) throws Exception {
        log.debug(String.format("Writing %d items", items.size()));
        for (ArticleDTO articleDTO : items) {
            articleDTO.addCategoriesForId(article2CategoryMappingRepository.getCategoriesForSupplierAID(articleDTO.getMainDetail().getSupplierNumber()));
            if (articleDTO.isInShopwareDB()) { // Update
                if (articleAPIService.updateArticle(articleDTO)) {
                    if (log.isDebugEnabled())
                        log.debug(String.format("Artikel mit id %d und artnr '%s' geupdated", articleDTO.getId(), articleDTO.getArtNr()));
                } else {
                    log.warn(String.format("Fehler beim Update von Artikel mit id %d und artnr '%s'", articleDTO.getId(), articleDTO.getArtNr()));
                }
            } else { // Neuer Artikel
                if (articleDTO.getMainDetail().getInStock() > 0) // Alle Artikel mit Bestand werden aktiviert
                    articleDTO.setActive(true); //@TODO: Konfigurierbar machen
                if (articleAPIService.createArticle(articleDTO)) {
                    if (log.isDebugEnabled())
                        log.debug(String.format("Artikel mit artnr '%s' angelegt", articleDTO.getArtNr()));
                } else {
                    log.warn(String.format("Fehler beim Anlegen von Artikel mit artnr '%s'", articleDTO.getId(), articleDTO.getArtNr()));
                }
            }
        }
    }
}
