package space.schellenberger.etl.shopware2lexware.batch.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import space.schellenberger.etl.shopware2lexware.dto.Article2CatalogGroupDTO;

import java.util.*;

@Repository
@Component
public class Article2CategoryMappingRepository {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Set<Article2CatalogGroupDTO> mappingSet;

    public Article2CategoryMappingRepository() {
        this.mappingSet = new HashSet<>(1000, 1f);
        log.debug("Initialisiere Article2CategoryMappingRepository");
    }

    public Set<Article2CatalogGroupDTO> getMappings() {
        return mappingSet;
    }

    public boolean addMapping(Article2CatalogGroupDTO article2CatalogGroupDTO) {
        return mappingSet.add(article2CatalogGroupDTO);
    }

    public List<Long> getCategoriesForSupplierAID(String supplierAID) {
        Integer supplierId = Integer.parseInt(supplierAID, 10);
        ArrayList<Long> categoryList = new ArrayList<>(5);
        for (Article2CatalogGroupDTO article2CatalogGroupDTO : getMappings()) {
            if (article2CatalogGroupDTO.getArtId().intValue() == supplierId.intValue())
                categoryList.add(article2CatalogGroupDTO.getCatalogGroupId());
        }
        return categoryList;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Article2CategoryMappingRepository.class.getSimpleName() + "[", "]")
                .add("mappingSet.size=" + mappingSet.size())
                .toString();
    }
}
