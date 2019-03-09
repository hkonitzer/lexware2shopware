package space.schellenberger.etl.shopware2lexware.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Article DTO
 *
 * @author Hendrik Schellenberger
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(using = ArticleJSONDeserializer.class)
@XStreamAlias("ARTICLE")
public class ArticleDTO {

    private final static Logger LOG = LoggerFactory.getLogger(ArticleDTO.class);

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
        @XStreamImplicit(itemFieldName = "FEATURE")
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
    private Integer supplierId;
    @XStreamAlias("SUPPLIER_AID")
    @JsonIgnore
    private String supplierAIDFromLexware;
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

    private ArticleMainDetailDTO mainDetail;
    private ArticleSupplierDTO supplier;

    private class ArticleTaxDTO {
        private Integer id;
        private String name;
        private String tax;

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

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }
    }

    private ArticleTaxDTO tax;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private class ArticleCategoriesDTO {
        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private List<ArticleCategoriesDTO> categories;

    //supplier
    //propertyGroup
    //images
    //links
    //similiar
    //related
    //details
    //seoCategories

    @JsonIgnore
    private LocalDateTime changed;
    @JsonIgnore
    private LocalDateTime added;

    public ArticleDTO() {}

    public ArticleDTO(Integer id) {
        setId(id);
    }

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

    public String getSupplierAIDFromLexware() {
        return supplierAIDFromLexware;
    }

    public void setSupplierAIDFromLexware(String supplierAIDFromLexware) {
        this.supplierAIDFromLexware = supplierAIDFromLexware;
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
        if (xmlArticleDetails != null) {
            if (xmlArticleDetails.getDescriptionShort() != null) {
                return xmlArticleDetails.getDescriptionShort();
            }
        }
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

    public ArticleMainDetailDTO getMainDetail() {
        return mainDetail;
    }

    public void setMainDetail(ArticleMainDetailDTO mainDetail) {
        this.mainDetail = mainDetail;
    }

    public ArticleSupplierDTO getSupplier() {
        return supplier;
    }

    public void setSupplier(ArticleSupplierDTO supplier) {
        this.supplier = supplier;
    }

    public ArticleTaxDTO getTax() {
        return tax;
    }

    public void setTax(ArticleTaxDTO tax) {
        this.tax = tax;
    }

    @JsonIgnore
    public void setTaxValue(String taxValue) {
        getTax().setTax(taxValue);
    }

    public List<ArticleCategoriesDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<ArticleCategoriesDTO> categories) {
        this.categories = categories;
    }

    @JsonIgnore
    public void addCategoriesForId(List<Long> categoryIds) {
        if (getCategories() == null) {
            setCategories(new ArrayList<>(categoryIds.size()));
        }
        for (Long categoryId : categoryIds) {
            ArticleCategoriesDTO articleCategoriesDTO = new ArticleCategoriesDTO();
            articleCategoriesDTO.setId(categoryId);
            getCategories().add(articleCategoriesDTO);
        }
    }

    @JsonIgnore
    public void addCategoryForId(Long categoryId) {
        if (getCategories() == null) {
            setCategories(new ArrayList<ArticleCategoriesDTO>(1));
        }
        ArticleCategoriesDTO articleCategoriesDTO = new ArticleCategoriesDTO();
        articleCategoriesDTO.setId(categoryId);
        getCategories().add(articleCategoriesDTO);
    }

    @JsonIgnore
    public LocalDateTime getChanged() {
        return changed;
    }

    @JsonIgnore
    public void setChanged(LocalDateTime changed) {
        this.changed = changed;
    }

    @JsonIgnore
    public LocalDateTime getAdded() {
        return added;
    }

    @JsonIgnore
    public void setAdded(LocalDateTime added) {
        this.added = added;
    }

    @JsonIgnore
    public String getArtNr() {
        if (xmlArticleFeatures != null) {
            return xmlArticleFeatures.getFeatureValue("artikelnr");
        }
        return null;
    }

    @JsonIgnore
    public void setLexwareXMLData() {
        if (xmlArticleFeatures != null) {
            // Langbeschreibung
            if (xmlArticleDetails.getDescriptionLong() != null) {
                setDescriptionLong(xmlArticleDetails.getDescriptionLong());
                if (LOG.isTraceEnabled())
                    LOG.trace(String.format("Setzte Artikeldaten von Lexware für ID %s / ArtNr %s - descriptionLong"
                            , getId(), getArtNr()));
            }
            // (Kurz-)Beschreibung
            if (xmlArticleDetails.getDescriptionShort() != null) {
                setDescription(xmlArticleDetails.getDescriptionShort());
                if (LOG.isTraceEnabled())
                    LOG.trace(String.format("Setzte Artikeldaten von Lexware für ID %s / ArtNr %s - description"
                            , getId(), getArtNr()));
            }
            // Bestand, Gewicht
            if (xmlArticleFeatures.getFeatures().size() > 0) {
                if (mainDetail != null) {
                    String stock = xmlArticleFeatures.getFeatureValue("menge_bestand");
                    if (stock != null) {

                        getMainDetail().setInStock(stock);
                        if (LOG.isTraceEnabled())
                            LOG.trace(String.format("Setzte Artikeldaten von Lexware für ID %s / ArtNr %s - menge_bestand [%s]"
                                    , getId(), getArtNr(), mainDetail.getInStock()));
                    }
                    String weight = xmlArticleFeatures.getFeatureValue("Gewicht");
                    if (weight != null) {
                        getMainDetail().setWeight(weight);
                        if (LOG.isTraceEnabled())
                            LOG.trace(String.format("Setzte Artikeldaten von Lexware für ID %s / ArtNr %s - Gewicht [%s]"
                                    , getId(), getArtNr(), mainDetail.getWeight()));
                    }
                } else {
                    LOG.warn(String.format("Setzte Artikeldaten von Lexware für ID %s / ArtNr %s fehlgeschlagen, mainDetail DTO nicht angelegt"
                            , getId(), getArtNr())); // warum legen wir es dann nicht jetzt an?
                }
            }
        }
        // Preise und Steuern
        if (xmlArticlePrice.getPriceXMLDTO() != null) {
            if (mainDetail != null) {
                mainDetail.setPrice(xmlArticlePrice.getPriceXMLDTO().getPrice());
                if (LOG.isTraceEnabled())
                    LOG.trace(String.format("Setzte Preis von Lexware für ID %s / ArtNr %s - Type: %s Wert: %s"
                            , getId(), getArtNr(), xmlArticlePrice.getPriceXMLDTO().getType(), xmlArticlePrice.getPriceXMLDTO().getPrice()));
            } else {
                LOG.warn(String.format("Setzte Preis von Lexware für ID %s / ArtNr %s fehlgeschlagen, mainDetail DTO nicht angelegt"
                        , getId(), getArtNr()));
            }
            ArticleTaxDTO taxDTO = new ArticleTaxDTO();
            taxDTO.setTax((xmlArticlePrice.getPriceXMLDTO().getTax() * 100) + ".00"); // ARTICLE_PRICE_DETAILS>ARTICLE_PRICE>TAX
            setTax(taxDTO);
        }

    }

    /**
     * Schreibt Felder aus dem übergebenen DTO an dieses DTO, dabei wird geprüft ob ID und Artikelnummer identisch sind
     * und das übergebene Objekt aus der Shopware DB stammt
     *
     * @param shopwareArticleDTO
     */
    @JsonIgnore
    public void mergeFromShopwareObject(ArticleDTO shopwareArticleDTO) {
        mergeFromShopwareObject(shopwareArticleDTO, false);
    }

    /**
     * Schreibt Felder aus dem übergebenen DTO an dieses DTO, dabei wird geprüft ob ID und Artikelnummer identisch sind
     * und das übergebene Objekt aus der Shopware DB stammt, sofern force übergeben wird
     *
     * @param shopwareArticleDTO
     * @param force              - Prüfung forcieren (true/false)
     */
    @JsonIgnore
    public void mergeFromShopwareObject(ArticleDTO shopwareArticleDTO, boolean force) {
        if (!force) {
            if (getId() != null && shopwareArticleDTO.getId().intValue() != getId().intValue()) {
                throw new UnsupportedOperationException(String.format("Kann ArticleDTOs nicht vereinen, IDs unterschiedlich! Shopware: %s vs. %s", shopwareArticleDTO.getId(), getId()));
            }
            if (!getMainDetail().getNumber().equalsIgnoreCase(shopwareArticleDTO.getMainDetail().getNumber())) {
                throw new UnsupportedOperationException(String.format("Kann ArticleDTOs nicht vereinen, Artikelnummern (mainDetail.Number) unterschiedlich! Shopware: %s vs. %s", shopwareArticleDTO.getMainDetail().getNumber(), getMainDetail().getNumber()));
            }
            if (shopwareArticleDTO.getAdded() == null || shopwareArticleDTO.getChanged() == null) {
                throw new UnsupportedOperationException(String.format("Kann ArticleDTO nicht vereinen: Übergebenes Objekt mit id %d hat keine added/changed Zeitstempel (nicht in Datenbank?)", shopwareArticleDTO.getId()));
            }
        }
        // Aufpassen! Alle folgenden Felder werden immer von Shopware überschrieben!
        setId(shopwareArticleDTO.getId());
        setMainDetailId(shopwareArticleDTO.getMainDetailId());
        setTaxId(shopwareArticleDTO.getTaxId());
        setActive(shopwareArticleDTO.getActive());
        setSupplierId(shopwareArticleDTO.getSupplierId()); //@TODO: Aktuell wird das genericSupplierDTO nicht angepasst!
        setAdded(shopwareArticleDTO.getAdded());
        setChanged(shopwareArticleDTO.getChanged());
    }

    @JsonIgnore
    public boolean isInShopwareDB() {
        return getAdded() != null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ArticleDTO{");
        sb.append("id=").append(id);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", artikelnr='").append(getArtNr()).append('\'');
        sb.append(", name='").append(getName()).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", active=").append(getActive());
        sb.append(", mainDetail=").append(getMainDetail());
        sb.append(", xmlArticlePrice=").append(xmlArticlePrice);
        sb.append(", xmlArticleDetails=").append(xmlArticleDetails);
        sb.append('}');
        return sb.toString();
    }
}
