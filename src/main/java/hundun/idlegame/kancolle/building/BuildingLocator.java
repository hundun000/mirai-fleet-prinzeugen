package hundun.idlegame.kancolle.building;

import java.util.HashMap;
import java.util.Map;

import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.exception.BuildingNotFoundException;
import hundun.idlegame.kancolle.world.DataBus;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2021/09/17
 */
public class BuildingLocator {
    
    @Getter
    private ExpeditionBuilding expeditionBuilding;
    
    private Map<String, BaseBuilding> buildings = new HashMap<>();
    
    
    public BuildingLocator(EventBus eventBus, DataBus dataBus) {
        expeditionBuilding = new ExpeditionBuilding(eventBus, dataBus);
        buildings.put(expeditionBuilding.getId(), expeditionBuilding);
    }
    
    public BaseBuilding getBuilding(String id) throws BuildingNotFoundException {
        if (!buildings.containsKey(id)) {
            throw new BuildingNotFoundException(id);
        }
        return buildings.get(id);
    }
}
