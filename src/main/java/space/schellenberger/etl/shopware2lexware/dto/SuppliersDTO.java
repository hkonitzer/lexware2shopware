package space.schellenberger.etl.shopware2lexware.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Suppliers DTO
 * @author Hendrik Schellenberger
 *
 * @see <a href="https://developers.shopware.com/developers-guide/rest-api/models/#supplier">https://developers.shopware.com/developers-guide/rest-api/models/#supplier</a>
 * @see <a href="https://developers.shopware.com/developers-guide/rest-api/api-resource-manufacturers/">https://developers.shopware.com/developers-guide/rest-api/api-resource-manufacturers/</a>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SuppliersDTO {

    private List<ArticleSupplierDTO> data;
    private Integer total;
    private Boolean success;

    public List<ArticleSupplierDTO> getData() {
        return data;
    }

    public void setData(List<ArticleSupplierDTO> data) {
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
    public ArticleSupplierDTO getSupplierForName(String supplierName) {
        //@TODO: Teste, ob der Suppliername mehrmals vorkommen kann!
        for (ArticleSupplierDTO articleSupplierDTO : getData()) {
            if (articleSupplierDTO.getName().equalsIgnoreCase(supplierName))
                return articleSupplierDTO;
        }
        return null;
    }
}
