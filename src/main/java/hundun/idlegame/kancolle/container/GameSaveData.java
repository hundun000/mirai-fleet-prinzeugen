package hundun.idlegame.kancolle.container;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.expedition.ExpeditionModel;
import hundun.idlegame.kancolle.expedition.ExpeditionSaveData;
import hundun.idlegame.kancolle.resource.Resource;
import hundun.idlegame.kancolle.resource.ResourceBoard;
import hundun.idlegame.kancolle.ship.ShipFactory;
import hundun.idlegame.kancolle.ship.ShipModel;
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
    ResourceBoard resourceBoard;
}
