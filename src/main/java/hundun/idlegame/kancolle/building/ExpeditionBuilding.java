package hundun.idlegame.kancolle.building;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.event.IClockEventListener;
import hundun.idlegame.kancolle.event.LogTag;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.ship.ShipModel;
import hundun.idlegame.kancolle.world.ComponentContext;
import hundun.idlegame.kancolle.world.SessionData;
import hundun.idlegame.kancolle.world.WorldConfig;

/**
 * @author hundun
 * Created on 2021/09/16
 */
public class ExpeditionBuilding extends BaseBuilding implements IClockEventListener {

    public static final String ID = "远征中心";
    
    private Map<String, Integer> oneWorkerAwardResources;
    
    
    public ExpeditionBuilding(ComponentContext context) {
        super(ID, context);
        
        oneWorkerAwardResources = new HashMap<>();
        oneWorkerAwardResources.put(WorldConfig.RESOURCE_FUEL_ID, 3);
        oneWorkerAwardResources.put(WorldConfig.RESOURCE_AMMO_ID, 3);
        oneWorkerAwardResources.put(WorldConfig.RESOURCE_STEEL_ID, 3);
        oneWorkerAwardResources.put(WorldConfig.RESOURCE_BAUXITE_ID, 1);
    }
    
    



    @Override
    public void tick(SessionData sessionData) {
        List<ShipModel> worker = sessionData.getShips().stream().filter(ship -> this.id.equals(ship.getWorkInBuildingId())).collect(Collectors.toList());
        eventBus.log(sessionData.getId(), LogTag.BUILDING, "{} tick-work. worker size = {}", this.id, worker.size());
        try {
            dataBus.resourceMerge(sessionData, oneWorkerAwardResources, worker.size() + 1);
        } catch (IdleGameException e) {
            eventBus.log(sessionData.getId(), LogTag.ERROR, "minuteAwardResources error: {}", dataBus.getExceptionAdvice().exceptionToMessage(e));
        }
    }

}
