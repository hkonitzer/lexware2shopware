package space.schellenberger.etl.shopware2lexware.batch.step.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import space.schellenberger.etl.shopware2lexware.dto.Article2CatalogGroupDTO;
import space.schellenberger.etl.shopware2lexware.dto.ArticleDTO;

import java.util.HashMap;
import java.util.Map;

public class LexwareArticle2CatalogGroupXMLReader {

    private final Logger log = LoggerFactory.getLogger(LexwareArticle2CatalogGroupXMLReader.class);

    private String fileName = null;

    public LexwareArticle2CatalogGroupXMLReader(String fileName) {
        this.fileName = fileName;
    }

    public XStreamMarshaller tradeMarshaller() {
        Map<String, Class> aliases = new HashMap<>();

        aliases.put("ARTICLE_TO_CATALOGGROUP_MAP", Article2CatalogGroupDTO.class);

        XStreamMarshaller marshaller = new XStreamMarshaller();

        marshaller.setAliases(aliases);
        marshaller.getXStream().processAnnotations(Article2CatalogGroupDTO.class);
        marshaller.getXStream().ignoreUnknownElements(); // beware!
        marshaller.getXStream().allowTypeHierarchy(Article2CatalogGroupDTO.class);

        return marshaller;
    }

    public StaxEventItemReader itemReader() {
        log.debug(String.format("Creating itemReader for file '%s'", fileName));
        return new StaxEventItemReaderBuilder<Article2CatalogGroupDTO>()
                .name("XMLitemReader")
                .resource(new FileSystemResource(fileName))
                .addFragmentRootElements("ARTICLE_TO_CATALOGGROUP_MAP")
                .unmarshaller(tradeMarshaller())
                .build();
    }

}
