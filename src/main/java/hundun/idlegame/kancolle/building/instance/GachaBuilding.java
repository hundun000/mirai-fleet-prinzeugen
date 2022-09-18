package hundun.idlegame.kancolle.building.instance;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.building.BuildingModel;
import hundun.idlegame.kancolle.data.SessionData;
import hundun.idlegame.kancolle.ship.ShipModel;
import hundun.idlegame.kancolle.ship.ShipPrototype;
import hundun.idlegame.kancolle.world.ComponentContext;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/09/18
 */
public class GachaBuilding extends BuildingModel {
    
    
    Random random = new Random();
    
    
    public GachaBuilding(ComponentContext context) {
        super(BuidingId.GACHA_BUILDING, context);
    }
    
    public GachaResult gachaShip(SessionData sessionData, int[] inputResources) {
        List<ShipModel> workers = getWorkers(sessionData);
        int maxGachaRarity = workers.size() + 1;
        Collection<ShipPrototype> prototypes = context.getShipFactory().getPrototypes();
        Map<ShipPrototype, Double> gachaWeights = new LinkedHashMap<>();
        double sumWeight = 0.0;
        for (ShipPrototype prototype : prototypes) {
            // filter phase
            if (prototype.getGachaRarity() > maxGachaRarity) {
                continue;
            }
            if (prototype.getStandardGachaResources() == null) {
                continue;
            }
            boolean hasMinGachaResources = hasMinGachaResources(inputResources, prototype.getStandardGachaResources());
            if (!hasMinGachaResources) {
                continue;
            }
            
            double distance = distance(inputResources, prototype.getStandardGachaResources());
            double weight = distance * (1.0 / prototype.getGachaRarity());
            sumWeight += weight;
            gachaWeights.put(prototype, weight);
        }
        double targetPosition = random.nextDouble() * sumWeight;
        double currentPosition = targetPosition;
        ShipPrototype targetPrototype = null;
        for (Entry<ShipPrototype, Double> entry : gachaWeights.entrySet()) {
            currentPosition -= entry.getValue();
            if (currentPosition <= 0.0) {
                targetPrototype = entry.getKey();
                break;
            }
        }
        GachaResult result = new GachaResult();
        result.setPayload(targetPrototype);
        result.setSumWeight(sumWeight);
        result.setGachaWeights(gachaWeights.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().getId(), entry -> entry.getValue())));
        result.setTargetPosition(targetPosition);
        result.setMaxGachaRarity(maxGachaRarity);
        return result;
    }
    
    private boolean hasMinGachaResources(int[] inputResources, int[] standardGachaResources) {
        for (int i = 0; i < inputResources.length; i++) {
            int limit = standardGachaResources[i];
            if (inputResources[i] < limit) {
                return false;
            }
        }
        return true;
    }

    private double distance(int[] a, int[] b) {
        double diff_square_sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            diff_square_sum += (a[i] - b[i]) * (a[i] - b[i]);
        }
        return Math.sqrt(diff_square_sum);
    }
    
    @Data
    public static class GachaResult {
        ShipPrototype payload;
        Map<String, Double> gachaWeights;
        double sumWeight;
        double targetPosition;
        int maxGachaRarity;
        
    }

}
