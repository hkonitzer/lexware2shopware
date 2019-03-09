package space.schellenberger.etl.shopware2lexware.batch.step.category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import space.schellenberger.etl.shopware2lexware.dto.CategoryDTO;
import space.schellenberger.etl.shopware2lexware.services.CategoryAPIService;

public class CategoryPProcessor implements ItemProcessor<CategoryDTO, CategoryDTO> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CategoryAPIService categoryAPIService;

    public CategoryPProcessor(CategoryAPIService categoryAPIService) {
        this.categoryAPIService = categoryAPIService;
    }

    @Override
    public CategoryDTO process(CategoryDTO item) throws Exception {
        // Prüfe ob ID bereits existiert
        CategoryDTO categoryObjectInShopware = categoryAPIService.getCategory(item.getId());
        if (categoryObjectInShopware != null) {
            // Sonderfall: Wurzelkategorie aus Lexware, darf nicht Wurzel in Shopware sein (sondern eine Ebene darunter)
            // Unangenehm, weil die ID 1 in Shopware meist die Wurzel ist, ebenso wie in Lexware
            if (item.getType().equalsIgnoreCase("root")) {
                if (categoryObjectInShopware.getParentId() == null &&
                        item.getId().intValue() == categoryObjectInShopware.getId().intValue()) {
                    // Beide IDs gleich: Konflikt.
                    log.warn("ACHTUNG: Shopware Wurzel-Kategorie hat gleiche ID wie Lexware Wurzel-ID!");
                    //@TODO: Wurzel Kategorie / erste Ebene für Lexware Import konfigurierbar machen
                }
            }
            // Update Kategorie mit Shopwaredaten
            item.mergeFromShopwareObject(categoryObjectInShopware);
        }
        return item;
    }
}
