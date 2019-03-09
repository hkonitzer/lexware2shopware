package space.schellenberger.etl.shopware2lexware.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * ArticleMainDetail DTO
 * @author Hendrik Schellenberger
 *
 * @see <a href="https://developers.shopware.com/developers-guide/rest-api/models/#article-detail">https://developers.shopware.com/developers-guide/rest-api/models/#article-detail</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ArticleMainDetailDTO {

    private final static Logger LOG = LoggerFactory.getLogger(ArticleMainDetailDTO.class);

    private class ArticleMainDetailPricesDTO {
        private Integer from = 1;
        private String to = "beliebig";
        private Float price;
        private Float pseudoPrice = 0f;
        private Float percent = 0f;

        public ArticleMainDetailPricesDTO(Float price) {
            setPrice(price);
        }

        public Integer getFrom() {
            return from;
        }

        public void setFrom(Integer from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public Float getPrice() {
            return price;
        }

        public void setPrice(Float price) {
            this.price = price;
        }

        public Float getPseudoPrice() {
            return pseudoPrice;
        }

        public void setPseudoPrice(Float pseudoPrice) {
            this.pseudoPrice = pseudoPrice;
        }

        public Float getPercent() {
            return percent;
        }

        public void setPercent(Float percent) {
            this.percent = percent;
        }
    }

    private Integer id;
    private Integer articleId;
    private Integer unitId;
    private String number;
    private String supplierNumber;
    private String descriptionLong;
    private Integer kind;
    private String additionalText;
    private Boolean active;
    private Long inStock = 0l;
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
    private ArrayList<ArticleMainDetailPricesDTO> prices = new ArrayList<>(1);

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
        //@TODO: Baue eine Regex
        this.number = number
                .replaceAll(" ", "_")
                .replaceAll("\\(","-")
                .replaceAll("\\)", "-")
                .replaceAll("/","-")
                .replaceAll("ü","ue")
                .replaceAll("ä","ae")
                .replaceAll("ö","oe")
                .replaceAll(",",".")
                .replaceAll("\\+","plus");
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

    public Long getInStock() {
        return inStock;
    }

    public void setInStock(Long inStock) {
        this.inStock = inStock;
    }

    public void setInStock(String inStock) {
        try {
            this.inStock = Math.round(Double.parseDouble(inStock)); // vermeide Kommawerte
        } catch (NumberFormatException nex) {
            LOG.warn(String.format("setInStock fehlgeschlagen, Wertumwandlung von %s fehlgeschlagen", inStock));
        }
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

    public List<ArticleMainDetailPricesDTO> getPrices() {
        return prices;
    }

    public void setPrices(ArrayList<ArticleMainDetailPricesDTO> prices) {
        this.prices = prices;
    }

    @JsonIgnore
    public void setPrice(Float price) {
        this.prices.clear();
        addPrice(price);
    }

    @JsonIgnore
    public void addPrice(Float price) {
        getPrices().add(new ArticleMainDetailPricesDTO(price));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ArticleMainDetailDTO{");
        sb.append("id=").append(id);
        sb.append(", articleId=").append(articleId);
        sb.append(", number='").append(number).append('\'');
        sb.append(", supplierNumber='").append(supplierNumber).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
