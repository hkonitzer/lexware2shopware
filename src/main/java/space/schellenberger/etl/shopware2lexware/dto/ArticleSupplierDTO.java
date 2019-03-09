package space.schellenberger.etl.shopware2lexware.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * ArticleSupplier DTO
 * @author Hendrik Schellenberger
 *
 * @see <a href="https://developers.shopware.com/developers-guide/rest-api/models/#supplier">https://developers.shopware.com/developers-guide/rest-api/models/#supplier</a>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ArticleSupplierDTO {
    private Integer id;
    private String name;
    private String image;
    private String link;
    private String description;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }
}
