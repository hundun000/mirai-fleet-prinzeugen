package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.newversion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.newversion.TranslatorDictionary.Req;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.oldversion.OldKcwikiQuestDocument;



/**
 * @author hundun
 * Created on 2021/08/28
 */
public class QuestFileParser {
    
    static ObjectMapper objectMapper;
    
    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    public static void main(String[] args) {
        File file = new File("101.json");
        try {
            KcwikiQuestData document = parseKcwikiQuestData(file);
            System.out.println(document.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static TranslatorDictionary parseTranslatorDictionary(File file) throws IOException {
        ObjectNode root = (ObjectNode) objectMapper.readTree(file);
        ObjectNode reqNode = (ObjectNode) root.remove("req");
        Req req = objectMapper.treeToValue(reqNode, Req.class);
        TranslatorDictionary dictionary = new TranslatorDictionary(root, req);
        return dictionary;
    }
    
    public static KcwikiQuestData parseKcwikiQuestData(File file) throws IOException {
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
