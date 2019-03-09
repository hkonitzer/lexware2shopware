package space.schellenberger.etl.shopware2lexware.batch.step.category;

import org.springframework.batch.core.ItemReadListener;
import space.schellenberger.etl.shopware2lexware.dto.CategoryDTO;

public class LexwareCategoryXMLReaderListener implements ItemReadListener<CategoryDTO> {

    @Override
    public void beforeRead() {}

    @Override
    public void afterRead(CategoryDTO item) {
        if (item.getParentId() == 0)
            item.setParentId(null);
    }

    @Override
    public void onReadError(Exception ex) {}
}
