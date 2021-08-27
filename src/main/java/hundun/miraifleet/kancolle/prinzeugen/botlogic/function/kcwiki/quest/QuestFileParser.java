package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * @author hundun
 * Created on 2021/08/28
 */
public class QuestFileParser {
    
    ObjectMapper objectMapper;
    
    public QuestFileParser() {
        objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    public static void main(String[] args) {
        QuestFileParser parser = new QuestFileParser();
        File file = new File("101.json");
        try {
            KcwikiQuestData document = parser.parse(file);
            System.out.println(document.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public KcwikiQuestData parse(File file) throws IOException {
        KcwikiQuestData document = objectMapper.readValue(file, KcwikiQuestData.class);
        return document;
    }

    public OldKcwikiQuestDocument parseOldKcwikiQuestDocument(File file) throws IOException {
        InputStream inputStream;

        inputStream = new FileInputStream(file);
        Yaml yaml = new Yaml();
        Iterable<Object> objs = yaml.loadAll(inputStream);
        for (Object obj : objs) {
            // only first
            OldKcwikiQuestDocument document = objectMapper.readValue(objectMapper.writeValueAsString(obj), OldKcwikiQuestDocument.class);
            return document;
        }
        return null;
    }

}
