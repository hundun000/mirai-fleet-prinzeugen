package hundun.idlegame.kancolle.container;

import java.util.List;
import hundun.idlegame.kancolle.expedition.ExpeditionSaveData;
import hundun.idlegame.kancolle.resource.ResourceSaveData;
import hundun.idlegame.kancolle.ship.ShipSaveData;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/09/02
 */
@Data
public class GameSaveData {
    
    String id;
    
    //int sumTickCount;
    int year;
    int month;
    int day;
    int tick;
    
    List<ExpeditionSaveData> expeditionSaveDatas;
    List<ShipSaveData> idleShipSaveDatas;
    List<ShipSaveData> busyShipSaveDatas;
    List<ResourceSaveData> resourceSaveDatas;
}
