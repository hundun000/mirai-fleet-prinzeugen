package hundun.idlegame.kancolle.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hundun.idlegame.kancolle.expedition.ExpeditionModel;
import hundun.idlegame.kancolle.resource.ResourceModel;
import hundun.idlegame.kancolle.ship.ShipModel;
import hundun.idlegame.kancolle.time.GameCalendar;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/09/02
 */
@Data
public class SessionData {
    
    String id;
    List<ExpeditionModel> expeditions;
    //Set<ExpeditionModel> completedExpeditions;
    List<ShipModel> ships;
    Map<String, ResourceModel> resources;

    
    //int sumTickCount;
    GameCalendar calendar;
    
    public static SessionData newSession(String sessionId) {
        SessionData sessionData = new SessionData();
        sessionData.id = sessionId;
        
        sessionData.expeditions = new ArrayList<>();
//        sessionData.completedExpeditions = new HashSet<>();
        sessionData.ships = new ArrayList<>();
        sessionData.resources = new HashMap<>();
//        sessionData.resourceBoard = new ResourceBoard();
//        sessionData.resourceBoard.setFuel(0);
//        sessionData.resourceBoard.setAmmo(0);
//        sessionData.resourceBoard.setSteel(0);
//        sessionData.resourceBoard.setBauxite(0);
        
        //sessionData.sumTickCount = 0;
        GameCalendar calendar = new GameCalendar();
        calendar.setYear(1);
        calendar.setMonth(1);
        calendar.setDay(1);
        calendar.setTick(0);
        sessionData.calendar = calendar;
        
        return sessionData;
    }
}
