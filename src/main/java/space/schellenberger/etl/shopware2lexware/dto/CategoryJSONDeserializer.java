package space.schellenberger.etl.shopware2lexware.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CategoryJSONDeserializer extends StdDeserializer<CategoryDTO> {

    private final static DateTimeFormatter SHOPWARE_API_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

    public CategoryJSONDeserializer() {
        this(null);
    }

    public CategoryJSONDeserializer(Class<?> vc) {
        super(vc);
    }

    private CategoryDTO getCategoryObjectFromJsonNode(JsonNode node) {
        CategoryDTO categoryObject = new CategoryDTO();
        //@TODO: Built whole object from response
        categoryObject.setId(node.get("id").asInt());
        if (node.has("parentId")) {
            if (node.get("parentId").asText().equalsIgnoreCase("null"))
                categoryObject.setParentId(null);
            else if (node.get("parentId").asText().equalsIgnoreCase("0"))
                categoryObject.setParentId(null);
            else
                categoryObject.setParentId(node.get("parentId").asInt());
        }
        if (node.has("streamId"))
            categoryObject.setStreamId(node.get("streamId").asInt());
        if (node.has("name"))
            categoryObject.setName(node.get("name").asText());
        if (node.has("position"))
            categoryObject.setPosition(node.get("position").asInt());
        if (node.has("metaTitle"))
            categoryObject.setMetaTitle(node.get("metaTitle").asText());
        if (node.has("metaDescription"))
            categoryObject.setMetaDescription(node.get("metaDescription").asText());
        if (node.has("cmsHeadline"))
            categoryObject.setCmsHeadline(node.get("cmsHeadline").asText());
        if (node.has("cmsText"))
            categoryObject.setCmsText(node.get("cmsText").asText());
        if (node.has("active"))
            categoryObject.setActive(node.get("active").asBoolean());
        if (node.has("template"))
            categoryObject.setTemplate(node.get("template").asText());
        if (node.has("added"))
            categoryObject.setAdded(LocalDateTime.parse(node.get("added").asText(), SHOPWARE_API_DATE_FORMATTER));
        if (node.has("changed")) {
            categoryObject.setChanged(LocalDateTime.parse(node.get("changed").asText(), SHOPWARE_API_DATE_FORMATTER));
        }
        return categoryObject;
    }

    @Override
    public CategoryDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode catNode = p.getCodec().readTree(p);
        if (catNode.has("success") && !catNode.get("success").asBoolean()) {
            return null;
        }
        if (catNode.has("success") && catNode.get("success").asBoolean()) { // direct API call
            return getCategoryObjectFromJsonNode(catNode.get("data"));
        } else {
            return getCategoryObjectFromJsonNode(catNode); // indirect
        }
    }
}
