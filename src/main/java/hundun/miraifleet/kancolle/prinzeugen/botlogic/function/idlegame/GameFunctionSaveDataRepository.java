package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.idlegame;

import java.io.File;
import java.util.function.Function;

import hundun.miraifleet.framework.core.helper.repository.MapDocumentRepository;
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
    
    
    public GameFunctionSaveData findOneByGameSessionId(String gameSessionId) {
        return findOneByFilter(document -> document.getData().getId(), gameSessionId);
    }

}
