package space.schellenberger.etl.shopware2lexware.batch.step.article;

import com.thoughtworks.xstream.converters.extended.NamedCollectionConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import space.schellenberger.etl.shopware2lexware.dto.ArticleDTO;
import space.schellenberger.etl.shopware2lexware.dto.CategoriesDTO;
import space.schellenberger.etl.shopware2lexware.dto.CategoryDTO;

import java.util.HashMap;
import java.util.Map;

public class LexwareArticleXMLReader {

    private final Logger log = LoggerFactory.getLogger(LexwareArticleXMLReader.class);

    private String fileName = null;

    public LexwareArticleXMLReader(String fileName) {
        this.fileName = fileName;
    }

    public XStreamMarshaller tradeMarshaller() {
        Map<String, Class> aliases = new HashMap<>();

        aliases.put("ARTICLE", ArticleDTO.class);
        aliases.put("FEATURE", ArticleDTO.ArticleFeaturesXMLDTO.class);
        aliases.put("FNAME", ArticleDTO.ArticleFeaturesXMLDTO.ArticleFeaturesFeatureXMLDTO.class);
        aliases.put("FVALUE", ArticleDTO.ArticleFeaturesXMLDTO.ArticleFeaturesFeatureXMLDTO.class);

        XStreamMarshaller marshaller = new XStreamMarshaller();

        marshaller.setAliases(aliases);
        marshaller.getXStream().processAnnotations(ArticleDTO.class);
        marshaller.getXStream().ignoreUnknownElements(); // beware!
        marshaller.getXStream().allowTypeHierarchy(ArticleDTO.class);

        return marshaller;
    }

    public StaxEventItemReader itemReader() {
        log.debug(String.format("Creating itemReader for file '%s'", fileName));
        return new StaxEventItemReaderBuilder<ArticleDTO>()
                .name("XMLitemReader")
                .resource(new FileSystemResource(fileName))
                .addFragmentRootElements("ARTICLE")
                .unmarshaller(tradeMarshaller())
                .build();
    }

}
