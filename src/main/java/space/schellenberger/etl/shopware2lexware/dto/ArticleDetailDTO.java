package space.schellenberger.etl.shopware2lexware.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * ArticleDetail DTO
 * @author Hendrik Schellenberger
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ArticleDetailDTO {
    private Integer id;
    private Integer articleId;
    private Integer unitId;
    private String number;
    private String supplierNumber;
    private String descriptionLong;
    private Integer kind;
    private String additionalText;
    private Boolean active;
    private Integer inStock;
    private Integer stockMin;
    private Boolean lastStock;
    private String weight;
    private String width;
    private String len;
    private String height;
    private String ean;
    private Float purchasePrice;
    private Integer position;
    private Integer minPurchase;
    private Integer purchaseSteps;
    private Integer maxPurchase;
    private String purchaseUnit;
    private String referenceUnit;
    private String packUnit;
    private Boolean shippingFree;
    private Object prices;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSupplierNumber() {
        return supplierNumber;
    }

    public void setSupplierNumber(String supplierNumber) {
        this.supplierNumber = supplierNumber;
    }

    public String getDescriptionLong() {
        return descriptionLong;
    }

    public void setDescriptionLong(String descriptionLong) {
        this.descriptionLong = descriptionLong;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public String getAdditionalText() {
        return additionalText;
    }

    public void setAdditionalText(String additionalText) {
        this.additionalText = additionalText;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getInStock() {
        return inStock;
    }

    public void setInStock(Integer inStock) {
        this.inStock = inStock;
    }

    public Integer getStockMin() {
        return stockMin;
    }

    public void setStockMin(Integer stockMin) {
        this.stockMin = stockMin;
    }

    public Boolean getLastStock() {
        return lastStock;
    }

    public void setLastStock(Boolean lastStock) {
        this.lastStock = lastStock;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLen() {
        return len;
    }

    public void setLen(String len) {
        this.len = len;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public Float getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Float purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getMinPurchase() {
        return minPurchase;
    }

    public void setMinPurchase(Integer minPurchase) {
        this.minPurchase = minPurchase;
    }

    public Integer getPurchaseSteps() {
        return purchaseSteps;
    }

    public void setPurchaseSteps(Integer purchaseSteps) {
        this.purchaseSteps = purchaseSteps;
    }

    public Integer getMaxPurchase() {
        return maxPurchase;
    }

    public void setMaxPurchase(Integer maxPurchase) {
        this.maxPurchase = maxPurchase;
    }

    public String getPurchaseUnit() {
        return purchaseUnit;
    }

    public void setPurchaseUnit(String purchaseUnit) {
        this.purchaseUnit = purchaseUnit;
    }

    public String getReferenceUnit() {
        return referenceUnit;
    }

    public void setReferenceUnit(String referenceUnit) {
        this.referenceUnit = referenceUnit;
    }

    public String getPackUnit() {
        return packUnit;
    }

    public void setPackUnit(String packUnit) {
        this.packUnit = packUnit;
    }

    public Boolean getShippingFree() {
        return shippingFree;
    }

    public void setShippingFree(Boolean shippingFree) {
        this.shippingFree = shippingFree;
    }

    public Object getPrices() {
        return prices;
    }

    public void setPrices(Object prices) {
        this.prices = prices;
    }
}
