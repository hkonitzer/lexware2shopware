package space.schellenberger.etl.shopware2lexware.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ArticleJSONDeserializer extends StdDeserializer<ArticleDTO> {

    private final static DateTimeFormatter SHOPWARE_API_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

    public ArticleJSONDeserializer() {
        this(null);
    }

    public ArticleJSONDeserializer(Class<?> vc) {
        super(vc);
    }

    private ArticleDTO getArticleObjectFromJsonNode(JsonNode node) {
        ArticleDTO articleDTO = new ArticleDTO();
        //@TODO: Built whole object from response
        if (node.has("id"))
            articleDTO.setId(node.get("id").asInt());
        if (node.has("mainDetailId"))
            articleDTO.setMainDetailId(node.get("mainDetailId").asInt());
        if (node.has("supplierId"))
            articleDTO.setSupplierId(node.get("supplierId").asInt());
        if (node.has("taxId"))
            articleDTO.setTaxId(node.get("taxId").asInt());
        if (node.has("priceGroupId"))
            articleDTO.setPriceGroupId(node.get("priceGroupId").asInt());
        if (node.has("description"))
            articleDTO.setDescription(node.get("description").asText());
        if (node.has("descriptionLong"))
            articleDTO.setDescriptionLong(node.get("descriptionLong").asText());
        if (node.has("metaTitle"))
            articleDTO.setMetaTitle(node.get("metaTitle").asText());
        if (node.has("active"))
            articleDTO.setActive(node.get("active").asBoolean());
        if (node.has("template"))
            articleDTO.setTemplate(node.get("template").asText());
        if (node.has("added"))
            articleDTO.setAdded(LocalDateTime.parse(node.get("added").asText(), SHOPWARE_API_DATE_FORMATTER));
        if (node.has("changed")) {
            articleDTO.setChanged(LocalDateTime.parse(node.get("changed").asText(), SHOPWARE_API_DATE_FORMATTER));
        //@TODO: Baue mainDetail
        }
        return articleDTO;
    }

    @Override
    public ArticleDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode catNode = p.getCodec().readTree(p);
        if (catNode.has("success") && !catNode.get("success").asBoolean()) {
            return null;
        }
        if (catNode.has("success") && catNode.get("success").asBoolean()) { // direct API call
            return getArticleObjectFromJsonNode(catNode.get("data"));
        } else {
            return getArticleObjectFromJsonNode(catNode); // indirect
        }
    }
}
