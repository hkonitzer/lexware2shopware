package space.schellenberger.etl.shopware2lexware.batch.step.category;

import org.springframework.batch.item.ItemProcessor;
import space.schellenberger.etl.shopware2lexware.dto.CategoryDTO;
import space.schellenberger.etl.shopware2lexware.services.CategoryAPIService;

public class CategoryPProcessor implements ItemProcessor<CategoryDTO, CategoryDTO> {

    private final CategoryAPIService categoryAPIService;

    public CategoryPProcessor(CategoryAPIService categoryAPIService) {
        this.categoryAPIService = categoryAPIService;
    }

    @Override
    public CategoryDTO process(CategoryDTO item) throws Exception {
        // Trying to get ID from database
        CategoryDTO categoryObjectInShopware = categoryAPIService.getCategory(item.getId());
        if (categoryObjectInShopware != null) {
            // Merge both categories, item is leading
            item.mergeFromShopwareObject(categoryObjectInShopware);
        }
        return item;
    }
}
