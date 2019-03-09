package space.schellenberger.etl.shopware2lexware.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * @author Hendrik Schellenberger
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CategoriesDTO {
    private List<CategoryDTO> data;
    private Integer total;
    private Boolean success;

    public List<CategoryDTO> getData() {
        return data;
    }

    public void setData(List<CategoryDTO> data) {
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @JsonIgnore
    public boolean addCategoryDTO(CategoryDTO categoryDTO) {
        if (getCategoryObject(categoryDTO.getId()) != null) {
            return false;
        }
        return getData().add(categoryDTO);
    }
    /**
     * Holt die Wurzel-Kategorie ID, bei Shopware ist die Level 0 Kategorie nicht zu benutzen, von daher
     * ist die eigentliche Root-ID immer darunter anzusetzen
     * Die Wurzel hat selbst keine  Eltern (Parent = 0 oder NULL)
     * @return Integer - Die ID der Wurzel-Kategorie
     */
    @JsonIgnore
    public Integer getRootId() {
        // @TODO: get attribute "type" from xml element "CATALOG_STRUCTURE" with value "root"
        for (CategoryDTO categoryDTO : getData()) {
            if (categoryDTO.getParentId() == null || categoryDTO.getParentId() == 0)
                return categoryDTO.getId();
        }
        return null;
    }

    /**
     * Holt die Wurzel-Kategorie aus Lexware
     *
     * @return ID der Wurzel-Kategorie aus dem Lexware-Export
     */
    @JsonIgnore
    public Integer getLexwareRootId() {
        for (CategoryDTO categoryDTO : getData()) {
            if (categoryDTO.getType() != null && categoryDTO.getType().equalsIgnoreCase("root"))
                return categoryDTO.getId();
        }
        return null;
    }

    @JsonIgnore
    public CategoryDTO getCategoryObject(Integer id) {
        for (CategoryDTO categoryDTO : getData()) {
            if (categoryDTO.getId().intValue() == id.intValue())
                return categoryDTO;
        }
        return null;
    }

    @Override
    public String toString() {
        return "CategoriesDTO{" +
                "data.size=" + data.size() +
                ", total=" + total +
                ", success=" + success +
                '}';
    }
}

