package hundun.idlegame.kancolle.resource;
/**
 * @author hundun
 * Created on 2021/09/01
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.event.IClockEventListener;
import hundun.idlegame.kancolle.event.IExpeditionEventListener;
import hundun.idlegame.kancolle.expedition.ExpeditionModel;
import hundun.idlegame.kancolle.world.BaseManager;
import hundun.idlegame.kancolle.world.SessionData;

public class ResourceManager extends BaseManager implements IExpeditionEventListener, IClockEventListener {
    
    Map<Resource, Integer> minuteAwardResources;
    
    
    public ResourceManager(EventBus eventBus) {
        super(eventBus);

        minuteAwardResources = new HashMap<>();
        minuteAwardResources.put(Resource.FUEL, 3);
        minuteAwardResources.put(Resource.AMMO, 3);
        minuteAwardResources.put(Resource.STEEL, 3);
        minuteAwardResources.put(Resource.BAUXITE, 1);
    }
    
    public void merge(ResourceBoard resourceBoard, Map<Resource, Integer> delta) {
        resourceBoard.setFuel(resourceBoard.getFuel() + delta.getOrDefault(Resource.FUEL, 0));
        resourceBoard.setAmmo(resourceBoard.getAmmo() + delta.getOrDefault(Resource.AMMO, 0));
        resourceBoard.setSteel(resourceBoard.getSteel() + delta.getOrDefault(Resource.STEEL, 0));
        resourceBoard.setBauxite(resourceBoard.getBauxite() + delta.getOrDefault(Resource.BAUXITE, 0));
    }

    @Override
    public void onExpeditionCompleted(SessionData sessionData, List<ExpeditionModel> completedTasks) {
        
        for (ExpeditionModel completedTask : completedTasks) {
            
            merge(sessionData.getResourceBoard(), completedTask.getPrototype().getResourceRewards());
            eventBus.sendResourceChangeEvent(sessionData, completedTask.getPrototype().getResourceRewards(), sessionData.getResourceBoard());
        }
        
    }
    
    public static String des(Map<Resource, Integer> resourceMap) {
        return new StringBuilder()
                .append("资源:")
                .append("油").append(resourceMap.getOrDefault(Resource.FUEL, 0)).append(" ")
                .append("弹").append(resourceMap.getOrDefault(Resource.AMMO, 0)).append(" ")
                .append("钢").append(resourceMap.getOrDefault(Resource.STEEL, 0)).append(" ")
                .append("铝").append(resourceMap.getOrDefault(Resource.BAUXITE, 0)).append(" ")
                .toString();
    }

    public static String des(ResourceBoard resourceBoard) {
        return new StringBuilder()
                .append("资源:")
                .append("油").append(resourceBoard.getFuel()).append(" ")
                .append("弹").append(resourceBoard.getAmmo()).append(" ")
                .append("钢").append(resourceBoard.getSteel()).append(" ")
                .append("铝").append(resourceBoard.getBauxite()).append(" ")
                .toString();
        
    }
            

    public String overviewResourceAmount(SessionData sessionData) {
        return des(sessionData.getResourceBoard());
    }


    @Override
    public void tick(SessionData sessionData) {
        merge(sessionData.getResourceBoard(), minuteAwardResources);
        eventBus.sendResourceChangeEvent(sessionData, minuteAwardResources, sessionData.getResourceBoard());
    }
}
