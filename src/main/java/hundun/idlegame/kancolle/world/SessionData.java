package hundun.idlegame.kancolle.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hundun.idlegame.kancolle.expedition.ExpeditionModel;
import hundun.idlegame.kancolle.resource.Resource;
import hundun.idlegame.kancolle.resource.ResourceBoard;
import hundun.idlegame.kancolle.ship.ShipModel;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/09/02
 */
@Data
public class SessionData {
    
    String id;
    List<ExpeditionModel> expeditions;
    List<ShipModel> idleShips;
    List<ShipModel> busyShips;
    ResourceBoard resourceBoard;
    
    //int sumTickCount;
    int year;
    int month;
    int day;
    int tick;
    
    public static SessionData newSession(String sessionId) {
        SessionData sessionData = new SessionData();
        sessionData.id = sessionId;
        
        sessionData.expeditions = new ArrayList<>();
        sessionData.idleShips = new ArrayList<>();
        sessionData.busyShips = new ArrayList<>();
        sessionData.resourceBoard = new ResourceBoard();
        sessionData.resourceBoard.setFuel(0);
        sessionData.resourceBoard.setAmmo(0);
        sessionData.resourceBoard.setSteel(0);
        sessionData.resourceBoard.setBauxite(0);
        
        //sessionData.sumTickCount = 0;
        sessionData.year = 1;
        sessionData.month = 1;
        sessionData.day = 1;
        sessionData.tick = 0;
        
        return sessionData;
    }
}
