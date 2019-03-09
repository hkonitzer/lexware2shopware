package space.schellenberger.etl.shopware2lexware.batch.step.category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import space.schellenberger.etl.shopware2lexware.dto.CategoriesDTO;
import space.schellenberger.etl.shopware2lexware.dto.CategoryDTO;

import java.util.HashMap;
import java.util.Map;

public class LexwareCategoryXMLReader {

    private final Logger log = LoggerFactory.getLogger(LexwareCategoryXMLReader.class);

    private String fileName = null;

    public LexwareCategoryXMLReader(String fileName) {
        this.fileName = fileName;
    }

    public XStreamMarshaller tradeMarshaller() {
        Map<String, Class> aliases = new HashMap<>();
        aliases.put("CATALOG_STRUCTURE", CategoryDTO.class);

        Map<Class<?>, String> omittedFields = new HashMap<>();
        omittedFields.put(CategoryDTO.class, "USER_DEFINED_EXTENSIONS");

        XStreamMarshaller marshaller = new XStreamMarshaller();

        marshaller.setAliases(aliases);
        marshaller.setOmittedFields(omittedFields);

        marshaller.getXStream().allowTypeHierarchy(CategoryDTO.class);
        marshaller.getXStream().aliasField("GROUP_DESCRIPTION", CategoryDTO.class, "metaDescription");
        marshaller.getXStream().aliasField("GROUP_ID", CategoryDTO.class, "id");
        marshaller.getXStream().aliasField("GROUP_NAME", CategoryDTO.class, "name");
        marshaller.getXStream().aliasField("PARENT_ID", CategoryDTO.class, "parentId");
        marshaller.getXStream().aliasAttribute(CategoryDTO.class, "type", "type");

        return marshaller;
    }

    public StaxEventItemReader itemReader() {
        log.debug(String.format("Creating itemReader for file '%s'", fileName));
        return new StaxEventItemReaderBuilder<CategoriesDTO>()
                .name("XMLitemReader")
                .resource(new FileSystemResource(fileName))
                .addFragmentRootElements("CATALOG_STRUCTURE")
                .unmarshaller(tradeMarshaller())
                .build();
    }

}
