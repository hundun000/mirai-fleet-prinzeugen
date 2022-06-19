package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.idlegame;

import java.io.File;
import hundun.miraifleet.framework.helper.repository.MapDocumentRepository;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;

/**
 * @author hundun
 * Created on 2021/09/06
 */
public class GameFunctionSaveDataRepository extends MapDocumentRepository<GameFunctionSaveData> {

    public GameFunctionSaveDataRepository(
            JvmPlugin plugin, File file) {
        super(
                plugin, 
                file, 
                GameFunctionSaveData.class, 
                (item -> item.getId()), 
                ((item, id) -> item.setId(id))
                );
    }
    
    @Override
    public void delete(GameFunctionSaveData item) {
        String id = item.getId();
        item.setId("DELETED-" + item.getId());
        saveAndWriteFile(item, false);
        deleteById(id);
    }
    
    public GameFunctionSaveData findOneByGameSessionId(String gameSessionId) {
        return findOneByFilter(document -> document.getData().getId(), gameSessionId);
    }

}
