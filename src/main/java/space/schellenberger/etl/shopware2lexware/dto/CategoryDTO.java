package space.schellenberger.etl.shopware2lexware.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CategoryDTO
 * @author Hendrik Schellenberger
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(using = CategoryJSONDeserializer.class)
public class CategoryDTO {

    private Integer id;
    private Integer parentId;
    private Integer streamId;
    private String name;
    private Integer position;
    private String metaTitle;
    private String metaKeywords;
    private String metaDescription;
    private String cmsHeadline;
    private String cmsText;
    private Boolean active;
    private String template;
    private String productBoxLayout;
    private Boolean blog;
    private String path;
    private Boolean showFilterGroups;
    private String external;
    private Boolean hideFilter;
    private Boolean hideTop;
    private Integer mediaId;
    private List<Object> attribute;
    private List<Object> emotions;
    private Object media;
    private List<Object> customerGroups;
    private Integer childrenCount;
    private Integer articleCount;
    private List<Object> translations;

    @JsonIgnore private LocalDateTime changed;
    @JsonIgnore private LocalDateTime added;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getStreamId() {
        return streamId;
    }

    public void setStreamId(Integer streamId) {
        this.streamId = streamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        if (isValidValue(metaTitle))
            this.metaTitle = metaTitle;
        else
            this.metaTitle = null;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        if (isValidValue(metaKeywords))
            this.metaKeywords = metaKeywords;
        else
            this.metaKeywords = null;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        if (isValidValue(metaDescription))
            this.metaDescription = metaDescription;
        else
            this.metaDescription = null;
    }

    public String getCmsHeadline() {
        return cmsHeadline;
    }

    public void setCmsHeadline(String cmsHeadline) {
        this.cmsHeadline = cmsHeadline;
    }

    public String getCmsText() {
        return cmsText;
    }

    public void setCmsText(String cmsText) {
        this.cmsText = cmsText;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getProductBoxLayout() {
        return productBoxLayout;
    }

    public void setProductBoxLayout(String productBoxLayout) {
        this.productBoxLayout = productBoxLayout;
    }

    public Boolean getBlog() {
        return blog;
    }

    public void setBlog(Boolean blog) {
        this.blog = blog;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        if (isValidValue(path)) {
            this.path = path;
        } else {
            this.path = null;
        }
    }

    public Boolean getShowFilterGroups() {
        return showFilterGroups;
    }

    public void setShowFilterGroups(Boolean showFilterGroups) {
        this.showFilterGroups = showFilterGroups;
    }

    public String getExternal() {
        return external;
    }

    public void setExternal(String external) {
        this.external = external;
    }

    public Boolean getHideFilter() {
        return hideFilter;
    }

    public void setHideFilter(Boolean hideFilter) {
        this.hideFilter = hideFilter;
    }

    public Boolean getHideTop() {
        return hideTop;
    }

    public void setHideTop(Boolean hideTop) {
        this.hideTop = hideTop;
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

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public List<Object> getAttribute() {
        return attribute;
    }

    public void setAttribute(List<Object> attribute) {
        this.attribute = attribute;
    }

    public List<Object> getEmotions() {
        return emotions;
    }

    public void setEmotions(List<Object> emotions) {
        this.emotions = emotions;
    }

    public Object getMedia() {
        return media;
    }

    public void setMedia(Object media) {
        this.media = media;
    }

    public List<Object> getCustomerGroups() {
        return customerGroups;
    }

    public void setCustomerGroups(List<Object> customerGroups) {
        this.customerGroups = customerGroups;
    }

    public Integer getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(Integer childrenCount) {
        this.childrenCount = childrenCount;
    }

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }

    public List<Object> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Object> translations) {
        this.translations = translations;
    }

    @JsonIgnore
    public boolean isValidValue(String text) {
        return text != null && !text.equalsIgnoreCase("null");
    }

    @JsonIgnore
    public void mergeFromShopwareObject(CategoryDTO shopwareCategoryDTO) {
        //see: LexwareCategoryXMLReader
        if (shopwareCategoryDTO.getId().intValue() != getId().intValue()) {
            throw new UnsupportedOperationException(String.format("Cannot merge CategoryObjects, IDs differ! Shopware: %s vs. own: %s", shopwareCategoryDTO.getId(), getId()));
        }
        if (shopwareCategoryDTO.getAdded() == null || shopwareCategoryDTO.getChanged() == null) {
            throw new UnsupportedOperationException(String.format("Cannot merge CategoryObjects, Given category object with id %s seems hat no added/changed dates (not in database?)", shopwareCategoryDTO.getId()));
        }
        //setParentId(shopwareCategoryDTO.getParentId());
        //setName(shopwareCategoryDTO.getName());
        //setMetaDescription(shopwareCategoryDTO.getMetaDescription());
        //setPosition(shopwareCategoryDTO.getPosition());
        setAdded(shopwareCategoryDTO.getAdded());
        setChanged(shopwareCategoryDTO.getChanged());
    }

    @JsonIgnore
    public boolean isInShopwareDB() {
        return getAdded() != null;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", position=" + position +
                ", metaTitle='" + metaTitle + '\'' +
                ", metaKeywords='" + metaKeywords + '\'' +
                ", metaDescription='" + metaDescription + '\'' +
                ", active=" + active +
                ", path='" + path + '\'' +
                '}';
    }
}
