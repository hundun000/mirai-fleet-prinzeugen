package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import hundun.miraifleet.framework.helper.repository.MapDocumentRepository;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.domain.dto.KcwikiInitEquip;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.domain.dto.WhoCallsTheFleetItem;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.domain.model.ShipInfo;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.oldversion.OldKcwikiQuestData;
import net.mamoe.mirai.utils.MiraiLogger;

/**
 * @author hundun
 * Created on 2022/07/13
 */
public class WhoCallsTheFleetService {

    private final MapDocumentRepository<WhoCallsTheFleetItem> itemRepository;
    private final MiraiLogger logger;
    
    static ObjectMapper objectMapper;
    
    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    public WhoCallsTheFleetService(MapDocumentRepository<WhoCallsTheFleetItem> itemRepository,
            MiraiLogger logger
            ) {
        this.itemRepository = itemRepository;
        this.logger = logger;
    }

    public String loadItemFiles(File file) {
        List<WhoCallsTheFleetItem> items = null;
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                items = br.lines()
                        .filter(line -> !line.isBlank())
                        .map(line -> {
                            try {
                                return objectMapper.readValue(line, WhoCallsTheFleetItem.class);
                            } catch (Exception e) {
                                logger.error("readValue error for: " + line, e);
                                return null;
                            }
                        })
                        .filter(item -> item != null)
                        .collect(Collectors.toList())
                        ;
                itemRepository.deleteAll();
                itemRepository.saveAll(items);
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return ("导入有效数据" + (items != null ? items.size() : 0) + "个");
    }

    public void tryFillInitItem(ShipInfo detail) {
        if (detail.getInitEquip() == null) {
            detail.setInitEquip(new KcwikiInitEquip());
        }
        if (detail.getInitEquip().getSlot_names() == null) {
            var names = itemRepository.findAllByFilter(
                    null, 
                    item -> {
                        if (item.getDefault_equipped_on() != null) {
                            return item.getDefault_equipped_on().contains(detail.getId());
                        }
                        return false;
                    }, 
                    true)
                    .stream()
                    .map(item -> item.getName().getZh_cn())
                    .collect(Collectors.toList())
                    ;
            detail.getInitEquip().setSlot_names(names);
            logger.info(String.format("id = %s FillInitItem size %s", detail.getId(), names.size()));
        }
    }

}
