package space.schellenberger.etl.shopware2lexware.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * ARTICLE_TO_CATALOGGROUP_MAP DTO
 *
 * @author Hendrik Schellenberger
 */
@XStreamAlias("ARTICLE_TO_CATALOGGROUP_MAP")
public class Article2CatalogGroupDTO {

    @XStreamAlias("ART_ID")
    private Integer artId;
    @XStreamAlias("CATALOG_GROUP_ID")
    private Long catalogGroupId;

    public Integer getArtId() {
        return artId;
    }

    public void setArtId(Integer artId) {
        this.artId = artId;
    }

    public Long getCatalogGroupId() {
        return catalogGroupId;
    }

    public void setCatalogGroupId(Long catalogGroupId) {
        this.catalogGroupId = catalogGroupId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Article2CatalogGroupDTO.class.getSimpleName() + "[", "]")
                .add("artId=" + artId)
                .add("catalogGroupId=" + catalogGroupId)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article2CatalogGroupDTO that = (Article2CatalogGroupDTO) o;
        return getArtId().equals(that.getArtId()) &&
                getCatalogGroupId().equals(that.getCatalogGroupId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getArtId(), getCatalogGroupId());
    }
}
