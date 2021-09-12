package hundun.idlegame.kancolle.resource;
/**
 * @author hundun
 * Created on 2021/09/01
 */

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.event.IClockEventListener;
import hundun.idlegame.kancolle.event.LogTag;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;
import hundun.idlegame.kancolle.format.DescriptionFormatter;
import hundun.idlegame.kancolle.format.SimpleExceptionFormatter;
import hundun.idlegame.kancolle.world.BaseManager;
import hundun.idlegame.kancolle.world.DataBus;
import hundun.idlegame.kancolle.world.SessionData;

public class ResourceManager extends BaseManager implements IClockEventListener {
    
    Map<String, Integer> minuteAwardResources;
    
    
    public ResourceManager(EventBus eventBus, DataBus dataBus) {
        super(eventBus, dataBus);

        minuteAwardResources = new HashMap<>();
        minuteAwardResources.put("FUEL", 3);
        minuteAwardResources.put("AMMO", 3);
        minuteAwardResources.put("STEEL", 3);
        minuteAwardResources.put("BAUXITE", 1);
    }
    
    public void merge(SessionData sessionData, Map<String, Integer> delta) throws IdleGameException {
        Map<String, ResourceModel> resources = sessionData.getResources();
        
        for (Entry<String, Integer> deltaEntry : delta.entrySet()) {
            String resourceId = deltaEntry.getKey();
            Integer deltaAmount = deltaEntry.getValue();
            if (resources.containsKey(resourceId)) {
                BigDecimal newValue = resources.get(resourceId).getAmount().add(BigDecimal.valueOf(deltaAmount));
                resources.get(resourceId).setAmount(newValue);
            } else {
                ResourcePrototype prototype = ResourceFactory.INSTANCE.getPrototype(resourceId);
                ResourceModel newModel = new ResourceModel();
                newModel.setPrototype(prototype);
                newModel.setAmount(BigDecimal.valueOf(deltaAmount));
                resources.put(resourceId, newModel);
            }
        }
        eventBus.sendResourceChangedEvent(sessionData, delta, resources);
    }
//
//    @Override
//    public void onExpeditionCompleted(SessionData sessionData, List<ExpeditionModel> completedTasks) {
//        
//        for (ExpeditionModel completedTask : completedTasks) {
//            
//            merge(sessionData.getResourceBoard(), completedTask.getPrototype().getResourceRewards());
//            eventBus.sendResourceChangeEvent(sessionData, completedTask.getPrototype().getResourceRewards(), sessionData.getResourceBoard());
//        }
//        
//    }
    



    @Override
    public void tick(SessionData sessionData) {
        try {
            dataBus.resourceMerge(sessionData, minuteAwardResources);
        } catch (IdleGameException e) {
            eventBus.log(sessionData.getId(), LogTag.ERROR, "minuteAwardResources error: {}", dataBus.getExceptionAdvice().exceptionToMessage(e));
        }
        //eventBus.sendResourceChangeEvent(sessionData, minuteAwardResources, sessionData.getResourceBoard());
    }
}
