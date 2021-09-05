package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.idlegame;

import hundun.idlegame.kancolle.container.GameSaveData;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/09/03
 */
@Data
public class GameFunctionSaveData {
    String id;
    Player player;
    GameSaveData data;
}
