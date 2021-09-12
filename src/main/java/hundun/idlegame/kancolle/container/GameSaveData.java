package hundun.idlegame.kancolle.container;

import java.util.List;
import hundun.idlegame.kancolle.expedition.ExpeditionSaveData;
import hundun.idlegame.kancolle.resource.ResourceSaveData;
import hundun.idlegame.kancolle.ship.ShipSaveData;
import hundun.idlegame.kancolle.time.GameCalendar;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/09/02
 */
@Data
public class GameSaveData {
    
    String id;
    
    GameCalendar calendar;
    
    List<ExpeditionSaveData> expeditionSaveDatas;
    List<ShipSaveData> shipSaveDatas;
    List<ResourceSaveData> resourceSaveDatas;
}
