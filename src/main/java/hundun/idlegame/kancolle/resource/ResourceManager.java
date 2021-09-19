package hundun.idlegame.kancolle.resource;
/**
 * @author hundun
 * Created on 2021/09/01
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import hundun.idlegame.kancolle.base.BaseManager;
import hundun.idlegame.kancolle.data.SessionData;
import hundun.idlegame.kancolle.data.config.HardCodeWorldConfig;
import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.event.IClockEventListener;
import hundun.idlegame.kancolle.event.LogTag;
import hundun.idlegame.kancolle.exception.BadGachaCommandException;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;
import hundun.idlegame.kancolle.format.DescriptionFormatter;
import hundun.idlegame.kancolle.format.ExceptionFormatter;
import hundun.idlegame.kancolle.world.ComponentContext;
import hundun.idlegame.kancolle.world.DataBus;
import lombok.Setter;

public class ResourceManager extends BaseManager {
    
    Map<String, Integer> minuteAwardResources;
    
    
    public ResourceManager(ComponentContext context) {
        super(context);

        minuteAwardResources = new HashMap<>();
        minuteAwardResources.put(HardCodeWorldConfig.RESOURCE_FUEL_ID, 3);
        minuteAwardResources.put(HardCodeWorldConfig.RESOURCE_AMMO_ID, 3);
        minuteAwardResources.put(HardCodeWorldConfig.RESOURCE_STEEL_ID, 3);
        minuteAwardResources.put(HardCodeWorldConfig.RESOURCE_BAUXITE_ID, 1);
    }
    
    public void merge(SessionData sessionData, String id, Integer delta) throws IdleGameException {
        merge(sessionData, Map.of(id, delta), 1);
    }
    
    public void merge(SessionData sessionData, Map<String, Integer> delta, int rate) throws IdleGameException {
        Map<String, ResourceModel> resources = sessionData.getResources();
        
        Map<String, Integer> updatedDelta = new HashMap<>();
        for (Entry<String, Integer> deltaEntry : delta.entrySet()) {
            Integer deltaAmount = deltaEntry.getValue() * rate;
            updatedDelta.put(deltaEntry.getKey(), deltaAmount);
        }
        
        
        for (Entry<String, Integer> entry : updatedDelta.entrySet()) {
            String resourceId = entry.getKey();
            Integer deltaAmount = entry.getValue();
            if (resources.containsKey(resourceId)) {
                BigDecimal newValue = resources.get(resourceId).getAmount().add(BigDecimal.valueOf(deltaAmount));
                resources.get(resourceId).setAmount(newValue);
            } else {
                ResourcePrototype prototype = context.getResourceFactory().getPrototype(resourceId);
                ResourceModel newModel = new ResourceModel();
                newModel.setPrototype(prototype);
                newModel.setAmount(BigDecimal.valueOf(deltaAmount));
                resources.put(resourceId, newModel);
            }
        }
        eventBus.sendResourceChangedEvent(sessionData, updatedDelta, resources);
    }

    
    private List<String> checkEnough(SessionData sessionData, Map<String, Integer> cost) {
        Map<String, ResourceModel> resources = sessionData.getResources();
        List<String> notEnoughResourceIds = new ArrayList<>(cost.size());
        for (Entry<String, Integer> entry : cost.entrySet()) {
            String resourceId = entry.getKey();
            Integer costAmount = entry.getValue();

            if (resources.containsKey(resourceId)) {
                BigDecimal newValue = resources.get(resourceId).getAmount().subtract(BigDecimal.valueOf(costAmount));
                if (newValue.compareTo(BigDecimal.ZERO) < 0) {
                    notEnoughResourceIds.add(resourceId);
                }
            } else {
                notEnoughResourceIds.add(resourceId);
            }
        }
        return notEnoughResourceIds;
    }

    public void consume(SessionData sessionData, Map<String, Integer> cost) throws IdleGameException {
        List<String> notEnoughResourceIds = checkEnough(sessionData, cost);
        if (notEnoughResourceIds.isEmpty()) {
            merge(sessionData, cost, -1);
        } else {
            List<ResourcePrototype> prototypes = context.getResourceFactory().listIdToPrototype(notEnoughResourceIds);
            throw BadGachaCommandException.resourcesNotEnough(prototypes);
        }
    }
}
