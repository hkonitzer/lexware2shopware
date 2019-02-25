package space.schellenberger.etl.shopware2lexware.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;


/**
 * Article DTO
 * @author Hendrik Schellenberger
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@XStreamAlias("ARTICLE")
public class ArticleDTO {

    public class ArticleDetailsXMLDTO {

        @XStreamAlias("DESCRIPTION_LONG")
        private String descriptionLong;
        @XStreamAlias("DESCRIPTION_SHORT")
        private String descriptionShort;

        public String getDescriptionLong() {
            return descriptionLong;
        }

        public void setDescriptionLong(String descriptionLong) {
            this.descriptionLong = descriptionLong;
        }

        public String getDescriptionShort() {
            return descriptionShort;
        }

        public void setDescriptionShort(String descriptionShort) {
            this.descriptionShort = descriptionShort;
        }

        @Override
        public String toString() {
            return "ArticleDetailsXMLDTO{" +
                    "descriptionLong='" + descriptionLong + '\'' +
                    ", descriptionShort='" + descriptionShort + '\'' +
                    '}';
        }
    }

    public class ArticlePriceDetailsXMLDTO {

        public class ArticlePriceDetailsPriceXMLDTO {

            @XStreamAlias("type")
            @XStreamAsAttribute
            private String type;

            @XStreamAlias("PRICE_AMOUNT")
            private float price;

            @XStreamAlias("TAX")
            private float tax;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public float getPrice() {
                return price;
            }

            public void setPrice(float price) {
                this.price = price;
            }

            public float getTax() {
                return tax;
            }

            public void setTax(float tax) {
                this.tax = tax;
            }

            @Override
            public String toString() {
                return "ArticlePriceDetailsPriceXMLDTO{" +
                        "type='" + type + '\'' +
                        ", price=" + price +
                        ", tax=" + tax +
                        '}';
            }
        }

        @XStreamAlias("ARTICLE_PRICE")
        private ArticlePriceDetailsPriceXMLDTO priceXMLDTO;

        public ArticlePriceDetailsPriceXMLDTO getPriceXMLDTO() {
            return priceXMLDTO;
        }

        public void setPriceXMLDTO(ArticlePriceDetailsPriceXMLDTO priceXMLDTO) {
            this.priceXMLDTO = priceXMLDTO;
        }

        @Override
        public String toString() {
            return "ArticlePriceDetailsXMLDTO{" +
                    "priceXMLDTO=" + priceXMLDTO +
                    '}';
        }
    }

    public class ArticleFeaturesXMLDTO {

        public class ArticleFeaturesFeatureXMLDTO {

            @XStreamAlias("FNAME")
            private String name;

            @XStreamAlias("FVALUE")
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        //@XStreamAlias("FEATURE")
        @XStreamImplicit(itemFieldName="FEATURE")
        private List<ArticleFeaturesFeatureXMLDTO> features;

        public List<ArticleFeaturesFeatureXMLDTO> getFeatures() {
            return features;
        }

        public void setFeatures(List<ArticleFeaturesFeatureXMLDTO> features) {
            this.features = features;
        }

        public String getFeatureValue(String featureName) {
            for (ArticleFeaturesFeatureXMLDTO featureXMLDTO : getFeatures()) {
                if (featureXMLDTO.getName().equalsIgnoreCase(featureName)) {
                    return featureXMLDTO.getValue();
                }
            }
            return null;
        }
    }

    @XStreamAlias("ARTICLE_DETAILS")
    private ArticleDetailsXMLDTO xmlArticleDetails;

    @XStreamAlias("ARTICLE_PRICE_DETAILS")
    private ArticlePriceDetailsXMLDTO xmlArticlePrice;

    @XStreamAlias("ARTICLE_FEATURES")
    private ArticleFeaturesXMLDTO xmlArticleFeatures;

    private Integer id;
    private Integer mainDetailId;
    @XStreamAlias("SUPPLIER_AID")
    private Integer supplierId;
    private Integer taxId;
    private Integer priceGroupId;
    private Integer filterGroupId;
    private Integer configuratorSetId;
    private String name;
    private String description;
    private String descriptionLong;
    private Boolean active;
    private Integer pseudoSales;
    private Boolean highlight;
    private String keywords;
    private String metaTitle;
    private Boolean priceGroupActive;
    private Boolean lastStock;
    private Boolean crossBundleLook;
    private Boolean notification;
    private String template;
    private Integer mode;

    private List<ArticleDetailDTO> mainDetail;

    //tax
    //supplier
    //propertyGroup
    //images
    //links
    //categories
    //similiar
    //related
    //details
    //seoCategories

    @JsonIgnore
    private LocalDateTime changed;
    @JsonIgnore
    private LocalDateTime added;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMainDetailId() {
        return mainDetailId;
    }

    public void setMainDetailId(Integer mainDetailId) {
        this.mainDetailId = mainDetailId;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getTaxId() {
        return taxId;
    }

    public void setTaxId(Integer taxId) {
        this.taxId = taxId;
    }

    public Integer getPriceGroupId() {
        return priceGroupId;
    }

    public void setPriceGroupId(Integer priceGroupId) {
        this.priceGroupId = priceGroupId;
    }

    public Integer getFilterGroupId() {
        return filterGroupId;
    }

    public void setFilterGroupId(Integer filterGroupId) {
        this.filterGroupId = filterGroupId;
    }

    public Integer getConfiguratorSetId() {
        return configuratorSetId;
    }

    public void setConfiguratorSetId(Integer configuratorSetId) {
        this.configuratorSetId = configuratorSetId;
    }

    public String getName() {
        if (xmlArticleDetails != null) {
            if (xmlArticleDetails.getDescriptionShort() != null) {
                return xmlArticleDetails.getDescriptionShort();
            }
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionLong() {
        return descriptionLong;
    }

    public void setDescriptionLong(String descriptionLong) {
        this.descriptionLong = descriptionLong;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getPseudoSales() {
        return pseudoSales;
    }

    public void setPseudoSales(Integer pseudoSales) {
        this.pseudoSales = pseudoSales;
    }

    public Boolean getHighlight() {
        return highlight;
    }

    public void setHighlight(Boolean highlight) {
        this.highlight = highlight;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public Boolean getPriceGroupActive() {
        return priceGroupActive;
    }

    public void setPriceGroupActive(Boolean priceGroupActive) {
        this.priceGroupActive = priceGroupActive;
    }

    public Boolean getLastStock() {
        return lastStock;
    }

    public void setLastStock(Boolean lastStock) {
        this.lastStock = lastStock;
    }

    public Boolean getCrossBundleLook() {
        return crossBundleLook;
    }

    public void setCrossBundleLook(Boolean crossBundleLook) {
        this.crossBundleLook = crossBundleLook;
    }

    public Boolean getNotification() {
        return notification;
    }

    public void setNotification(Boolean notification) {
        this.notification = notification;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public List<ArticleDetailDTO> getMainDetail() {
        return mainDetail;
    }

    public void setMainDetail(List<ArticleDetailDTO> mainDetail) {
        this.mainDetail = mainDetail;
    }

    @JsonIgnore
    public String getArtNr() {
        if (xmlArticleFeatures != null) {
            return xmlArticleFeatures.getFeatureValue("artikelnr");
        }
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ArticleDTO{");
        sb.append("xmlArticleDetails=").append(xmlArticleDetails);
        sb.append(", id=").append(id);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", artikelnr='").append(getArtNr()).append('\'');
        sb.append(", name='").append(getName()).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", active=").append(getActive());
        sb.append(", xmlArticlePrice=").append(xmlArticlePrice);
        sb.append('}');
        return sb.toString();
    }
}
