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
    private final CategoriesDTO allKnownCategories;
    private Integer rootId;
    private Integer oldLexwareRootId;
    private final List<Integer> placeholderIdsAdded = new ArrayList<>(10);

    public Category2ShopwareAPIWriter(CategoryAPIService categoryAPIService_) {
        this.categoryAPIService = categoryAPIService_;
        // Hole die Root-ID
        // Achtung: Race conditions?!
        this.allKnownCategories = categoryAPIService.getCategories();
        this.rootId = allKnownCategories.getRootId();
    }

    @Override
    public void write(List<? extends CategoryDTO> items) throws Exception {
        log.debug(String.format("Schreibe %d items", items.size()));
        for (CategoryDTO category : items) {
            if (category.getParentId() != null) {
                if (oldLexwareRootId != null && category.getParentId().intValue() == oldLexwareRootId.intValue())
                    category.setParentId(rootId); // schreibe wegen Konflikt ID um, siehe unten
                CategoryDTO parent = categoryAPIService.getCategory(category.getParentId());
                if (parent == null) {
                    CategoryDTO placeholderCategory = new CategoryDTO();
                    placeholderCategory.setId(category.getParentId());
                    placeholderCategory.setParentId(rootId);
                    placeholderCategory.setActive(false);
                    placeholderCategory.setName("(platzhalter für import)");
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
            } else {
                // Versuche Lexware und Shopware Wurzel abzufangen
                // Gibt es bereits ein Match auf den Namen?
                for (CategoryDTO categoryDTO_ : allKnownCategories.getData()) {
                    if (category.getName().equalsIgnoreCase(categoryDTO_.getName())) {
                        // Überschreibe ID!
                        log.info(String.format("Wurzel-Kategorie aus Lexware wird umgeschrieben! Lexware ID %d wird zu Shopware ID  %d", category.getId(), categoryDTO_.getId()));
                        this.oldLexwareRootId = category.getId();
                        category.setId(categoryDTO_.getId());
                        category.setParentId(categoryDTO_.getParentId());
                        // Setze neue ROOT ID für folgende
                        this.rootId = category.getId();
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
