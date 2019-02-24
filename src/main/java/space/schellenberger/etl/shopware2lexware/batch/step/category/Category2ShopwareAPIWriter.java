package space.schellenberger.etl.shopware2lexware.batch.step.category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import space.schellenberger.etl.shopware2lexware.dto.CategoriesDTO;
import space.schellenberger.etl.shopware2lexware.dto.CategoryDTO;
import space.schellenberger.etl.shopware2lexware.services.CategoryAPIService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hendrik Schellenberger
 */


public class Category2ShopwareAPIWriter implements ItemWriter<CategoryDTO> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CategoryAPIService categoryAPIService;
    private final Integer rootId;
    private final List<Integer> placeholderIdsAdded = new ArrayList<>(10);

    public Category2ShopwareAPIWriter(CategoryAPIService categoryAPIService_) {
        this.categoryAPIService = categoryAPIService_;
        // get the root id
        // Beware: Race conditions may occur
        CategoriesDTO allKnownCategories = categoryAPIService.getCategories();
        this.rootId = allKnownCategories.getRootId();
    }

    @Override
    public void write(List<? extends CategoryDTO> items) throws Exception {
        log.debug(String.format("Writing %d items", items.size()));
        for (CategoryDTO category : items) {
            if (category.getParentId() != null) {
                CategoryDTO parent = categoryAPIService.getCategory(category.getParentId());
                if (parent == null) {
                    CategoryDTO placeholderCategory = new CategoryDTO();
                    placeholderCategory.setId(category.getParentId());
                    placeholderCategory.setParentId(rootId);
                    placeholderCategory.setActive(false);
                    placeholderCategory.setName("(placeholder for import)");
                    if (categoryAPIService.createCategory(placeholderCategory)) {
                        //knownCategoriesInShopware.setAddedForId(placeholderCategory.getId(), LocalDateTime.now());
                        placeholderIdsAdded.add(placeholderCategory.getId());
                        if (log.isDebugEnabled())
                            log.debug(String.format("Platzhaler-Kategorie mit id %d und parentId %d angelegt", category.getId(), category.getParentId()));
                    } else {
                        log.error(String.format("ACHTUNG! Platzhalter Kategorie id %d konnte nicht angelegt werden!", placeholderCategory.getId()));
                        continue;
                    }
                }
            }
            if (category.isInShopwareDB() || placeholderIdsAdded.contains(category.getId())) {
                if (categoryAPIService.updateCategory(category)) {
                    if (log.isDebugEnabled())
                        log.debug(String.format("Kategorie '%s' mit id %d und parentId %d angelegt", category.getName(), category.getId(), category.getParentId()));
                } else {
                    log.warn(String.format("Fehler beim Update von Kategorie '%s' mit id %d und parentId %d", category.getName(), category.getId(), category.getParentId()));
                }
            } else {
                category.setActive(true);
                if (categoryAPIService.createCategory(category)) {
                    if (log.isDebugEnabled())
                        log.debug(String.format("Kategorie '%s' mit id %d und parentId %d angelegt", category.getName(), category.getId(), category.getParentId()));
                } else {
                    log.warn(String.format("Fehler beim Anlegen von Kategorie '%s' mit id %d und parentId %d", category.getName(), category.getId(), category.getParentId()));
                }
            }
        }
    }
}
