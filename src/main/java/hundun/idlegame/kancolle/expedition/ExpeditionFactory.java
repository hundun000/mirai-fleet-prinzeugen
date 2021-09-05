package hundun.idlegame.kancolle.expedition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.resource.Resource;
import hundun.idlegame.kancolle.ship.ShipFactory;
import hundun.idlegame.kancolle.ship.ShipModel;
import hundun.idlegame.kancolle.ship.ShipPrototype;

/**
 * @author hundun
 * Created on 2021/09/02
 */
public class ExpeditionFactory {
    
    private static Map<String, ExpeditionPrototype> prototypes = new HashMap<>();
    static {
        ExpeditionPrototype prototype;
        Map<Resource, Integer> resourceRewards;
        
        prototype = new ExpeditionPrototype();
        resourceRewards = new HashMap<>();
        resourceRewards.put(Resource.FUEL, 200);
        prototype.setResourceRewards(resourceRewards);
        prototype.setId("A1");
        prototype.setTick(4);
        prototype.setRewardExp(10);
        prototypes.put(prototype.getId(), prototype);
        
    }
    
    public static ExpeditionPrototype getPrototype(String id) {
        return prototypes.get(id);
    }
    
    public static List<ExpeditionModel> listSaveDataToModel(List<ExpeditionSaveData> saveDatas) {
        List<ExpeditionModel> models = saveDatas.stream().map(saveData -> ExpeditionFactory.saveDataToModel(saveData)).collect(Collectors.toList());
        return models;
    }
    
    public static List<ExpeditionSaveData> listModelToSaveData(List<ExpeditionModel> models) {
        List<ExpeditionSaveData> expeditionSaveDatas = models.stream().map(model -> ExpeditionFactory.modelToSaveData(model)).collect(Collectors.toList());
        return expeditionSaveDatas;
    }
    
    public static ExpeditionModel saveDataToModel(ExpeditionSaveData saveData) {
        ExpeditionPrototype prototype = getPrototype(saveData.getExpeditionId());
        ExpeditionModel model = new ExpeditionModel(saveData.getShipIds(), prototype, saveData.getRemainTick());
        return model;
    }
    
    public static ExpeditionSaveData modelToSaveData(ExpeditionModel model) {
        List<String> shipIds = model.getShipIds();
        String expeditionId = model.getPrototype().getId();
        ExpeditionSaveData saveData = new ExpeditionSaveData(shipIds, expeditionId, model.getRemainTick());
        return saveData;
    }

}
