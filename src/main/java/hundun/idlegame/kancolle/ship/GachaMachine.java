package hundun.idlegame.kancolle.ship;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.world.ComponentContext;
import hundun.idlegame.kancolle.world.SessionData;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/09/18
 */
public class GachaMachine {
    
    
    Random random = new Random();
    
    ComponentContext context;
    
    public GachaMachine(ComponentContext context) {
        this.context = context;
    }
    
    public GachaResult gachaShip(int[] inputResources, int inputRarity) {
        Collection<ShipPrototype> prototypes = context.getShipFactory().getPrototypes();
        Map<ShipPrototype, Double> gachaWeights = new LinkedHashMap<>();
        double sumWeight = 0.0;
        for (ShipPrototype prototype : prototypes) {
            // filter phase
            if (prototype.getGachaRarity() > inputRarity) {
                continue;
            }
            
            if (prototype.getStandardGachaResources() == null) {
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
        return result;
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
        
    }

}
