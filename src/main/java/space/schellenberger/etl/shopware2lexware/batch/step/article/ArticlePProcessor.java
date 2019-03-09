package space.schellenberger.etl.shopware2lexware.batch.step.article;

import org.springframework.batch.item.ItemProcessor;
import space.schellenberger.etl.shopware2lexware.dto.ArticleDTO;
import space.schellenberger.etl.shopware2lexware.dto.ArticleMainDetailDTO;
import space.schellenberger.etl.shopware2lexware.dto.ArticleSupplierDTO;
import space.schellenberger.etl.shopware2lexware.services.ArticleAPIService;

public class ArticlePProcessor implements ItemProcessor<ArticleDTO, ArticleDTO> {

    private final ArticleAPIService articleAPIService;
    private final ArticleSupplierDTO genericSupplierDTO;

    public ArticlePProcessor(ArticleAPIService articleAPIService, ArticleSupplierDTO genericSupplierDTO) {
        this.articleAPIService = articleAPIService;
        this.genericSupplierDTO = genericSupplierDTO;
    }

    @Override
    public ArticleDTO process(ArticleDTO item) throws Exception {
        ArticleDTO articleInShopwareDTO = null;

        // SETUP: MainDetail - Erzeuge Artikelnummer
        ArticleMainDetailDTO articleMainDetailDTO = new ArticleMainDetailDTO();
        articleMainDetailDTO.setNumber(item.getArtNr());
        articleMainDetailDTO.setSupplierNumber(item.getSupplierAIDFromLexware());
        item.setMainDetail(articleMainDetailDTO);

        // Setzte XML Metadaten von Lexware für Shopware (umschreiben der Felder)
        item.setLexwareXMLData();

        if (item.getId() != null) // Versuche ID (sollte normalerweise nicht gesetzt sein)
            articleInShopwareDTO = articleAPIService.getArticle(item.getId());
        if (articleInShopwareDTO == null) // Versuche Artikelnummer (aus <FEATURE><FNAME>artikelnr)
            articleInShopwareDTO = articleAPIService.getArticleForNumber(item.getMainDetail().getNumber());
        if (articleInShopwareDTO != null) {
            item.mergeFromShopwareObject(articleInShopwareDTO, true);
        } else {
            // SETUP!
            // SETUP: Supplier
            item.setSupplierId(genericSupplierDTO.getId());
            item.setSupplier(genericSupplierDTO);
        }
        return item;
    }
}
