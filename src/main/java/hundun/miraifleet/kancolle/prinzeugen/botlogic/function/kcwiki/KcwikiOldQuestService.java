package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hundun.miraifleet.framework.helper.repository.MapDocumentRepository;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.newversion.QuestFileParser;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.oldversion.OldKcwikiQuestData;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.oldversion.OldKcwikiQuestDocument;
import net.mamoe.mirai.utils.MiraiLogger;

/**
 * @author hundun
 * Created on 2022/07/13
 */
public class KcwikiOldQuestService {
    
    private QuestFileParser questFileParser = new QuestFileParser();
    
    private final MapDocumentRepository<OldKcwikiQuestData> kcwikiQuestDataRepository;
    private final MiraiLogger logger;
    
    public KcwikiOldQuestService(MapDocumentRepository<OldKcwikiQuestData> kcwikiQuestDataRepository,
            MiraiLogger logger
            ) {
        this.kcwikiQuestDataRepository = kcwikiQuestDataRepository;
        this.logger = logger;
    }


    public String loadQuestFiles(File folder) {
        List<OldKcwikiQuestData> datas = new ArrayList<>();
        
        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                try {
                    OldKcwikiQuestDocument document = questFileParser.parseOldKcwikiQuestDocument(file);
                    if (!kcwikiQuestDataRepository.existsById(document.getData().getId())) {
                        datas.add(document.getData());
                    }
                } catch (Exception e) {
                    logger.warning("questFileParser.parse error: ", e);
                }
            }
        }
        kcwikiQuestDataRepository.saveAll(datas);
        return ("导入" + folder.listFiles().length + "个文件，有效数据：" + datas.size() + "个");


    }


    public OldKcwikiQuestData findById(String id) {
        return kcwikiQuestDataRepository.findById(id);
    }
    
    public String searchQuest(String questKeyword) {
        List<OldKcwikiQuestData> questDatas = kcwikiQuestDataRepository.findAll();
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (OldKcwikiQuestData questData : questDatas) {
            if (questData.getChinese_title().contains(questKeyword) || questData.getChinese_detail().contains(questKeyword)) {
                builder.append("id:").append(questData.getId()).append(" ").append(questData.getTitle()).append("\n");
                count++;
            }
        }
        
        if (count <= 10) {
            return ("找到" + count + "个结果:\n" + builder.toString());
        } else {
            return ("找到" + count + "个结果，结果数过多，请改为更明确的查询词");
        }
        
    }
    
}
