package hundun.idlegame.kancolle.ship;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.expedition.ExpeditionFactory;
import hundun.idlegame.kancolle.expedition.ExpeditionModel;
import hundun.idlegame.kancolle.expedition.ExpeditionSaveData;

/**
 * @author hundun
 * Created on 2021/09/02
 */
public class ShipFactory {
    
    private static Map<String, ShipPrototype> shipPrototypes = new HashMap<>();
    static {
        ShipPrototype prototype;
        prototype = new ShipPrototype();
        prototype.setId("吹雪");
        shipPrototypes.put(prototype.getId(), prototype);
        
        prototype = new ShipPrototype();
        prototype.setId("欧根");
        shipPrototypes.put(prototype.getId(), prototype);
    }
    
    public static ShipPrototype getPrototype(String id) {
        return shipPrototypes.get(id);
    }

    
    public static List<ShipModel> listSaveDataToModel(List<ShipSaveData> saveDatas) {
        List<ShipModel> shipModels = saveDatas.stream().map(saveData -> saveDataToModel(saveData)).collect(Collectors.toList());
        return shipModels;
    }

    public static List<ShipSaveData> listModelToSaveData(List<ShipModel> models) {
        List<ShipSaveData> expeditionSaveDatas = models.stream().map(model -> modelToSaveData(model)).collect(Collectors.toList());
        return expeditionSaveDatas;
    }
    public static ShipModel saveDataToModel(ShipSaveData saveData) {
        ShipPrototype prototype = getPrototype(saveData.getPrototypeId());
        ShipModel model = new ShipModel(prototype, saveData.getLevel(), saveData.getExp());
        return model;
    }
    public static ShipSaveData modelToSaveData(ShipModel model) {
        ShipSaveData saveData = new ShipSaveData(model.getPrototype().getId(), model.getLevel(), model.getExp());
        return saveData;
    }

}
